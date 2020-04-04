package com.guys.coding.hackathon.backend.infrastructure.slick.product

import cats.effect.IO
import com.guys.coding.hackathon.backend.infrastructure.slick.product.ProductSchema.{products, Products, ProviderProduct}
import com.guys.coding.hackathon.backend.infrastructure.slick.repo
import repo.CatsIntegration
import repo.profile.api._
import hero.common.query.PaginatedResponse
import scala.concurrent.ExecutionContext
import cats.effect.{ContextShift, IO}
import com.guys.coding.hackathon.backend.domain.ProductId
import com.guys.coding.hackathon.backend.infrastructure.slick.CommonJdbcTypes._
import com.guys.coding.hackathon.backend.domain.SectionId
import com.guys.coding.hackathon.backend.domain.product.ProductRepository

class SlickProductRepository()(implicit db: Database, ec: ExecutionContext, cs: ContextShift[IO])
    extends repo.plain.CrudRepo[ProductId, ProviderProduct, Products](db, products)
    with CatsIntegration
    with ProductRepository[IO] {
  import ProductSchema._

  override protected def DTOtoDomain = identity

  override protected def toInsertedDTO = identity

  override protected def id = _.id

  import ProductSchema._

  // HACK: we can easily unarchive, we don't check if section no control over this here
  override def upsert(value: ProductSchema.ProviderProduct): IO[ProviderProduct] =
    runIO(for {
      _ <- upsertAction(value)
    } yield value)

  override def query(page: Int, entriesPerPage: Int, sectionId: SectionId): IO[PaginatedResponse[ProviderProduct]] =
    runIO(getEntriesPageAction(page, entriesPerPage, row => !row.archived && row.sectionId === sectionId))

  override def get(id: ProductId): IO[Option[ProductSchema.ProviderProduct]] = runIO(getFirstEntityByMatcherAction(_.id === id))

}
