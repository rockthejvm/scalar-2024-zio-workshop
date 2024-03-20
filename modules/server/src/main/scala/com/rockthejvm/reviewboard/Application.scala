package com.rockthejvm.reviewboard

import zio.*
import sttp.tapir.server.ziohttp.*
import zio.http.Server

import com.rockthejvm.reviewboard.http.controllers.*
import com.rockthejvm.reviewboard.services.*
import com.rockthejvm.reviewboard.repositories.*

object Application extends ZIOAppDefault {

  def startServer = for {
    controller <- CompanyController.makeZIO
    _ <- Server.serve(
      ZioHttpInterpreter(
        ZioHttpServerOptions.default // TODO add CORS
      ).toHttp(controller.routes)
    )
  } yield ()

  def program =
    for {
      _ <- ZIO.log("Rock the JVM! Bootstrapping...")
      _ <- startServer
    } yield ()

  override def run = program.provide(
    // infra
    Server.default,
    Repository.dataLayer,
    // services
    CompanyServiceLive.layer,
    // repositories
    CompanyRepositoryLive.layer
  )
}
