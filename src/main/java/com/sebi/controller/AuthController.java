package com.sebi.controller;


import com.sebi.exception.UserException;
import com.sebi.model.Cart;
import com.sebi.model.User;
import com.sebi.repository.UserRepository;
import com.sebi.request.LoginRequest;
import com.sebi.response.AuthResponse;
import com.sebi.service.AuthService;
import com.sebi.service.CartService;
import com.sebi.service.CustomeUserServiceImplementation;
import com.sebi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private CustomeUserServiceImplementation customUserService;
    private CartService cartService;
    private final AuthService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
        AuthResponse authResponse =userService.register(user);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);


    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody User loginRequest)
    {
        AuthResponse authResponse = userService.login(loginRequest);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logoutUserHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        AuthResponse authResponse = userService.logout(jwt);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }



}
