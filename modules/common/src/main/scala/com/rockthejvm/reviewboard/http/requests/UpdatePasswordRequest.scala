package com.rockthejvm.reviewboard.http.requests

import zio.json.JsonCodec

final case class UpdatePasswordRequest(
    email: String,
    oldPassword: String,
    newPassword: String
) derives JsonCodec
