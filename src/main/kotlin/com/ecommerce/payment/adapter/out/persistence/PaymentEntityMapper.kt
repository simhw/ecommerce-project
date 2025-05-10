package com.ecommerce.payment.adapter.out.persistence

import com.ecommerce.payment.domain.model.Payment
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface PaymentEntityMapper {
    fun toPaymentEntity(payment: Payment): PaymentEntity
}