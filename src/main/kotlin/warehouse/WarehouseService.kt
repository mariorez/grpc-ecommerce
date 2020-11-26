package warehouse

import ecommerce.WarehouseManagementGrpcKt.WarehouseManagementCoroutineImplBase
import ecommerce.WarehouseManagementOuterClass.ItemAmount
import ecommerce.WarehouseManagementOuterClass.OrderItem
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

class WarehouseService : WarehouseManagementCoroutineImplBase() {

    companion object {
        private val LOG: org.slf4j.Logger = LoggerFactory.getLogger(WarehouseService::class.java)
        private val inventory = hashMapOf(
            "Google Pixel 3A" to 0,
            "Mac Book Pro" to 1,
            "Apple Watch S4" to 2,
            "Google Home Mini" to 3,
            "Google Nest Hub" to 7,
            "Amazon Echo" to 0,
            "Apple iPhone XS" to 2
        )
    }

    override suspend fun getStock(request: OrderItem): ItemAmount {

        val stockAmount = inventory.getOrDefault(request.name, 0)

        LOG.info("==> Stock for: ${request.name} = $stockAmount")

        delay(2000)

        return ItemAmount.newBuilder()
            .setValue(stockAmount)
            .build()
    }
}