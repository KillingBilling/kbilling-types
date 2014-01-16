package kbilling.model.j

import java.math.{BigDecimal => JBigDecimal}
import java.util.{Map => JMap}
import kbilling.model

object BillingPlan {

  implicit def j2s(jbp: BillingPlan): model.BillingPlan = new model.BillingPlan {
    val accounts: Map[String, model.Account] = ???
    val notifications: Map[String, model.Notification] = ???
  }

}

trait BillingPlan {
  def accounts: JMap[String, Account]
  def notifications: JMap[String, Notification]
}

trait Account {
  def aggregates: JMap[String, Aggregate]
}

trait ServiceAccount extends Account

trait PaymentAccount extends Account {
  def cost(vars: JMap[String, JBigDecimal]): JBigDecimal
}

trait Aggregate {
  def aggr(a: JBigDecimal, b: JBigDecimal): JBigDecimal
  def init(x: JBigDecimal): JBigDecimal // x can be null - there was no value prior to this billing cycle
}

trait Notification {
  def predicate(vars: JMap[String, BigDecimal]): Boolean
}
