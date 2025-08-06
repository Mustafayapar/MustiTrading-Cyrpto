package com.musti.service;

import com.musti.domain.WalletTransactionType;
import com.musti.modal.Wallet;
import com.musti.modal.WalletTransaction;
import com.musti.repository.IWalletTransacitonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    private IWalletTransacitonRepository walletTransactionRepository;


    @Override
    public WalletTransaction createTransaction(Wallet wallet, WalletTransactionType transactionType, Wallet toWallet, String description, Long amount) {


        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setType(transactionType);
        transaction.setDate(LocalDate.now());
        transaction.setPurpose(description);
        transaction.setAmount(amount);
        transaction.setTransferId(null); // Or generate a UUID or reference if needed

        return walletTransactionRepository.save(transaction);
    }
}
