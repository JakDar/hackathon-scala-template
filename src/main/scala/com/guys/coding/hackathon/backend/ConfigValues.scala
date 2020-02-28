package com.guys.coding.hackathon.backend

import com.typesafe.config.Config
import com.guys.coding.hackathon.backend.ConfigValues._

case class ConfigValues(
    app: ApplicationConfig,
    authKeys: AuthKeys,
    raw: Config
)

object ConfigValues {

  def apply(config: Config): ConfigValues = new ConfigValues(
    ApplicationConfig(
      bindHost = config.getString("bind-host"),
      bindPort = config.getInt("bind-port"),
      appLogLevel = config.getString("log-level.app"),
      rootLogLevel = config.getString("log-level.root")
    ),
    AuthKeys(config.getString("keys.private"), config.getString("keys.public")),
    config
  )

  case class AuthKeys(privatePath: String, publicPath: String)
  case class ApplicationConfig(
      bindHost: String,
      bindPort: Int,
      appLogLevel: String,
      rootLogLevel: String
  )
}
