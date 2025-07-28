package com.musti.repository;

import com.musti.modal.ForgotPasswordToken;
import com.musti.modal.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IForgotPasswordRepository extends JpaRepository<ForgotPasswordToken,String> {

    ForgotPasswordToken findByUserId(Long userId);


    String user(Users user);
}
