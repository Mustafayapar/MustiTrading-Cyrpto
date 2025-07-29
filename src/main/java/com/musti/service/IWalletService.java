package com.musti.service;

import com.musti.modal.Order;
import com.musti.modal.Users;
import com.musti.modal.Wallet;

public interface IWalletService {

    Wallet getUserWallet(Users user);
    Wallet addBalance(Wallet wallet, Long money);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(Users user, Wallet receiveWallet, Long amount) throws Exception;
    Wallet payOrderPayment(Order order, Users user) throws Exception;


}
