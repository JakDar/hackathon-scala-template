package com.guys.coding.hackathon.backend.infrastructure.slick

import cats.effect.{ContextShift, IO}
import com.guys.coding.hackathon.backend.infrastructure.slick.example.ExampleSchema
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec}
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps

trait PostgresSpec extends FlatSpec with BeforeAndAfterEach with BeforeAndAfterAll with repo.profile.API {

  implicit val ec: ExecutionContext           = global
  implicit val contextShift: ContextShift[IO] = IO.contextShift(global)

  implicit var db: Database = _

  protected val postgresHost: String =
    ConfigFactory.load("test.conf").getString("postgres-host")
  protected val postgresDbName   = "db_name"
  protected val postgresUsername = "username"
  protected val postgresPassword = "password"
  protected val postgresPort     = 5432

  val schemas =
    List(
      ExampleSchema
    )

  protected def openDbStartingPostgres(config: Config): Unit = {
    db = Database.forConfig("slick.db", config = config)

    schemas.foreach(schema => repo.SchemaUtils.createSchemasIfNotExists(db, schema.schemas))
  }

  protected def closeDb(): Unit =
    db.close()

  protected def clearDB(): Unit =
    Await.result(
      db.run(for {
        _ <- ExampleSchema.examples.delete
      } yield ()),
      5 seconds
    )

  implicit class DatabaseDefExt(database: Database) {
    def runSync[R](a: DBIOAction[R, NoStream, Nothing]): R =
      Await.result(database.run(a), 10 seconds)
  }

  override protected def beforeAll(): Unit =
    openDbStartingPostgres(ConfigFactory.load("test.conf"))

  override protected def afterAll(): Unit =
    closeDb()

  override protected def beforeEach(): Unit =
    clearDB()

  def wait[T](t: Future[T]): T = Await.result(t, 5 seconds)

}

object PostgresSpec extends PostgresSpec
