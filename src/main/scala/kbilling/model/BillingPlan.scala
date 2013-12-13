package kbilling.model

import BillingPlan.Vars

object BillingPlan {
  type Vars = Map[String, BigDecimal]
}

trait BillingPlan {
  val accounts: Map[String, Account]
  val notifications: Set[Notification]
  final lazy val aggregates = (accounts.values flatMap {_.aggregates}).toMap
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

case class Notification(predicate: Vars => Boolean, name: String)
