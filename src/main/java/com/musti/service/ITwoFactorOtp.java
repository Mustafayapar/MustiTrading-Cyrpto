package com.musti.service;

import com.musti.modal.TwoFactorOTP;
import com.musti.modal.Users;

public interface ITwoFactorOtp {

    TwoFactorOTP createTwoFactorOtp(Users user, String otp, String jwt);

    TwoFactorOTP findByUser(Long userId);
    TwoFactorOTP findById(String id);

    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);
}
