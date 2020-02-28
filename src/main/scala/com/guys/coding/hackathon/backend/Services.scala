package com.guys.coding.hackathon.backend

import com.guys.coding.hackathon.backend.domain.{ExampleService, TokenService}
import cats.effect.IO

case class Services(exampleService: ExampleService[IO], jwtTokenService: TokenService)
