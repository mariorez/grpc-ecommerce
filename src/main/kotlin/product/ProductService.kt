package product

import ecommerce.Product
import ecommerce.ProductID
import ecommerce.ProductInfoGrpcKt.ProductInfoCoroutineImplBase
import io.grpc.Status.NOT_FOUND
import io.grpc.StatusException
import java.util.*

class ProductService : ProductInfoCoroutineImplBase() {

    private val productMap: HashMap<String, Product> = HashMap<String, Product>()

    override suspend fun addProduct(request: Product): ProductID {

        val uuid = UUID.randomUUID()
        val product = Product.newBuilder().apply {
            this.id = uuid.toString()
            this.name = request.name
            this.description = request.description
            this.price = request.price
        }.build()

        productMap.put(product.id, product)

        return ProductID.newBuilder().apply {
            this.value = product.id
        }.build()
    }

    override suspend fun getProduct(request: ProductID): Product {

        val productID = request.value

        return productMap[productID]
            ?: throw StatusException(NOT_FOUND.withDescription("Product with id $productID doesn't exist"))
    }
}
