package com.ecommerce.product.command.application.out

import com.ecommerce.product.command.domain.model.Stock

interface StockPort {
    fun loadStockByProductId(productId: Long): Stock

    fun loadStockForUpdateByProductId(productId: Long): Stock

    fun saveStock(stock: Stock)

    fun saveAllStock(stocks: List<Stock>)
}