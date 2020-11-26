import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import product.ProductClient

fun main() = runBlocking {

    val channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext()
        .executor(Dispatchers.Default.asExecutor())
        .build()

    callProductClient(channel)
}

private suspend fun callProductClient(channel: ManagedChannel) {
    val client = ProductClient(channel)

    val productID = client.addProduct(
        "Samsung S10",
        "Samsung Galaxy S10 is the latest smart phone, launched in February 2019",
        700.0f
    )

    client.getProduct(productID)
    client.close()
}