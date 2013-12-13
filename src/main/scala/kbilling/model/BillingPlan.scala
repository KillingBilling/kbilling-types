package kbilling.model

import BillingPlan.Vars

object BillingPlan {
  type Vars = Map[String, BigDecimal]
}

trait BillingPlan {
  val accounts: Map[String, Account]
  val notifications: Set[Notification]
  final lazy val aggregates = (accounts.values collect {case a: ServiceAccount => a} flatMap {_.aggregates}).toMap
}

sealed trait Account

case class ServiceAccount(aggregates: Map[String, Aggregate]) extends Account

case class PaymentAccount(cost: Vars => BigDecimal) extends Account

case class Aggregate(
  aggr: (BigDecimal, BigDecimal) => BigDecimal = {(a, b) => a + b},
  init: Option[BigDecimal] => BigDecimal = {_ => 0}
)

case class Notification(predicate: Vars => Boolean, name: String)
