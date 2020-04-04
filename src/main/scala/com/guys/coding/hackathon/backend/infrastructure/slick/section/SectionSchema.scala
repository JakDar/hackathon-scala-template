package com.guys.coding.hackathon.backend.infrastructure.slick.section

import com.guys.coding.hackathon.backend.infrastructure.slick.repo.SlickSchemas
import com.guys.coding.hackathon.backend.infrastructure.slick.repo.profile.api._
import slick.lifted.{TableQuery, Tag}
import com.guys.coding.hackathon.backend.domain.SectionId
import com.guys.coding.hackathon.backend.domain.ChatbotId

object SectionSchema extends SlickSchemas {
  import com.guys.coding.hackathon.backend.infrastructure.slick.CommonJdbcTypes._

  case class ProviderSection(
      id: SectionId,
      chatbotId: ChatbotId,
      name: String,
      logoFileId: String,
      address: String,
      latitude: Double,
      lognitude: Double,
      archived: Boolean
  )

  override def schemas = List(sections)

  val sections = TableQuery[Sections]

  class Sections(tag: Tag) extends Table[ProviderSection](tag, "provider_sections") {

    def id         = column[SectionId]("id", O.PrimaryKey)
    def chatbotId  = column[ChatbotId]("chatbotId")
    def name       = column[String]("name")
    def logoFileId = column[String]("logoFileId")
    def address    = column[String]("address")
    def latitude   = column[Double]("latitude")
    def lognitude  = column[Double]("lognitude")
    def archived   = column[Boolean]("archived")

    override def * =
      (
        id,
        chatbotId,
        name,
        logoFileId,
        address,
        latitude,
        lognitude,
        archived
      ) <> (ProviderSection.tupled, ProviderSection.unapply)
  }

}
