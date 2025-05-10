package com.ecommerce.payment.adapter.out.persistence;

import com.ecommerce.common.BaseEntity;
import com.ecommerce.common.model.Money;
import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class PaymentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "payment_amount"))
    private Money amount;

    private Long orderId;

    private Long userId;
}
