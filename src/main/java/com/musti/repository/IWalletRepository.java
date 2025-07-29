package com.musti.repository;

import com.musti.modal.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findByUserId(Long userId);

}
