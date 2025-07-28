package com.musti.controller;


import com.musti.config.JwtProvider;
import com.musti.modal.TwoFactorOTP;
import com.musti.modal.Users;
import com.musti.repository.IUserRepository;
import com.musti.response.AuthResponse;
import com.musti.service.CustomeUserDetailsService;
import com.musti.service.EmailService;
import com.musti.service.TwoFactorOtpServiceImpl;
import com.musti.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private CustomeUserDetailsService customeUserDetailsService;
    @Autowired
    private TwoFactorOtpServiceImpl twoFactorOtpService;
    @Autowired
    private EmailService emailService;

    public AuthController(IUserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody Users user) throws Exception {


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

        String jwtToken = JwtProvider.generateToken(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwtToken);
        authResponse.setStatus(true);
        authResponse.setMessage("signup success");


        return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody Users user) throws Exception {

        String userName = user.getEmail();
        String password = user.getPassword();

        Users authUser = userRepository.findByEmail(userName);

        Authentication auth = authenticate(userName, password);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = JwtProvider.generateToken(auth);

        if(user.getTwoFactorAuth().isEnabled()){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two-factor authentication enabled");
            authResponse.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOtp();

            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(authUser.getId());

            if(oldTwoFactorOTP != null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }
            TwoFactorOTP newTwoFactorAuth = twoFactorOtpService.createTwoFactorOtp(authUser, otp,jwtToken);

            emailService.sendVerificationOtpEmail(userName, otp);

            authResponse.setSession(newTwoFactorAuth.getId());

            return new ResponseEntity<>(authResponse,HttpStatus.ACCEPTED);
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwtToken);
        authResponse.setStatus(true);
        authResponse.setMessage("Login success");

        return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
    }


    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customeUserDetailsService.loadUserByUsername(userName);

        if(userDetails == null){
            throw new BadCredentialsException("invalid username ");
        }
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySigninOtp(@PathVariable String otp,
                                                        @RequestParam String id) throws Exception {

        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);
        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP, otp)){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two-factor authentication verified");
            authResponse.setTwoFactorAuthEnabled(true);
            authResponse.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(authResponse,HttpStatus.OK);
        }
        throw new Exception("invalid otp");

    }
}
