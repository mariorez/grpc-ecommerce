package product

import ecommerce.Product
import ecommerce.ProductID
import ecommerce.ProductInfoGrpcKt.ProductInfoCoroutineStub
import io.grpc.ManagedChannel
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS


class ProductClient constructor(
    private val channel: ManagedChannel
) : Closeable {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(ProductClient::class.java)
    }

    private val stub: ProductInfoCoroutineStub = ProductInfoCoroutineStub(channel)

    suspend fun addProduct(name: String, description: String, price: Float): String = runBlocking {

        var product = Product.newBuilder().apply {
            this.name = name
            this.description = description
            this.price = price
        }.build()

        var response = stub
            .withDeadlineAfter(3000, MILLISECONDS)
            .addProduct(product)
        var productID = response.value

        LOG.info("*** Product added successfully - ID:  $productID")

        return@runBlocking productID
    }

    suspend fun getProduct(productID: String): Product = runBlocking {

        var request = ProductID.newBuilder().apply {
            this.value = productID;
        }.build()

        var response = stub
            .withDeadlineAfter(3000, MILLISECONDS)
            .getProduct(request)

        LOG.info("*** Product found:\n$response")

        return@runBlocking response
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
