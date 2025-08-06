package com.musti.service;

import com.musti.domain.WalletTransactionType;
import com.musti.modal.Wallet;
import com.musti.modal.WalletTransaction;

import java.math.BigDecimal;

public interface ITransactionService {

    WalletTransaction createTransaction(
            Wallet wallet,
            WalletTransactionType transactionType,
            Wallet toWallet,
            String description,
            Long amount
    );

}
