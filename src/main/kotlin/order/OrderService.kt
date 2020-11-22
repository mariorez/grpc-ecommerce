package order

import com.google.protobuf.StringValue
import ecommerce.OrderManagementGrpcKt.OrderManagementCoroutineImplBase
import ecommerce.OrderManagementOuterClass.CombinedShipment
import ecommerce.OrderManagementOuterClass.Order
import kotlinx.coroutines.flow.Flow
import java.util.logging.Logger

class OrderService : OrderManagementCoroutineImplBase() {

    companion object {
        val logger: Logger = Logger.getLogger(OrderService::class.java.toString())
    }

    override suspend fun addOrder(request: Order): StringValue {
        return super.addOrder(request)
    }

    override suspend fun getOrder(request: StringValue): Order {
        return super.getOrder(request)
    }

    override fun searchOrders(request: StringValue): Flow<Order> {
        return super.searchOrders(request)
    }

    override suspend fun updateOrders(requests: Flow<Order>): StringValue {
        return super.updateOrders(requests)
    }

    override fun processOrders(requests: Flow<StringValue>): Flow<CombinedShipment> {
        return super.processOrders(requests)
    }
}
