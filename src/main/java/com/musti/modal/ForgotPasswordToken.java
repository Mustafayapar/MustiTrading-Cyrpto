package com.musti.modal;

import com.musti.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private Users user;

    private String otp;
    private VerificationType verificationType;
    private String sendTo;
}
