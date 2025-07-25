package com.musti.repository;

import com.musti.modal.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);

    String email(String email);
}
