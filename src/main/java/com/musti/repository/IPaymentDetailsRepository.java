package com.musti.repository;

import com.musti.modal.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

    PaymentDetails findByUserId(Long userId);
}
