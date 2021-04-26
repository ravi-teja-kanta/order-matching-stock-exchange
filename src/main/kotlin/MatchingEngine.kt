import java.util.*

class MatchingEngine (
    val stock: StockSymbol,
    private val orders: List<Order>
) {
    fun match(): List<Invoice> {
        val buyQueue = buildOrderQueueBasedOnType(orders) { it.type == OrderType.BUY };
        val sellQueue = buildOrderQueueBasedOnType(orders) { it.type == OrderType.SELL };

        val invoices = mutableListOf<Invoice>();
        while (sellQueue.isNotEmpty() && buyQueue.isNotEmpty()) {

            val sell = sellQueue.peek();
            val buy = buyQueue.peek();

            if (buy.priceInPaise >= sell.priceInPaise) {
                val stocksLeftWithSeller = sell.quantity - buy.quantity;
                val demandLeftWithBuyer = buy.quantity - sell.quantity;
                sellQueue.remove();
                buyQueue.remove();
                if (stocksLeftWithSeller > 0) {
                    val partialOrderLeft = sell.copy(quantity = stocksLeftWithSeller);
                    sellQueue.add(partialOrderLeft);
                    Invoice(
                        sell.orderId,
                        buy.quantity,
                        sell.priceInPaise,
                        buy.orderId
                    ).let {
                        invoices.add(it);
                    }
                } else {
                    val partialOrderLeft = buy.copy(quantity = demandLeftWithBuyer);
                    buyQueue.add(partialOrderLeft);
                    Invoice(
                        sell.orderId,
                        sell.quantity,
                        sell.priceInPaise,
                        buy.orderId
                    ).let {
                        invoices.add(it);
                    }
                }
            }
        }
        return invoices;
    }

    private fun buildOrderQueueBasedOnType(orders: List<Order>, predicate: (o :Order) -> Boolean): PriorityQueue<Order> {
        return orders.filter(predicate)
        .let {
            filteredOrders ->
                PriorityQueue<Order>().also {
                    it.addAll(filteredOrders);
                };
        }
    }
}


fun main() {
    val orders = listOf(
        Order(
            "1",
            System.currentTimeMillis(),
            StockSymbol.BAC,
            OrderType.SELL,
            100,
            2500
        ),
        Order(
            "2",
            System.currentTimeMillis(),
            StockSymbol.BAC,
            OrderType.SELL,
            100,
            2400
        ),
        Order(
            "3",
            System.currentTimeMillis()+1,
            StockSymbol.BAC,
            OrderType.BUY,
            50,
            2600
        ),
        Order(
            "4",
            System.currentTimeMillis()+1,
            StockSymbol.BAC,
            OrderType.BUY,
            150,
            2500
        )
    );

    OrderManager().processOrders(orders);
}
