package com.guys.coding.hackathon.backend.domain.product

import com.guys.coding.hackathon.backend.infrastructure.slick.product.ProductSchema.ProviderProduct
import com.guys.coding.hackathon.backend.domain.section.SectionRepository
import cats.Monad
import cats.data.EitherT
import com.guys.coding.hackathon.backend.domain.CategoryId
import com.guys.coding.hackathon.backend.domain.ChatbotId

object ProductService {

  def upsert[F[_]: Monad: ProductRepository: SectionRepository](
      chatbotId: ChatbotId,
      value: ProviderProduct
  ): F[Either[UpsertProductError, ProviderProduct]] = {

    def liftF[T](t: F[T]) = EitherT.liftF[F, UpsertProductError, T](t)

    val eitherResult = for {
      _ <- EitherT.cond[F](value.categoryIds.forall(CategoryId.mockedCategories.contains), (), UpsertProductError.CateogryNotFound.widen)

      section <- EitherT.fromOptionF(SectionRepository[F].get(value.sectionId), ifNone = UpsertProductError.SectionNotFound.widen)
      _       <- EitherT.cond[F](chatbotId == section.chatbotId, (), UpsertProductError.NoAccessToSection.widen)
      _       <- EitherT.cond[F](!section.archived, (), UpsertProductError.SectionArchived.widen)

      _   <- EitherT.cond[F](value.price < 0 || value.salePrice.exists(_ < 0), (), UpsertProductError.SectionArchived.widen)
      res <- liftF(ProductRepository[F].upsert(value))
    } yield res

    eitherResult.value
  }

  sealed trait UpsertProductError {
    def widen: UpsertProductError = this
  }

  object UpsertProductError {
    case object CateogryNotFound  extends UpsertProductError
    case object SectionArchived   extends UpsertProductError
    case object InvalidPrice      extends UpsertProductError
    case object NoAccessToSection extends UpsertProductError
    case object SectionNotFound   extends UpsertProductError
  }

}
