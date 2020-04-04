package com.guys.coding.hackathon.backend.api

import com.guys.coding.hackathon.backend.domain.SectionId
import com.chatbotize.ecommerce.provider.manual.api.CreateSectionRequest
import com.guys.coding.hackathon.backend.infrastructure.slick.section.SectionSchema.ProviderSection
import com.guys.coding.hackathon.backend.domain.ChatbotId
import com.guys.coding.hackathon.backend.domain.ProductId
import com.chatbotize.ecommerce.provider.manual.api.{CreateProductRequest, UpdateProductRequest, UpdateSectionRequest}
import com.guys.coding.hackathon.backend.infrastructure.slick.product.ProductSchema.ProviderProduct
import com.guys.coding.hackathon.backend.domain.CategoryId

trait ToDomain {

  def createToSection(id: SectionId, c: CreateSectionRequest) = ProviderSection(
    id = id,
    chatbotId = ChatbotId(c.chatbotId),
    name = c.name,
    logoFileId = c.logoFileId,
    address = c.address,
    latitude = c.latitude,
    lognitude = c.lognitude,
    archived = false
  )

  def updateToSection(c: UpdateSectionRequest) = ProviderSection(
    id = SectionId(c.id),
    chatbotId = ChatbotId(c.chatbotId),
    name = c.name,
    logoFileId = c.logoFileId,
    address = c.address,
    latitude = c.latitude,
    lognitude = c.lognitude,
    archived = false
  )

  def createToProduct(id: ProductId, c: CreateProductRequest) = ProviderProduct(
    id = id,
    sectionId = SectionId(c.sectionId),
    name = c.name,
    description = c.description,
    mainPhotoId = c.mainPhotoId,
    price = c.price,
    salePrice = c.salePrice,
    currency = c.currency,
    categoryIds = c.categoryIds.map(CategoryId.apply).toList,
    archived = false
  )

  def updateToProduct(c: UpdateProductRequest) = ProviderProduct(
    id = ProductId(c.id),
    sectionId = SectionId(c.sectionId),
    name = c.name,
    description = c.description,
    mainPhotoId = c.mainPhotoId,
    price = c.price,
    salePrice = c.salePrice,
    currency = c.currency,
    categoryIds = c.categoryIds.map(CategoryId.apply).toList,
    archived = false
  )

}
