package com.ecommerce.product.command.adapter.out.persistence

import com.ecommerce.product.command.application.out.StockPort
import com.ecommerce.product.command.domain.model.Stock
import org.springframework.stereotype.Repository

@Repository
class StockPersistenceAdapter(
    private val stockJpaRepository: StockJpaRepository,
    private val stockEntityMapper: StockEntityMapper
) : StockPort {
    override fun loadStockByProductId(productId: Long): Stock {
        val stockEntity = stockJpaRepository.findByProductId(productId)
            .orElseThrow { IllegalArgumentException("not found stock product id: $productId") }
        return stockEntityMapper.toStock(stockEntity)
    }

    override fun loadStockForUpdateByProductId(productId: Long): Stock {
        val stockEntity = stockJpaRepository.findForUpdateByProductId(productId)
            .orElseThrow { IllegalArgumentException("not found stock product id: $productId") }
        return stockEntityMapper.toStock(stockEntity)
    }

    override fun saveStock(stock: Stock) {
        val stockEntity = stockEntityMapper.toStockEntity(stock)
        stockJpaRepository.save(stockEntity)
    }

    override fun saveAllStock(stocks: List<Stock>) {
        val stockEntities = stocks.map { stockEntityMapper.toStockEntity(it) }
        stockJpaRepository.saveAll(stockEntities)
    }

}