package server.product

import io.grpc.Server
import io.grpc.ServerBuilder

class ProductInfoServer constructor(
    private val port: Int
) {
    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(ProductInfoService())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@ProductInfoServer.stop()
                println("*** server shut down")
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