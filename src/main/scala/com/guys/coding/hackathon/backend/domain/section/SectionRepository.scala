package com.guys.coding.hackathon.backend.domain.section

import hero.common.query.PaginatedResponse
import com.guys.coding.hackathon.backend.domain._
import com.guys.coding.hackathon.backend.infrastructure.slick.section.SectionSchema.ProviderSection
import simulacrum.typeclass

@typeclass
trait SectionRepository[F[_]] {

  def upsert(value: ProviderSection): F[ProviderSection]

  def query(page: Int, entriesPerPage: Int, chatbotId: ChatbotId): F[PaginatedResponse[ProviderSection]]

  def get(id: SectionId): F[Option[ProviderSection]]

}
