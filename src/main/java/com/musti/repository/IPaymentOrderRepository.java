package com.musti.repository;

import com.musti.modal.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    
}
