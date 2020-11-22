import io.grpc.Server
import io.grpc.ServerBuilder
import product.ProductService
import java.util.logging.Logger

class EcommerceServer constructor(
    private val port: Int
) {

    companion object {
        val logger: Logger = Logger.getLogger(EcommerceServer::class.java.toString())
    }

    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(ProductService())
        .build()

    fun start() {
        server.start()
        logger.info("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                logger.info("*** shutting down gRPC server since JVM is shutting down")
                this@EcommerceServer.stop()
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

fun main() {
    val port = 50051
    val server = EcommerceServer(port)
    server.start()
    server.blockUntilShutdown()
}
