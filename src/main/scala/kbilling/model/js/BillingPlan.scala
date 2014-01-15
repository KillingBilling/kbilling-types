package kbilling.model.js

import java.util.{Map => JMap}

trait BillingPlan {
  def getServiceAccounts: JMap[String, ServiceAccount]
  def getPaymentAccounts: JMap[String, PaymentAccount]
  def getNotifications: JMap[String, Notification]
}

trait Account {
  def getAggregates: JMap[String, Aggregate]
}

trait ServiceAccount extends Account

trait PaymentAccount extends Account {
  def cost(vars: JMap[String, String]): String
}

trait Aggregate {
  def aggr(a: String, b: String): String
  def init(x: String): String // x can be null - there was no value prior to this billing cycle
}

trait Notification {
  def predicate(vars: JMap[String, String]): Boolean
}
