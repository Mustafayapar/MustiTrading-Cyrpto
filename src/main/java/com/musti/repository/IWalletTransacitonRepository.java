package com.musti.repository;

import com.musti.modal.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWalletTransacitonRepository extends JpaRepository<WalletTransaction,Long >{


}
