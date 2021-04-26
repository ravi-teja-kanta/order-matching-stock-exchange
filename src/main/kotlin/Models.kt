import java.sql.Timestamp

data class Order(
    val orderId: String,
    val timeStamp: Long,
    val stock: StockSymbol,
    val type: OrderType,
    val quantity: Long,
    val priceInPaise: Long
): Comparable<Order> {
    override fun compareTo(other: Order): Int {
        return when {
            priceInPaise != other.priceInPaise -> {
                if (type == OrderType.SELL)
                    (priceInPaise - other.priceInPaise).toInt()
                else (other.priceInPaise - priceInPaise).toInt()
            }
            else -> (timeStamp - other.timeStamp).toInt()
        }
    }

}
data class Invoice(
    val sellerOrderId: String,
    val quantity: Long,
    val sellPriceInPaise: Long,
    val buyerOrderId: String
)

enum class OrderType {
    BUY,
    SELL
}

enum class StockSymbol {
    BAC,
    GOOGL
}