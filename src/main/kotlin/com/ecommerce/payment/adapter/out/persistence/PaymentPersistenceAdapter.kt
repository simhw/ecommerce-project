package com.ecommerce.payment.adapter.out.persistence

import com.ecommerce.payment.applicaiton.out.SavePaymentPort
import com.ecommerce.payment.domain.model.Payment
import org.springframework.stereotype.Repository

@Repository
class PaymentPersistenceAdapter(
    private val paymentJpaRepository: PaymentJpaRepository,
    private val paymentEntityMapper: PaymentEntityMapper
) : SavePaymentPort {
    override fun savePayment(payment: Payment) {
        val paymentEntity = paymentEntityMapper.toPaymentEntity(payment)
        paymentJpaRepository.save(paymentEntity)
    }
}