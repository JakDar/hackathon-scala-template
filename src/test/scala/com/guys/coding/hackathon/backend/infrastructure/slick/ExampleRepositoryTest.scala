package com.guys.coding.hackathon.backend.infrastructure.slick

import org.scalatest._
import com.guys.coding.hackathon.backend.infrastructure.slick.example.SlickExampleRepository
import org.scalatest._
import com.guys.coding.hackathon.backend.infrastructure.slick.example.ExampleSchema.{examples, ExampleDTO}

class ExampleRepositoryTest extends PostgresSpec with Matchers {
  def repository(implicit db: Database) = new SlickExampleRepository()

  "exampleRepo" should "insert" in {

    repository.insert("ala", "makota").unsafeRunSync()

    val res = db.runSync(examples.result)
    res.size shouldBe 1
    res.head shouldBe ExampleDTO("ala", "makota", archived = false)

  }

  it should "query not archived" in {
    val dto1 = ExampleDTO("alice", "with bob", archived = false)
    val dto2 = ExampleDTO("eve", "with john", archived = true)

    db.runSync(examples ++= List(dto1, dto2))

    val res = repository.query(1, 10).unsafeRunSync()
    res.pageCount shouldBe 1
    res.entities.size shouldBe 1
    res.entities.head shouldBe dto1
  }

}
