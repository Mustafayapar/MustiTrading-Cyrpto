package com.musti.service;

import com.musti.domain.OrderType;
import com.musti.modal.Order;
import com.musti.modal.Users;
import com.musti.modal.Wallet;
import com.musti.repository.IWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements IWalletService{

    @Autowired
    public IWalletRepository walletRepository;


    @Override
    public Wallet getUserWallet(Users user) {

        Wallet wallet = walletRepository.findByUserId(user.getId());
        if(wallet == null){
            wallet = new Wallet();
            wallet.setUser(user);

            wallet = walletRepository.save(wallet);
        }

        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long money) {

        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));
        wallet.setBalance(newBalance);

        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {

        Optional<Wallet> wallet = walletRepository.findById(id);
        if(wallet.isPresent()){
            return wallet.get();
        }
        throw new Exception("wallet not found");
    }

    @Override
    public Wallet walletToWalletTransfer(Users user, Wallet receiveWallet, Long amount) throws Exception {

        Wallet senderWallet = getUserWallet(user);
        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0){
            throw new Exception("Insufficient balance...");

        }

        BigDecimal senderBalance = senderWallet.getBalance()
                .subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);

        BigDecimal receiverBalance = receiveWallet.getBalance()
                .add(BigDecimal.valueOf(amount));
        receiveWallet.setBalance(receiverBalance);
        walletRepository.save(receiveWallet);

        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, Users user) throws Exception {

        Wallet wallet = getUserWallet(user);

        if (order.getOrderType().equals(OrderType.BUY)){
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            if (newBalance.compareTo(BigDecimal.ZERO) < 0){
                throw new Exception("Insufficent funds for this transaction");
            }
            wallet.setBalance(newBalance);
        }
        else {
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            wallet.setBalance(newBalance);
        }
        walletRepository.save(wallet);

        return wallet;
    }
}
