package com.musti.service;

import com.musti.domain.VerificationType;
import com.musti.modal.Users;
import com.musti.modal.VerificationCode;
import com.musti.repository.IVerificaitonCodeRepository;
import com.musti.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements IVerificationCodeService{

    @Autowired
    private IVerificaitonCodeRepository verificaitonCodeRepository;



    @Override
    public VerificationCode sendVerificationCode(Users user, VerificationType verificationType) {

        VerificationCode verificationCode1 =  new VerificationCode();
        verificationCode1.setOtp(OtpUtils.generateOtp());
        verificationCode1.setVerificationType(verificationType);
        verificationCode1.setUser(user);

        return verificaitonCodeRepository.save(verificationCode1);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificationCodeOptional = verificaitonCodeRepository.findById(id);

        if (verificationCodeOptional.isPresent()) {
            return verificationCodeOptional.get();

        }
        throw new Exception("Verification code not found");


    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificaitonCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        verificaitonCodeRepository.delete(verificationCode);

    }
}
