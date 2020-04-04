package com.guys.coding.hackathon.backend.api

import com.guys.coding.hackathon.backend.infrastructure.slick.section.SectionSchema.ProviderSection
import com.chatbotize.ecommerce.provider.manual.common.{ProviderSection => ProtoProviderSection, ProviderProduct => ProtoProviderProduct}
import com.guys.coding.hackathon.backend.infrastructure.slick.product.ProductSchema.ProviderProduct
import com.guys.coding.hackathon.backend.domain.product.ProductService.UpsertProductError
import com.chatbotize.ecommerce.provider.manual.api.{CreateProductResponse, UpdateProductResponse}
import com.guys.coding.hackathon.backend.domain.product.ProductService.UpsertProductError._

trait ToProto {

  def sectiontoProto(c: ProviderSection) = ProtoProviderSection(
    id = c.id.value,
    chatbotId = c.chatbotId.value,
    name = c.name,
    logoFileId = c.logoFileId,
    address = c.address,
    latitude = c.latitude,
    lognitude = c.lognitude,
    archived = c.archived
  )

  def productToProto(c: ProviderProduct) = ProtoProviderProduct(
    id = c.id.value,
    sectionId = c.sectionId.value,
    name = c.name,
    description = c.description,
    mainPhotoId = c.mainPhotoId,
    price = c.price,
    salePrice = c.salePrice,
    currency = c.currency,
    categoryIds = c.categoryIds.map(_.value),
    archived = false
  )

  def updateErrorToProto(e: UpsertProductError): UpdateProductResponse.Error = {
    import UpdateProductResponse.Error._
    e match {
      case CateogryNotFound  => CATEOGRY_NOT_FOUND
      case SectionArchived   => SECTION_ARCHIVED
      case InvalidPrice      => INVALID_PRICE
      case NoAccessToSection => NO_ACCESS_TO_SECTION
      case SectionNotFound   => SECTION_NOT_FOUND
    }
  }

  def createErrorToProto(e: UpsertProductError): CreateProductResponse.Error = {
    import CreateProductResponse.Error._
    e match {
      case CateogryNotFound  => CATEOGRY_NOT_FOUND
      case SectionArchived   => SECTION_ARCHIVED
      case InvalidPrice      => INVALID_PRICE
      case NoAccessToSection => NO_ACCESS_TO_SECTION
      case SectionNotFound   => SECTION_NOT_FOUND
    }
  }

}
