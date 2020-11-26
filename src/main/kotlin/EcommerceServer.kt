import io.grpc.Server
import io.grpc.ServerBuilder
import order.OrderService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import product.ProductService
import warehouse.WarehouseService

class EcommerceServer constructor(
    private val port: Int
) {

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(EcommerceServer::class.java)
    }

    private val server: Server = ServerBuilder
        .forPort(port)
        .intercept(LogInterceptor())
        .addService(ProductService())
        .addService(OrderService())
        .addService(WarehouseService())
        .build()

    fun start() {
        server.start()
        LOG.info("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                LOG.info("*** shutting down gRPC server since JVM is shutting down")
                this@EcommerceServer.stop()
                LOG.info("*** server shut down")
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
