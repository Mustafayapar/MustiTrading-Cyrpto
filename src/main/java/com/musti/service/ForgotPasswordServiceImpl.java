package com.musti.service;

import com.musti.domain.VerificationType;
import com.musti.modal.ForgotPasswordToken;
import com.musti.modal.Users;
import com.musti.repository.IForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements IForgotPasswordService {

    @Autowired
    private IForgotPasswordRepository forgotPasswordRepository;



    @Override
    public ForgotPasswordToken createForgotPasswordToken(Users user,
                                                         String id,
                                                         String otp,
                                                         VerificationType type,
                                                         String sendTo) {

        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setUser(user);
        forgotPasswordToken.setSendTo(sendTo);
        forgotPasswordToken.setVerificationType(type);
        forgotPasswordToken.setOtp(otp);
        forgotPasswordToken.setId(id);


        return forgotPasswordRepository.save(forgotPasswordToken);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> optionalForgotPasswordToken =
                forgotPasswordRepository.findById(id);
        return optionalForgotPasswordToken.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUserId(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteForgotPasswordToken(ForgotPasswordToken token) {
        forgotPasswordRepository.delete(token);

    }


}
