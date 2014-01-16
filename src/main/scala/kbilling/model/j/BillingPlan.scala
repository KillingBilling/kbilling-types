package kbilling.model.j

import java.math.{BigDecimal => JBigDecimal}
import java.util.{Map => JMap}
import kbilling.model
import kbilling.model.BillingPlan.Vars
import scala.collection.JavaConversions._

object BillingPlan {

  implicit val dec2j: BigDecimal => JBigDecimal = _.underlying()
  implicit val vars2j: Vars => JMap[String, JBigDecimal] = _ mapValues dec2j

  def j2Aggregate(a: Aggregate): model.Aggregate = model.Aggregate(
    aggr = (z, x) => a.aggr(z, x),
    init = xo => a.init(xo getOrElse null)
  )

  val j2Account: Account => model.Account = {

    case a: ServiceAccount => model.ServiceAccount(a.aggregates.toMap mapValues j2Aggregate)

    case a: PaymentAccount => model.PaymentAccount(
      cost = vars => a.cost(vars),
      aggregates = a.aggregates.toMap mapValues j2Aggregate
    )

  }

  def j2Notification(n: Notification): model.Notification = model.Notification(vars => n.predicate(vars))

  implicit def j2BillingPlan(bp: BillingPlan): model.BillingPlan = new model.BillingPlan {

    val accounts: Map[String, model.Account] = bp.accounts.toMap mapValues j2Account

    val notifications: Map[String, model.Notification] = bp.notifications.toMap mapValues j2Notification

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
  def aggr(z: JBigDecimal, x: JBigDecimal): JBigDecimal
  def init(x: JBigDecimal): JBigDecimal // x can be null - there was no value prior to this billing cycle
}

trait Notification {
  def predicate(vars: JMap[String, BigDecimal]): Boolean
}
