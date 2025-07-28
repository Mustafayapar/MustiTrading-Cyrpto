package com.musti.service;

import com.musti.config.JwtProvider;
import com.musti.domain.VerificationType;
import com.musti.modal.TwoFactorAuth;
import com.musti.modal.Users;
import com.musti.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository _userRepository;



    @Override
    public Users findUserProfileByJwt(String jwt) throws Exception {

        String email = JwtProvider.getEmailFromToken(jwt);
        Users user = _userRepository.findByEmail(email);

        if (user == null) {
            throw new Exception("user not found");
        }


        return user;
    }

    @Override
    public Users findUserByEmail(String email) throws Exception {
        Users user = _userRepository.findByEmail(email);

        if (user == null) {
            throw new Exception("user not found");
        }


        return user;
    }

    @Override
    public Users findUserById(Long userId) throws Exception {
        Optional<Users> user = _userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new Exception("User not found");
        }
        return user.get();
    }

    @Override
    public Users enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, Users users) {

        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);
        users.setTwoFactorAuth(twoFactorAuth);
        return _userRepository.save(users);

    }


    @Override
    public Users updatePassword(Users user, String newPassword) {

        user.setPassword(newPassword);

        return _userRepository.save(user);

    }
}
