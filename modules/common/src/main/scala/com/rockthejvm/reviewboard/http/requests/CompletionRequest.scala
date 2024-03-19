package com.rockthejvm.reviewboard.http.requests

import zio.json.JsonCodec

case class CompletionMessage(
    content: String,
    role: String = "user"
) derives JsonCodec

final case class CompletionRequest(
    messages: List[CompletionMessage],
    model: String = "gpt-4"
) derives JsonCodec

object CompletionRequest {
  def single(prompt: String) = CompletionRequest(List(CompletionMessage(prompt)))
}
