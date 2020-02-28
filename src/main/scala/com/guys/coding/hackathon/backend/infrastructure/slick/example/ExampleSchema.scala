package com.guys.coding.hackathon.backend.infrastructure.slick.example

import com.guys.coding.hackathon.backend.infrastructure.slick.repo.SlickSchemas
import com.guys.coding.hackathon.backend.infrastructure.slick.repo.profile.api._
import slick.lifted.{TableQuery, Tag}

object ExampleSchema extends SlickSchemas {
  case class ExampleDTO(
      id: String,
      value: String,
      archived: Boolean
  )

  override def schemas = List(examples)

  val examples = TableQuery[Examples]

  class Examples(tag: Tag) extends Table[ExampleDTO](tag, "example") {

    def id       = column[String]("id", O.PrimaryKey)
    def value    = column[String]("value")
    def archived = column[Boolean]("archived")

    override def * =
      (
        id,
        value,
        archived
      ) <> (ExampleDTO.tupled, ExampleDTO.unapply)
  }

}
