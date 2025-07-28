package com.musti.service;

import com.musti.domain.VerificationType;
import com.musti.modal.Users;
import com.musti.modal.VerificationCode;

public interface IVerificationCodeService {

    public VerificationCode sendVerificationCode(Users user, VerificationType type);
    public VerificationCode getVerificationCodeById(Long id) throws Exception;

    public VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);


}
