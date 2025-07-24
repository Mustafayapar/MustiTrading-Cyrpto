package com.musti.controller;


import com.musti.modal.Users;
import com.musti.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserRepository userRepository;

    public AuthController(IUserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @PostMapping("/signup")
    public ResponseEntity<Users> register(@RequestBody Users user) throws Exception {


        Users isEmailExist = userRepository.findByEmail(user.getEmail());
        if(isEmailExist != null){
            throw new Exception("email is already used with another account");

        }
        Users newUser = new Users();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setFullName(user.getFullName());
        newUser.setPhone(user.getPhone());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
         
        ) ;
        SecurityContextHolder.getContext().setAuthentication(auth);

        Users savedUser =userRepository.save(newUser);

        return new ResponseEntity<>(savedUser,HttpStatus.CREATED);
    }
}
