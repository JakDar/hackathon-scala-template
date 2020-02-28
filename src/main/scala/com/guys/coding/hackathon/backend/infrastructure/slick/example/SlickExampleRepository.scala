package com.guys.coding.hackathon.backend.infrastructure.slick.example

import cats.effect.IO
import com.guys.coding.hackathon.backend.infrastructure.slick.example.ExampleSchema.{examples, ExampleDTO, Examples}
import com.guys.coding.hackathon.backend.infrastructure.slick.repo
import repo.CatsIntegration
import repo.profile.api._
import hero.common.query.PaginatedResponse
import scala.concurrent.ExecutionContext
import cats.effect.{ContextShift, IO}

class SlickExampleRepository()(implicit db: Database, ec: ExecutionContext, cs: ContextShift[IO])
    extends repo.plain.CrudRepo[String, ExampleDTO, Examples](db, examples)
    with CatsIntegration {

  override protected def DTOtoDomain = identity

  override protected def toInsertedDTO = identity

  override protected def id = _.id

  import ExampleSchema._

  def insert(id: String, value: String): IO[ExampleDTO] =
    runIO(for {
      _      <- insertAction(ExampleDTO(id, value, archived = false))
      result <- getFirstEntityByMatcherAction(_.id === id)
    } yield result.get)

  def query(page: Int, entriesPerPage: Int): IO[PaginatedResponse[ExampleDTO]] =
    runIO(getEntriesPageAction(page, entriesPerPage, row => !row.archived))

  def archive(id: String): IO[Int] =
    runIO(updateFieldAction(_.id === id, _.archived, true))

}
