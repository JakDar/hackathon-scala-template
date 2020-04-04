package com.guys.coding.hackathon.backend.domain

case class ChatbotId(value: String)  extends AnyVal
case class SectionId(value: String)  extends AnyVal
case class ProductId(value: String)  extends AnyVal
case class CategoryId(value: String) extends AnyVal

object CategoryId {
  val mockedCategories = 1 to 10 map (n => CategoryId(s"category$n"))
}
