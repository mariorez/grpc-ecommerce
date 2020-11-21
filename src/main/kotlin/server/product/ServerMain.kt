package server.product

fun main() {
    val port = 50051
    val server = ProductInfoServer(port)
    server.start()
    server.blockUntilShutdown()
}
