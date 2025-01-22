package com.sebi.controller;

import com.sebi.config.JwtService;
import com.sebi.exception.UserException;
import com.sebi.model.User;
import com.sebi.repository.UserRepository;
import com.sebi.response.AuthResponse;
import com.sebi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final JwtService jwtService;
    private final UserRepository userRepository;


    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException{
        String token = jwtService.extractBearer(jwt);
        String username = jwtService.extractUserName(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return new ResponseEntity<>(user.get(),HttpStatus.ACCEPTED);
        }else{
            throw new UserException("User not found!");
        }
    }
}
