package com.musti.controller;

import com.musti.modal.*;
import com.musti.response.PaymentResponse;
import com.musti.service.OrderServiceImpl;
import com.musti.service.PaymentServiceImpl;
import com.musti.service.UserServiceImpl;
import com.musti.service.WalletServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletServiceImpl walletService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorizaiton") String token) throws Exception {
        Users user = userService.findUserProfileByJwt(token);

        Wallet wallet = walletService.getUserWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);

    }

    @PutMapping("/api/wallet/{walledId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String token,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction walletTransaction

            ) throws Exception {
        Users senderUser =userService.findUserProfileByJwt(token);
        Wallet receiverWallet = walletService.getUserWallet(senderUser);
        Wallet wallet =walletService.walletToWalletTransfer(senderUser, receiverWallet,walletTransaction.getAmount());



        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);

    }

    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId


    ) throws Exception {
        Users user =userService.findUserProfileByJwt(token);
       Order order = orderService.getOrderById(orderId);
       Wallet wallet = walletService.payOrderPayment(order,user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);

    }

    @PutMapping("/api/wallet/deposit")
    public ResponseEntity<Wallet> addBalanceToWallet(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(name="order_id") Long orderId,
            @RequestParam(name="payment_id") String paymentId


    ) throws Exception {
        Users user =userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);

        PaymentOrder order=paymentService.getPaymentOrderById(orderId);

        Boolean status= paymentService.proceedPaymentOrder(order,paymentId);

        if(status){
            wallet =walletService.addBalance(wallet,order.getAmount());
        }

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);

    }





}
