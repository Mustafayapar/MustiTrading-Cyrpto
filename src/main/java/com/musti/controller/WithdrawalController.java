package com.musti.controller;

import com.musti.domain.WalletTransactionType;
 import com.musti.modal.Users;
import com.musti.modal.Wallet;
import com.musti.modal.WalletTransaction;
import com.musti.modal.Withdrawal;
import com.musti.service.TransactionServiceImpl;
import com.musti.service.UserServiceImpl;
import com.musti.service.WalletServiceImpl;
import com.musti.service.WithdrawalServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
 public class WithdrawalController {

    @Autowired
    private WithdrawalServiceImpl withdrawalService;

    @Autowired
    private WalletServiceImpl walletService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TransactionServiceImpl transactionService;




    @PostMapping("/api/withdrwal/{amount}")
    public ResponseEntity<?> withdrawalRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Users user = userService.findUserProfileByJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);

        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount,user);
        walletService.addBalance(userWallet,-withdrawal.getAmount());

       WalletTransaction walletTransaction = transactionService.createTransaction(
               userWallet,
               WalletTransactionType.WITHDRAWAL,null,
               "bank account withdrawal",
               withdrawal.getAmount()
       );


        return new ResponseEntity<>(withdrawal, HttpStatus.OK);

    }


    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Users user = userService.findUserProfileByJwt(jwt);
        Withdrawal withdrawal= withdrawalService.procedWithdrawal(id,accept);

        Wallet userWallet = walletService.getUserWallet(user);
        if (!accept){
            walletService.addBalance(userWallet,withdrawal.getAmount());
        }
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(
            @RequestHeader("Authorization") String jwt) throws Exception {

        Users user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawal = withdrawalService.getUsersWithdrawalHistory(user);
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }


    @GetMapping("/api/admin/withdrawal")

    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Users user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawal = withdrawalService.getWithdrawalRequest();
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

}
