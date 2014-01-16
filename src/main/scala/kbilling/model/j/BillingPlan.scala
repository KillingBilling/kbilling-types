package kbilling.model.j

import java.util.{Map => JMap}
import java.math.BigDecimal

trait BillingPlan {
  def getServiceAccounts: JMap[String, ServiceAccount]
  def getPaymentAccounts: JMap[String, PaymentAccount]
  def getNotifications: JMap[String, Notification]
}

trait Account {
  def aggregates: JMap[String, Aggregate]
}

trait ServiceAccount extends Account

trait PaymentAccount extends Account {
  def cost(vars: JMap[String, BigDecimal]): BigDecimal
}

trait Aggregate {
  def aggr(a: BigDecimal, b: BigDecimal): BigDecimal
  def init(x: BigDecimal): BigDecimal // x can be null - there was no value prior to this billing cycle
}

trait Notification {
  def predicate(vars: JMap[String, BigDecimal]): Boolean
}
