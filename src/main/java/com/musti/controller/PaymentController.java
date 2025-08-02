package com.musti.controller;

import com.musti.domain.PaymentMethod;
import com.musti.modal.PaymentOrder;
import com.musti.modal.Users;
import com.musti.response.PaymentResponse;
import com.musti.service.PaymentServiceImpl;
import com.musti.service.UserServiceImpl;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PaymentServiceImpl paymentService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwtToken
            ) throws Exception {

        Users user = userService.findUserProfileByJwt(jwtToken);
        PaymentResponse paymentResponse;
        PaymentOrder order = paymentService.createPaymentOrder(user,amount,paymentMethod);

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentResponse=paymentService.createRazorpayPaymentLing(user, amount);
        }else{
            paymentResponse=paymentService.createStripePaymentLing(user,amount, order.getId());

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
    }
}
