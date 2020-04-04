package com.guys.coding.hackathon.backend.api

import com.chatbotize.ecommerce.provider.manual.api.ManualEcommerceProviderGrpc
import com.chatbotize.ecommerce.provider.manual.api.{UpdateSectionRequest, UpdateSectionResponse}
import scala.concurrent.Future
import com.chatbotize.ecommerce.provider.manual.api.{GetProductRequest, GetProductResponse}
import scala.concurrent.Future
import com.chatbotize.ecommerce.provider.manual.api.{UpdateProductRequest, UpdateProductResponse}
import scala.concurrent.Future
import com.chatbotize.ecommerce.provider.manual.api.{CreateProductRequest, CreateProductResponse}
import scala.concurrent.Future
import com.chatbotize.ecommerce.provider.manual.api.{GetSectionRequest, GetSectionResponse}
import scala.concurrent.Future
import com.chatbotize.ecommerce.provider.manual.api.{QueryProductRequest, QueryProductResponse}
import scala.concurrent.Future
import com.chatbotize.ecommerce.provider.manual.api.{QuerySectionRequest, QuerySectionResponse}
import scala.concurrent.Future
import com.chatbotize.ecommerce.provider.manual.api.{CreateSectionRequest, CreateSectionResponse}
import scala.concurrent.Future
import com.guys.coding.hackathon.backend.domain.product.ProductRepository
import com.guys.coding.hackathon.backend.domain.section.SectionRepository
import scala.concurrent.ExecutionContext
import cats.effect.IO
import hero.common.logging.Logger
import hero.common.util.LoggingExt
import cats.Id
import hero.common.util.IdProvider
import com.guys.coding.hackathon.backend.domain.SectionId
import com.guys.coding.hackathon.backend.domain.ChatbotId
import com.guys.coding.hackathon.backend.domain.product.ProductService
import com.guys.coding.hackathon.backend.domain.ProductId

class DomainApiEndpoint()(
    implicit ec: ExecutionContext,
    productRepo: ProductRepository[IO],
    sectionRepo: SectionRepository[IO],
    logger: Logger[IO],
    idProvider: IdProvider[Id]
) extends ManualEcommerceProviderGrpc.ManualEcommerceProvider
    with LoggingExt
    with ToProto
    with ToDomain {

  override def createSection(request: CreateSectionRequest): Future[CreateSectionResponse] =
    sectionRepo
      .upsert(createToSection(SectionId(idProvider.newId()), request))
      .map(sec => CreateSectionResponse(CreateSectionResponse.Result.Section(sectiontoProto(sec))))
      .logFailure(request)
      .unsafeToFuture()

  override def updateSection(request: UpdateSectionRequest): Future[UpdateSectionResponse] =
    sectionRepo
      .upsert(updateToSection(request))
      .map(sec => UpdateSectionResponse(UpdateSectionResponse.Result.Section(sectiontoProto(sec))))
      .logFailure(request)
      .unsafeToFuture()

  override def getSection(request: GetSectionRequest): Future[GetSectionResponse] =
    sectionRepo
      .get(SectionId(request.id))
      .map(sec => GetSectionResponse(sec.map(sectiontoProto)))
      .logFailure(request)
      .unsafeToFuture()

  override def querySection(request: QuerySectionRequest): Future[QuerySectionResponse] =
    sectionRepo
      .query(page = request.page, entriesPerPage = request.entriesPerPage, chatbotId = ChatbotId(request.chatbotId))
      .map(pag => QuerySectionResponse(pag.entities.map(sectiontoProto), pag.pageCount))
      .logFailure(request)
      .unsafeToFuture()

  override def createProduct(request: CreateProductRequest): Future[CreateProductResponse] = {
    import CreateProductResponse._

    ProductService
      .upsert[IO](ChatbotId(request.chatbotId), createToProduct(ProductId(idProvider.newId()), request))
      .map {
        case Right(value) => Result.Product(productToProto(value))
        case Left(error)  => Result.Error(ErrorResponse(Seq(createErrorToProto(error))))
      }
      .map(CreateProductResponse(_))
      .logFailure(request)
      .unsafeToFuture()
  }

  override def updateProduct(request: UpdateProductRequest): Future[UpdateProductResponse] = {
    import UpdateProductResponse._

    ProductService
      .upsert[IO](ChatbotId(request.chatbotId), updateToProduct(request))
      .map {
        case Right(value) => Result.Product(productToProto(value))
        case Left(error)  => Result.Error(ErrorResponse(Seq(updateErrorToProto(error))))
      }
      .map(UpdateProductResponse(_))
      .logFailure(request)
      .unsafeToFuture()
  }

  override def getProduct(request: GetProductRequest): Future[GetProductResponse] =
    productRepo
      .get(ProductId(request.id))
      .map(sec => GetProductResponse(sec.map(productToProto)))
      .logFailure(request)
      .unsafeToFuture()

  override def queryProduct(request: QueryProductRequest): Future[QueryProductResponse] =
    productRepo
      .query(page = request.page, entriesPerPage = request.entriesPerPage, sectionId = SectionId(request.sectionId))
      .map(pag => QueryProductResponse(pag.entities.map(productToProto), pag.pageCount))
      .logFailure(request)
      .unsafeToFuture()

}
