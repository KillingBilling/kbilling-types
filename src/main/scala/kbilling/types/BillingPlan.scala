package kbilling.types

import annotation.StaticAnnotation
import language.experimental.macros

class BillingPlan extends StaticAnnotation {

  def macroTransform(annottees: Any*) = macro ???

}
