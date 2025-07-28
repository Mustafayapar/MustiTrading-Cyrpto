package com.musti.service;

import com.musti.modal.TwoFactorOTP;
import com.musti.modal.Users;
import com.musti.repository.ITwoFactorOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements ITwoFactorOtp{

    @Autowired
    private ITwoFactorOtpRepository twoFactorOtpRepository;

    @Override
    public TwoFactorOTP createTwoFactorOtp(Users user, String otp, String jwt) {

        UUID uuid = UUID.randomUUID();

        String id = uuid.toString();

        TwoFactorOTP twoFactorOtp = new TwoFactorOTP();
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setJwt(jwt);
        twoFactorOtp.setId(id);
        twoFactorOtp.setUser(user);
        return twoFactorOtpRepository.save(twoFactorOtp);

    }

    @Override
    public TwoFactorOTP findByUser(Long userId) {

        return twoFactorOtpRepository.findByUserId(userId);

    }

    @Override
    public TwoFactorOTP findById(String id) {

        Optional<TwoFactorOTP> twoFactorOtp = twoFactorOtpRepository.findById(id);
        return twoFactorOtp.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp) {

        return twoFactorOtp.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp) {

        twoFactorOtpRepository.delete(twoFactorOtp);

    }
}
