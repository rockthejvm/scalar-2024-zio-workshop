package com.rockthejvm.reviewboard.http.requests

import zio.json.JsonCodec

final case class ForgotPasswordRequest(email: String) derives JsonCodec
