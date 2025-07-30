package com.musti.controller;

import com.musti.modal.PaymentDetails;
import com.musti.modal.Users;
import com.musti.service.PaymentDetailsServiceImpl;
import com.musti.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentDetailsController {

    @Autowired
    private PaymentDetailsServiceImpl paymentDetailsService;
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(
            @RequestBody PaymentDetails paymentDetailsReq,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        Users user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails1 =paymentDetailsService.addPaymentDetails(
                paymentDetailsReq.getAccountNumber(),
                paymentDetailsReq.getAccountHolderName(),
                paymentDetailsReq.getIfsc(),
                paymentDetailsReq.getBankName(),
                user
        );
        return new ResponseEntity<>(paymentDetails1, HttpStatus.CREATED);


    }

    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getUsersPaymentDetails(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Users user =userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails1 =paymentDetailsService.getUsersPaymentDetails(user);
        return new ResponseEntity<>(paymentDetails1, HttpStatus.OK);
    }

}
