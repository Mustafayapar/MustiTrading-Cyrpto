package com.musti.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.musti.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class Users {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private long id;

     private String fullName;
     private String email;
     private String phone;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
     private String password;

     @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

     private  USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public TwoFactorAuth getTwoFactorAuth() {
        return twoFactorAuth;
    }

    public void setTwoFactorAuth(TwoFactorAuth twoFactorAuth) {
        this.twoFactorAuth = twoFactorAuth;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }
}
