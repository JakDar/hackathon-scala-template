package com.guys.coding.hackathon.backend.infrastructure.slick

import com.github.tminglei.slickpg.{ExPostgresProfile, PgCirceJsonSupport, PgPostGISSupport}

trait HackathonbackendPostgresProfile extends ExPostgresProfile with PgPostGISSupport with PgCirceJsonSupport {

  override def pgjson: String = "jsonb" // jsonb support is in postgres 9.4.0 onward; for 9.3.x use "json"

  override val api: API = new API {}

  val plainAPI = new API with CirceJsonPlainImplicits

  trait API extends super.API with PostGISImplicits with PostGISAssistants with JsonImplicits
}

object HackathonbackendPostgresProfile extends HackathonbackendPostgresProfile
