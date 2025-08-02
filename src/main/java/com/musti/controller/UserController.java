package com.musti.controller;

import com.musti.domain.VerificationType;
import com.musti.modal.ForgotPasswordToken;
import com.musti.modal.Users;
import com.musti.modal.VerificationCode;
import com.musti.request.ForgotPasswordTokenRequest;
import com.musti.request.ResetPasswordRequest;
import com.musti.response.ApiResponse;
import com.musti.response.AuthResponse;
import com.musti.service.EmailServiceImpl;
import com.musti.service.ForgotPasswordServiceImpl;
import com.musti.service.UserServiceImpl;
import com.musti.service.VerificationCodeServiceImpl;
import com.musti.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService ;

    @Autowired
    private VerificationCodeServiceImpl verificationCodeService ;

    @Autowired
    private ForgotPasswordServiceImpl forgotPasswordService ;

    @Autowired
    private EmailServiceImpl emailService ;
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


    @PostMapping("/auth/users/reset-passsword/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
                                                        @RequestBody ForgotPasswordTokenRequest req) throws Exception {
        Users user= userService.findUserByEmail(req.getSendTo());
        String  otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasswordService.findByUserId(user.getId());

        if (token == null) {
            token =forgotPasswordService.createForgotPasswordToken(user,id,otp,req.getVerificationType(),req.getSendTo());
        }
        if (req.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(),token.getOtp());

        }
        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("password reset otp sent successfully");



        return new ResponseEntity<>(response , HttpStatus.OK);

    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse>   resetPassword(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {

         ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

         boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp());
         if (isVerified) {
             userService.updatePassword(forgotPasswordToken.getUser(),req.getPassword());
             ApiResponse response = new ApiResponse();
             response.setMessage("password update successfully");

             return new ResponseEntity<>(response,HttpStatus.ACCEPTED);


         }
         throw new Exception("wrong otp");

    }





}
