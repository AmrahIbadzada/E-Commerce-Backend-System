package com.payment.mypayment_ms.repository;

import com.payment.mypayment_ms.entity.PaymentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentHistoryRepository extends JpaRepository<PaymentHistoryEntity, Long> {
}
