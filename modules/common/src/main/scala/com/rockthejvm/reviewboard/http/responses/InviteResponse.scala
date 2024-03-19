package com.rockthejvm.reviewboard.http.responses

import zio.json.JsonCodec

final case class InviteResponse(
    status: String,
    nInvites: Int
) derives JsonCodec
