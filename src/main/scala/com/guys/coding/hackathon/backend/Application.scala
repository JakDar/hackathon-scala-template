package com.guys.coding.hackathon.backend

import cats.effect.Timer
import cats.effect.{ContextShift, IO}
import hero.common.logging.Logger
import hero.common.logging.slf4j.LoggingConfigurator
import com.guys.coding.hackathon.backend.infrastructure.slick.example.ExampleSchema
import com.guys.coding.hackathon.backend.infrastructure.slick.repo

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

class Application(config: ConfigValues)(
    implicit ec: ExecutionContext,
    appLogger: Logger[IO],
    cs: ContextShift[IO]
) extends LoggingExt {

  LoggingConfigurator.setRootLogLevel(config.app.rootLogLevel)
  LoggingConfigurator.setLogLevel("com.guys.coding.hackathon.backend", config.app.appLogLevel)

  implicit private val db: repo.profile.api.Database =
    repo.profile.api.Database.forConfig("slick.db", config.raw)

  private val schemas = List(
    ExampleSchema
  )

  schemas.foreach(schema => repo.SchemaUtils.createSchemasIfNotExists(db, schema.schemas))

  private val privateKey      = PrivateKeyReader.get(config.authKeys.privatePath)
  private val publicKey       = PublicKeyReader.get(config.authKeys.publicPath)
  private val jwtTokenService = new JwtTokenService(publicKey, privateKey)
  private val services        = Services(new ExampleService[IO] {}, jwtTokenService)
  val graphqlRoute            = new GraphqlRoute(services)

  private val routes = Router(
    "/graphql" -> graphqlRoute.route
  ).orNotFound

  def start()(implicit t: Timer[IO]): IO[Unit] = {

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
