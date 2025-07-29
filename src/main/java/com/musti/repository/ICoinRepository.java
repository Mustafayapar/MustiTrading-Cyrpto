package com.musti.repository;

import com.musti.modal.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICoinRepository extends JpaRepository<Coin, String> {



}
