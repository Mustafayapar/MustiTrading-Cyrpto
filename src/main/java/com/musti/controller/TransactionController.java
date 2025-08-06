package com.musti.controller;

import com.musti.domain.WalletTransactionType;
import com.musti.modal.Users;
import com.musti.modal.Wallet;
import com.musti.modal.WalletTransaction;
import com.musti.service.ITransactionService;
import com.musti.service.IWalletService;
import com.musti.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private final ITransactionService transactionService;

    @Autowired
    private final IWalletService walletService;
    @Autowired
    private UserServiceImpl userService;

    public TransactionController(ITransactionService transactionService, IWalletService walletService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
    }


    @PostMapping("/withdraw")
    public ResponseEntity<WalletTransaction> withdraw(
            @RequestHeader("Authorization") String token,
            @RequestParam Long userId,
            @RequestParam Long amount) throws Exception {
        Users user =userService.findUserProfileByJwt(token);
        // Kullanıcının cüzdanını al
        Wallet userWallet = walletService.getUserWallet(user);
        if (userWallet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // İşlemi oluştur
        WalletTransaction transaction = transactionService.createTransaction(
                userWallet,
                WalletTransactionType.WITHDRAWAL,
                null,
                "Bank account withdrawal",
                amount
        );

        return ResponseEntity.ok(transaction);
    }
}

