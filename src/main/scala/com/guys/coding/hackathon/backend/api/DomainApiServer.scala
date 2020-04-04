package com.guys.coding.hackathon.backend.api
import com.typesafe.scalalogging.StrictLogging
import io.grpc.ServerBuilder

import scala.concurrent.ExecutionContext
import hero.common.util.ToUnit.AnyExt
import com.chatbotize.ecommerce.provider.manual.api.ManualEcommerceProviderGrpc

class DomainApiServer(endpoint: DomainApiEndpoint, port: Int)(implicit ec: ExecutionContext) extends StrictLogging {

  private val server = ServerBuilder.forPort(port).addService(ManualEcommerceProviderGrpc.bindService(endpoint, ec)).build()

  def initialize(): Unit = {
    try {
      server.start().unit
      logger.info(s"Server started at 0.0.0.0:$port")
    } catch {
      case ex: Throwable =>
        logger.error(s"Error while starting DomainAPI at  0.0.0.0:$port", ex)
    }

  }
}
