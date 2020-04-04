package com.guys.coding.hackathon.backend.infrastructure.slick.section

import cats.effect.IO
import com.guys.coding.hackathon.backend.infrastructure.slick.section.SectionSchema.{sections, ProviderSection, Sections}
import com.guys.coding.hackathon.backend.infrastructure.slick.repo
import repo.CatsIntegration
import repo.profile.api._
import hero.common.query.PaginatedResponse
import scala.concurrent.ExecutionContext
import cats.effect.{ContextShift, IO}
import com.guys.coding.hackathon.backend.domain.SectionId
import com.guys.coding.hackathon.backend.infrastructure.slick.CommonJdbcTypes._
import com.guys.coding.hackathon.backend.domain.section.SectionRepository
import com.guys.coding.hackathon.backend.domain.ChatbotId

class SlickSectionRepository()(implicit db: Database, ec: ExecutionContext, cs: ContextShift[IO])
    extends repo.plain.CrudRepo[SectionId, ProviderSection, Sections](db, sections)
    with CatsIntegration
    with SectionRepository[IO] {
  import SectionSchema._

  override protected def DTOtoDomain = identity

  override protected def toInsertedDTO = identity

  override protected def id = _.id

  import SectionSchema._

  def upsert(value: SectionSchema.ProviderSection): IO[ProviderSection] =
    runIO(for {
      _ <- upsertAction(value)
    } yield value)

  def query(page: Int, entriesPerPage: Int, chatbotId: ChatbotId): IO[PaginatedResponse[ProviderSection]] =
    runIO(getEntriesPageAction(page, entriesPerPage, row => !row.archived && row.chatbotId === chatbotId))

  def get(id: SectionId) = runIO(getFirstEntityByMatcherAction(_.id === id))

}
