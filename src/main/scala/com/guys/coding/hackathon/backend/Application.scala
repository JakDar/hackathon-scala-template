package com.guys.coding.hackathon.backend

import cats.effect.{ContextShift, IO}
import hero.common.logging.Logger
import hero.common.logging.slf4j.LoggingConfigurator
import com.guys.coding.hackathon.backend.infrastructure.slick.section.SectionSchema
import com.guys.coding.hackathon.backend.infrastructure.slick.repo

import scala.concurrent.ExecutionContext
import hero.common.util.LoggingExt
import com.guys.coding.hackathon.backend.infrastructure.slick.product.ProductSchema
import com.guys.coding.hackathon.backend.infrastructure.slick.product.SlickProductRepository
import com.guys.coding.hackathon.backend.infrastructure.slick.section.SlickSectionRepository
import hero.common.util.IdProvider
import com.guys.coding.hackathon.backend.api.DomainApiEndpoint
import com.guys.coding.hackathon.backend.api.DomainApiServer

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
    SectionSchema,
    ProductSchema
  )

  schemas.foreach(schema => repo.SchemaUtils.createSchemasIfNotExists(db, schema.schemas))

  implicit val productRepository = new SlickProductRepository
  implicit val sectionRepository = new SlickSectionRepository
  implicit val idProvider        = IdProvider.id

  val e      = new DomainApiEndpoint()
  val server = new DomainApiServer(e, 8080)

  def start(): IO[Unit] =
    IO(server.initialize())

}
