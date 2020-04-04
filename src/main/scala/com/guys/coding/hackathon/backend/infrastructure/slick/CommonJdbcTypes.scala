package com.guys.coding.hackathon.backend.infrastructure.slick

import com.guys.coding.hackathon.backend.infrastructure.slick.repo.profile.api._

import slick.jdbc.JdbcType

import io.circe.generic.semiauto._
import com.guys.coding.hackathon.backend.domain._

object CommonJdbcTypes {

  implicit val ChatbotIdMap: JdbcType[ChatbotId]   = MappedColumnType.base[ChatbotId, String](_.value, ChatbotId)
  implicit val ProductIdMap: JdbcType[ProductId]   = MappedColumnType.base[ProductId, String](_.value, ProductId)
  implicit val SectionIdMap: JdbcType[SectionId]   = MappedColumnType.base[SectionId, String](_.value, SectionId)
  implicit val CategoryIdMap: JdbcType[CategoryId] = MappedColumnType.base[CategoryId, String](_.value, CategoryId.apply)

  implicit val categoryDecoder = deriveDecoder[CategoryId]
  implicit val categoryEncoder = deriveEncoder[CategoryId]

}
