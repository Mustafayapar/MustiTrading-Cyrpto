package com.musti.repository;

import com.musti.modal.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVerificaitonCodeRepository  extends JpaRepository<VerificationCode,Long> {

    public VerificationCode findByUserId(Long userId);



}
