package order

import ecommerce.WarehouseManagementGrpcKt.WarehouseManagementCoroutineStub
import ecommerce.WarehouseManagementOuterClass.OrderItem
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS


class WarehouseClient constructor(
    private val channel: ManagedChannel = ManagedChannelBuilder
        .forAddress("localhost", 50051)
        .usePlaintext()
        .executor(Dispatchers.Default.asExecutor())
        .build()
) : Closeable {

    private val client: WarehouseManagementCoroutineStub = WarehouseManagementCoroutineStub(channel)

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(WarehouseClient::class.java)
    }

    suspend fun getStock(item: String): Int = runBlocking {

        val request = OrderItem.newBuilder().setName(item).build()

        val response = client
            .withDeadlineAfter(3000, MILLISECONDS)
            .getStock(request)

        LOG.info("==> Received ${response.value} items in stock")

        return@runBlocking response.value
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
