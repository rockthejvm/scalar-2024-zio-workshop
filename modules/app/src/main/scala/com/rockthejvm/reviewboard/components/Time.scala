package com.rockthejvm.reviewboard.components

import scala.scalajs.*
import scala.scalajs.js.* // js.native
import scala.scalajs.js.annotation.*

@js.native
@JSGlobal
class Moment extends js.Object {
  def format(): String  = js.native
  def fromNow(): String = js.native
}

// m = moment.unix(52367562) => Moment object
// m.format('....') => "...."
@js.native
@JSImport("moment", JSImport.Default)
object MomentLib extends js.Object {
  def unix(millis: Long): Moment = js.native
}

// API that I will use in the app
object Time {
  def unix2hr(millis: Long) =
    MomentLib.unix(millis / 1000).fromNow()
  def past(millis: Long) =
    new Date().getTime.toLong - millis
}
