package product.server

import io.grpc.Server
import io.grpc.ServerBuilder
import java.util.logging.Logger

class ProductInfoServer constructor(
    private val port: Int
) {

    companion object {
        val logger: Logger = Logger.getLogger(ProductInfoServer::class.java.toString())
    }

    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(ProductInfoService())
        .build()

    fun start() {
        server.start()
        logger.info("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                logger.info("*** shutting down gRPC server since JVM is shutting down")
                this@ProductInfoServer.stop()
                logger.info("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}