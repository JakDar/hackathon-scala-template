package com.guys.coding.hackathon.backend.infrastructure.slick.product

import com.guys.coding.hackathon.backend.infrastructure.slick.repo.SlickSchemas
import com.guys.coding.hackathon.backend.infrastructure.slick.repo.profile.api._
import slick.lifted.{TableQuery, Tag}
import com.guys.coding.hackathon.backend.domain._

object ProductSchema extends SlickSchemas {
  import com.guys.coding.hackathon.backend.infrastructure.slick.CommonJdbcTypes._

  case class ProviderProduct(
      id: ProductId,
      sectionId: SectionId,
      name: String,
      description: String,
      mainPhotoId: String,
      price: Long,
      salePrice: Option[Long],
      currency: String,
      categoryIds: List[CategoryId],
      archived: Boolean
  )

  override def schemas = List(products)

  val products = TableQuery[Products]

  class Products(tag: Tag) extends Table[ProviderProduct](tag, "provider_products") {

    def id          = column[ProductId]("id", O.PrimaryKey)
    def sectionId   = column[SectionId]("sectionId")
    def name        = column[String]("name")
    def description = column[String]("description")
    def mainPhotoId = column[String]("mainPhotoId")
    def price       = column[Long]("price")
    def salePrice   = column[Option[Long]]("salePrice")
    def currency    = column[String]("currency")
    def categoryId  = column[List[CategoryId]]("categoryId")
    def archived    = column[Boolean]("archived")

    override def * =
      (
        id,
        sectionId,
        name,
        description,
        mainPhotoId,
        price,
        salePrice,
        currency,
        categoryId,
        archived
      ) <> (ProviderProduct.tupled, ProviderProduct.unapply)
  }

}
