package com.musti.repository;


import com.musti.modal.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {

    TwoFactorOTP findByUserId(Long userId);

}
