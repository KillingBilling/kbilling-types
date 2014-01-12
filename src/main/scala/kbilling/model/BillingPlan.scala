package kbilling.model

import BillingPlan._

object BillingPlan {
  type Vars = Map[String, BigDecimal]
  @inline def !!(acck: String, aggk: String) = s"$acck.$aggk"
  val _COST = "_COST"
  @inline def COST(acck: String) = !!(acck, _COST)
}

trait BillingPlan {

  val accounts: Map[String, Account]
  val notifications: Map[String, Notification]

  final lazy val aggregates: Map[String, Aggregate] = (for {
    (acck, account) <- accounts
    (aggk, aggregate) <- account.aggregates
  } yield (!!(acck, aggk), aggregate)).toMap

}

trait Account {
  def aggregates: Map[String, Aggregate]
}

case class ServiceAccount(aggregates: Map[String, Aggregate]) extends Account

case class PaymentAccount(cost: Vars => BigDecimal, aggregates: Map[String, Aggregate] = Map()) extends Account

case class Aggregate(
  aggr: (BigDecimal, BigDecimal) => BigDecimal = {(a, b) => a + b},
  init: Option[BigDecimal] => BigDecimal = {_ => 0}
)

case class Notification(predicate: Vars => Boolean)
