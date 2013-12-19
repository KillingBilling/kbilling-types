package kbilling.types

package object aggregates {

  val sum = Aggregate(_ + _, _ => 0)

}
