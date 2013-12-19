package kbilling.types

case class Aggregate(aggr: (BigDecimal, BigDecimal) => BigDecimal, init: Option[BigDecimal] => BigDecimal)