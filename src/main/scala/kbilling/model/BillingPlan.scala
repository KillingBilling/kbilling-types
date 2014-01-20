package kbilling.model

import BillingPlan._

object BillingPlan {

  type Vars = Map[String, BigDecimal]
  val COST = "cost"

  @inline def costAcc(acc: String) = acc $ COST

  implicit class AccountKey(val accKey: String) extends AnyVal {
    @inline def $(aggKey: String): String = s"$accKey.$aggKey"
  }

}

trait BillingPlan {

  val accounts: Map[String, Account]
  val notifications: Map[String, Notification]

  final lazy val aggregates: Map[String, Aggregate] = (for {
    (acck, account) <- accounts
    (aggk, aggregate) <- account.aggregates
  } yield (acck $ aggk) -> aggregate).toMap

}

sealed trait Account {
  def aggregates: Map[String, Aggregate]
}

case class ServiceAccount(aggregates: Map[String, Aggregate]) extends Account

case class PaymentAccount(cost: Vars => BigDecimal, aggregates: Map[String, Aggregate] = Map()) extends Account

case class Aggregate(
  aggr: (BigDecimal, BigDecimal) => BigDecimal = {(a, b) => a + b},
  init: Option[BigDecimal] => BigDecimal = {_ => 0}
)

case class Notification(predicate: Vars => Boolean)
