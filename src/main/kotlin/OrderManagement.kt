class OrderManager {
    fun processOrders(orders: List<Order>): List<Invoice> {
        return segregateOrdersByStock(orders)
            .map {
                (stock, orders) ->
                    buildMatchingEngineForTheStock(stock, orders);
            }.map {
                it.match()
            }.flatten().also {
                it.forEach {
                    println(it);
                }
            }
    }
    private fun buildMatchingEngineForTheStock(stock: StockSymbol, orders: List<Order>): MatchingEngine {
        return MatchingEngine(stock, orders)
    }
    private fun segregateOrdersByStock(orders: List<Order>): Map<StockSymbol, List<Order>> {
        return orders.groupBy {
            it.stock
        }
    };
}