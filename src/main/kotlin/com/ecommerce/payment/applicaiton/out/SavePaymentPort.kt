package com.ecommerce.payment.applicaiton.out

import com.ecommerce.payment.domain.model.Payment

interface SavePaymentPort {
    fun savePayment(payment: Payment)
}