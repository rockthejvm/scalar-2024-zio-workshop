package com.rockthejvm.reviewboard.services

import zio.*
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import com.stripe.Stripe
import com.stripe.net.Webhook

import scala.jdk.OptionConverters.*

trait PaymentService {
  def createCheckoutSession(id: Long): Task[Option[Session]]
  def handleWebhookEvent[A](
      signature: String,
      payload: String,
      action: String => Task[A]
  ): Task[Option[A]]
}

class PaymentServiceLive private extends PaymentService {
  Stripe.apiKey = "YOUR API KEY"
  val whSecret = "YOUR SECRET"
  val price    = "YOUR PRODUCT PRICE"

  def createCheckoutSession(id: Long): Task[Option[Session]] =
    ZIO
      .attempt(
        SessionCreateParams
          .builder()
          .setMode(SessionCreateParams.Mode.PAYMENT)
          .setSuccessUrl("http://localhost:1234") // TODO set this in config
          .setCancelUrl("http://localhost:1234")
          .setClientReferenceId(id.toString) // setting the company I will activate later
          .addLineItem(
            SessionCreateParams.LineItem
              .builder()
              .setPrice(price)
              .setQuantity(1L)
              .build()
          )
          .build()
      )
      .map(params => Session.create(params))
      .map(Option(_))
      .logError("Stripe checkout session creation FAILED")

  def handleWebhookEvent[A](
      signature: String,
      payload: String,
      action: String => Task[A]
  ): Task[Option[A]] =
    ZIO
      .attempt(Webhook.constructEvent(payload, signature, whSecret))
      .logError("Webhook event failed")
      .flatMap { event =>
        event.getType() match {
          case "checkout.session.completed" =>
            ZIO.foreach( // "traverse"
              event
                .getDataObjectDeserializer()
                .getObject()
                .toScala
                .map(_.asInstanceOf[Session])
                .map(_.getClientReferenceId())
            )(action)
          case _ => ZIO.none
        }
      }
}

object PaymentServiceLive {
  val layer = ZLayer.succeed(new PaymentServiceLive)
}
