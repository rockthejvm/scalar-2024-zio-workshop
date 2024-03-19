package com.rockthejvm.reviewboard.domain.errors

import sttp.model.StatusCode

final case class HttpError(
    statusCode: StatusCode,
    message: String,
    cause: Throwable
) extends RuntimeException(message, cause)

object HttpError {
  def decode(tuple: (StatusCode, String)) =
    HttpError(tuple._1, tuple._2, new RuntimeException(tuple._2))

  def encode(error: Throwable) = error match {
    case UnauthorizedException(message) => (StatusCode.Unauthorized, message)
    // can surface out diffferent statused depending on the exception thrown
    case _ => (StatusCode.InternalServerError, error.getMessage)
  }

}
