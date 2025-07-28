package com.musti.service;

import com.musti.domain.VerificationType;
import com.musti.modal.Users;

public interface IUserService {

    public Users findUserProfileByJwt(String jwt) throws Exception;
    public Users findUserByEmail(String email) throws Exception;
    public Users findUserById(Long userId) throws Exception;

    public Users enableTwoFactorAuthentication(VerificationType verificationType , String sendTo, Users users);

    Users updatePassword(Users user, String newPassword);

}
