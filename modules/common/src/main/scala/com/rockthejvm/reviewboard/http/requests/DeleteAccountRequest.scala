package com.rockthejvm.reviewboard.http.requests

import zio.json.JsonCodec

final case class DeleteAccountRequest(
    email: String,
    password: String
) derives JsonCodec
