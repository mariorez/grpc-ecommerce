package product.client

import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val port = 50051

    val client = ProductInfoClient(
        ManagedChannelBuilder.forAddress("localhost", port)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build()
    )


    var productID = client.addProduct(
        "Samsung S10",
        "Samsung Galaxy S10 is the latest smart phone, launched in February 2019",
        700.0f
    )

    client.getProduct(productID)

    client.close()
}