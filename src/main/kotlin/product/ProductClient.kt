package product

import ecommerce.Product
import ecommerce.ProductID
import ecommerce.ProductInfoGrpcKt.ProductInfoCoroutineStub
import io.grpc.ManagedChannel
import kotlinx.coroutines.runBlocking
import java.io.Closeable
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class ProductClient constructor(
    private val channel: ManagedChannel
) : Closeable {

    companion object {
        val logger: Logger = Logger.getLogger(ProductClient::class.java.toString())
    }

    private val stub: ProductInfoCoroutineStub = ProductInfoCoroutineStub(channel)

    suspend fun addProduct(name: String, description: String, price: Float): String = runBlocking {

        var product = Product.newBuilder().apply {
            this.name = name
            this.description = description
            this.price = price
        }.build()

        var response = stub.addProduct(product)
        var productID = response.value

        logger.info("*** Product added successfully - ID:  $productID")

        return@runBlocking productID
    }

    suspend fun getProduct(productID: String): Product = runBlocking {

        var request = ProductID.newBuilder().apply {
            this.value = productID;
        }.build()

        var response = stub.getProduct(request)

        logger.info("*** Product found:\n$response")

        return@runBlocking response
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
