package com.guys.coding.hackathon.backend.api.graphql.schema.example

import sangria.macros.derive._
import sangria.schema._

object ExampleTypes {

  case class ExampleInput(id: String, value: String)
  implicit val ExampleInputType: InputType[ExampleInput] =
    deriveInputObjectType[ExampleInput]()

}
