package com.musti.repository;

import com.musti.modal.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IWithdrawalRepository extends JpaRepository<Withdrawal, Long> {

    List<Withdrawal> findByUserId(Long userId);

}
