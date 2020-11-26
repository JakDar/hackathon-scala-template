package com.guys.coding.hackathon.backend

import cats.effect.Timer
import cats.effect.{ContextShift, IO}
import hero.common.logging.Logger
import hero.common.logging.slf4j.LoggingConfigurator

import com.guys.coding.hackathon.backend.api.graphql.core.GraphqlRoute
import com.guys.coding.hackathon.backend.domain.ExampleService
import com.guys.coding.hackathon.backend.infrastructure.jwt.JwtTokenService
import hero.common.crypto.KeyReaders.{PrivateKeyReader, PublicKeyReader}
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import org.http4s.syntax.kleisli._

import scala.concurrent.ExecutionContext
import hero.common.util.LoggingExt
import com.guys.coding.hackathon.backend.infrastructure.postgres.Database

class Application(config: ConfigValues)(
    implicit ec: ExecutionContext,
    appLogger: Logger[IO],
    cs: ContextShift[IO]
) extends LoggingExt {

  LoggingConfigurator.setRootLogLevel(config.app.rootLogLevel)
  LoggingConfigurator.setLogLevel("com.guys.coding.hackathon.backend", config.app.appLogLevel)

  private val privateKey      = PrivateKeyReader.get(config.authKeys.privatePath)
  private val publicKey       = PublicKeyReader.get(config.authKeys.publicPath)
  private val jwtTokenService = new JwtTokenService(publicKey, privateKey)

  def start()(implicit t: Timer[IO]): IO[Unit] = Database.transactor(config.postgres).use { tx =>
    val services = Services(new ExampleService[IO] {}, jwtTokenService, tx)
    val routes = Router(
      "/graphql" -> new GraphqlRoute(services).route
    ).orNotFound

    for {
      _ <- appLogger.info(s"Started server at ${config.app.bindHost}:${config.app.bindPort}")
      _ <- BlazeServerBuilder[IO]
            .bindHttp(config.app.bindPort, config.app.bindHost)
            .withHttpApp(CORS(routes))
            .serve
            .compile
            .drain
    } yield ()

  }

}
