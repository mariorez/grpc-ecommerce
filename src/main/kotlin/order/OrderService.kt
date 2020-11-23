package order

import com.google.protobuf.StringValue
import ecommerce.OrderManagementGrpcKt.OrderManagementCoroutineImplBase
import ecommerce.OrderManagementOuterClass.CombinedShipment
import ecommerce.OrderManagementOuterClass.Order
import io.grpc.Status.NOT_FOUND
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.logging.Logger
import kotlin.random.Random

class OrderService : OrderManagementCoroutineImplBase() {

    companion object {
        val logger: Logger = Logger.getLogger(OrderService::class.java.toString())
        private val ord1 = Order.newBuilder()
            .setId("102")
            .addItems("Google Pixel 3A").addItems("Mac Book Pro")
            .setDestination("Mountain View, CA")
            .setPrice(1800f)
            .build()
        private val ord2 = Order.newBuilder()
            .setId("103")
            .addItems("Apple Watch S4")
            .setDestination("San Jose, CA")
            .setPrice(400f)
            .build()
        private val ord3 = Order.newBuilder()
            .setId("104")
            .addItems("Google Home Mini").addItems("Google Nest Hub")
            .setDestination("Mountain View, CA")
            .setPrice(400f)
            .build()
        private val ord4 = Order.newBuilder()
            .setId("105")
            .addItems("Amazon Echo")
            .setDestination("San Jose, CA")
            .setPrice(30f)
            .build()
        private val ord5 = Order.newBuilder()
            .setId("106")
            .addItems("Amazon Echo").addItems("Apple iPhone XS")
            .setDestination("Mountain View, CA")
            .setPrice(300f)
            .build()

        private val orderMap: HashMap<String, Order> = hashMapOf(
            ord1.id to ord1,
            ord2.id to ord2,
            ord3.id to ord3,
            ord4.id to ord4,
            ord5.id to ord5
        )

        private val combinedShipmentMap: HashMap<String, CombinedShipment> = HashMap()
    }

    override suspend fun addOrder(request: Order): StringValue {

        logger.info("Order Added - ID: ${request.id}, Destination : ${request.destination}")

        orderMap[request.id] = request

        return StringValue.newBuilder().setValue(request.id).build()
    }

    override suspend fun getOrder(request: StringValue): Order {

        logger.info("Get order: ${request.value}")

        return orderMap.getOrElse(request.value) {
            throw StatusRuntimeException(NOT_FOUND.withDescription("Order ID: ${request.value}, not found"))
        }
    }

    override fun searchOrders(request: StringValue): Flow<Order> = flow {

        logger.info("Search for: ${request.value}")

        orderMap.forEach { (_, order) ->
            order.itemsList.forEach {
                if (it.contains(request.value, true)) {
                    logger.info("Found: $it")
                    emit(order)
                }
            }
        }
    }

    override suspend fun updateOrders(requests: Flow<Order>): StringValue {

        val response = StringValue.newBuilder().setValue("Updated orders")

        requests.collect {

            logger.info("Update order: ${it.id}")

            orderMap.getOrElse(it.id) {
                val exception = StatusRuntimeException(NOT_FOUND.withDescription("Order ID: ${it.id}, not found"))
                logger.warning(exception.message)
                throw exception
            }

            orderMap[it.id] = it

            response.value = "${response.build().value} - ${it.id}"
        }

        return response.build()
    }

    override fun processOrders(requests: Flow<StringValue>): Flow<CombinedShipment> = flow {

        requests.collect {

            logger.info("Process shipment for order: ${it.value}")

            val currentOrder = orderMap.getOrElse(it.value) {
                val exception = StatusRuntimeException(NOT_FOUND.withDescription("Order ID: ${it.value}, not found"))
                logger.warning(exception.message)
                throw exception
            }

            val existentShipment: CombinedShipment? = combinedShipmentMap[currentOrder.destination]
            val shipment = CombinedShipment.newBuilder();

            if (existentShipment != null) {
                logger.info("Shipment already exist: ${existentShipment.id}")
                shipment.mergeFrom(existentShipment).addOrdersList(currentOrder);
            } else {
                logger.info("Generate new shipment")
                shipment
                    .setId(Random(System.nanoTime()).nextInt(1000).toString())
                    .addOrdersList(currentOrder)
            }

            combinedShipmentMap[currentOrder.destination] = shipment.build()

            emit(shipment.build())
        }
    }
}
