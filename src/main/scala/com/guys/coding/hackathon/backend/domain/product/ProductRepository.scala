package com.guys.coding.hackathon.backend.domain.product

import com.guys.coding.hackathon.backend.domain.ProductId
import hero.common.query.PaginatedResponse
import com.guys.coding.hackathon.backend.domain.SectionId
import com.guys.coding.hackathon.backend.infrastructure.slick.product.ProductSchema.ProviderProduct
import simulacrum.typeclass

@typeclass
trait ProductRepository[F[_]] {

  def upsert(value: ProviderProduct): F[ProviderProduct]

  def query(page: Int, entriesPerPage: Int, sectionId: SectionId): F[PaginatedResponse[ProviderProduct]]

  def get(id: ProductId): F[Option[ProviderProduct]]

}
