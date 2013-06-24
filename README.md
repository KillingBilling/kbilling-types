# kbilling-types
The only required dependency for your [KillingBilling](https://www.killingbilling.com/) projects. Read [Getting Started](https://www.killingbilling.com/docs/001.Getting-Started) for a quick start.

# Billing Plan structure

**Billing plans** are formal descriptions of bill calculation rules used by service oriented businesses. KillingBilling transforms them to actual working billing calculation programs for you.

Any _billing plan_ definition consists of:

*   a set of named **accounts**
*   a set of named **notification rules**

```scala
trait BillingPlan {
  val accounts: Map[String, Account]
  val notifications: Set[Notification]
}
```

An _account_ is either:

*   **service account**
*   **payment account**

An _account_ represents a variable (of `BigDecimal` type) that will store the amount of some resource available for a Client. These resources are either service resources or monetary resources.

A _service account_ defines a (possibly empty) set of named **aggregates**.
```scala
case class ServiceAccount(aggregates: Map[String, Aggregate])
  extends Account
```

An _aggregate_ represents a variable that will store the aggregate value:

*   initialized on billing cycle start by its `init` function (optionally, from the previous aggregated value),
*   calculated by its `aggr` function from the previous aggregated value and the **compensation** amount on the parent service account.

**Service account** values never really go below `0.00`, because that would mean that the Client would pay the Provider back with the same service. Instead, when the amount consumed is greater than the **service account**'s current value, the _compensation amount_ (the absolute of the amount consumed below `0.00`) is applied to the **service account**'s **aggregates** with their `aggr` functions.

```scala
case class Aggregate(
  aggr: (BigDecimal, BigDecimal, DateTime) => BigDecimal,
  init: Option[BigDecimal] => BigDecimal
)
```

These **aggregate** values are then used, together with other available **account** values as inputs to the **cost function**.

A _payment account_ defines the **cost function**. A _cost function_ is what actually calculates the total cost of all the services consumed by the Client in the current billing cycle.

```scala
type Vars = Map[String, BigDecimal]

case class PaymentAccount(cost: Vars => BigDecimal) extends Account
```

Every new transaction (a set of additions or consumptions of resources on Clients' accounts) results in the following **payment account** balance calculation for each affected Client:

```
new_amount = previous_amount - (new_cost - previous_cost)
```
