import com.google.protobuf.StringValue
import ecommerce.OrderManagementGrpcKt.OrderManagementCoroutineStub
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit.MILLISECONDS

fun main() = runBlocking {

    val LOG: Logger = LoggerFactory.getLogger("MainClient")

    val channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext()
        .executor(Dispatchers.Default.asExecutor())
        .build()

    try {
        LOG.info("INIT: call")

        // ORDER CALL
        val orderClient = OrderManagementCoroutineStub(channel)
        val orderId = StringValue.newBuilder().setValue("103").build()
        val order = orderClient
            .withDeadlineAfter(1000, MILLISECONDS)
            .getOrder(orderId)

        LOG.info("FINISH: $order")

    } catch (exception: Exception) {
        LOG.warn("FINISH: ${exception.message}")
    }
}
