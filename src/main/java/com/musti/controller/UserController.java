package com.musti.controller;

import com.musti.domain.VerificationType;
import com.musti.modal.Users;
import com.musti.modal.VerificationCode;
import com.musti.service.EmailService;
import com.musti.service.UserServiceImpl;
import com.musti.service.VerificationCodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService ;

    @Autowired
    private VerificationCodeServiceImpl verificationCodeService ;

    @Autowired
    private EmailService emailService ;
    private String jwt;

    @GetMapping("/api/users/profile")
    public ResponseEntity<Users>   getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {

        Users user= userService.findUserProfileByJwt(jwt);


        return new ResponseEntity<Users>(user, HttpStatus.OK);

    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificaitonOtp(@RequestHeader("Authorization") String jwt,
                                            @PathVariable VerificationType verificationType) throws Exception {

         Users user= userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        if(verificationCode==null){
             verificationCode=verificationCodeService.sendVerificationCode(user,verificationType);
        }
        if (verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(),verificationCode.getOtp());
        }

        return new ResponseEntity<>("verification otp sent successfully" , HttpStatus.OK);

    }



    @PatchMapping("/api/users/enable-two-fac/verify-otp/{otp}")
    public ResponseEntity<Users>   enableTwoFactorAuthentication(
            @PathVariable String otp,
            @RequestHeader("Authorization") String jwt) throws Exception {

        Users user= userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService
                .getVerificationCodeByUser(user.getId());

        String sentTo =verificationCode.getVerificationType().equals(VerificationType.EMAIL)?
                verificationCode.getEmail():verificationCode.getPhone();
        boolean isVerified = verificationCode.getOtp().equals(otp);

        if (isVerified) {
            Users updatedUser =userService.enableTwoFactorAuthentication(
                    verificationCode.getVerificationType(),sentTo,user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }

        throw new Exception("wrong otp");
    }




}
