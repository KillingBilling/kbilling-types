package kbilling

package object types {

  case class Aggregate(aggr: (BigDecimal, BigDecimal) => BigDecimal, init: Option[BigDecimal] => BigDecimal)

}
