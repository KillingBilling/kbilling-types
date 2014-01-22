package kbilling.types

package object notifications {

  val belowZero: BigDecimal => Boolean = {_ < 0}

}
