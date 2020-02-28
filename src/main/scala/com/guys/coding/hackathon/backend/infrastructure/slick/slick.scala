package com.guys.coding.hackathon.backend.infrastructure

import hero.common.postgres.PgCircePostgresProfile

package object slick {
  val repo = hero.common.jdbc.repository(PgCircePostgresProfile)
}
