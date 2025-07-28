package com.musti.service;

import com.musti.domain.VerificationType;
import com.musti.modal.ForgotPasswordToken;
import com.musti.modal.Users;

public interface IForgotPasswordService {

    ForgotPasswordToken createForgotPasswordToken(Users user,
                                                  String id,
                                                  String otp,
                                                  VerificationType type,
                                                  String sendTo
                                                  );
    ForgotPasswordToken findById(String id);
    ForgotPasswordToken findByUserId(Long id);
    void deleteForgotPasswordToken(ForgotPasswordToken token);


}
