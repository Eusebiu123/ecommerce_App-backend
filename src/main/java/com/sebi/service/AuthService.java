package com.sebi.service;

import com.sebi.config.JwtService;
import com.sebi.exception.UserException;
import com.sebi.model.Cart;
import com.sebi.model.Token;
import com.sebi.model.User;
import com.sebi.repository.TokenRepository;
import com.sebi.repository.UserRepository;
import com.sebi.request.LoginRequest;
import com.sebi.response.AuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final UserService userService;
    private final TokenRepository tokenRepository;

    public AuthResponse register(User request)
    {
        User user= new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user= userRepository.save(user);
        Cart cart = cartService.createCart(user);

        String jwt = jwtService.generateToken(user);
        saveUserToken(jwt, user);


        return new AuthResponse(jwt,"Registration successfully!");
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getId());

        if(!validTokenListByUser.isEmpty()){
            validTokenListByUser.forEach(t->{
                t.setLoggedOut(true);
            });
        }
        tokenRepository.saveAll(validTokenListByUser);
    }

    public AuthResponse login(User request)
    {
        Authentication authentication = authenticate(request.getUsername(),request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(authentication.getAuthorities());

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        String token = jwtService.generateToken(user);
        revokeAllTokenByUser(user);
        saveUserToken(token,user);

        return new AuthResponse(token,"Authenticated successfully!");
    }
    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = userService.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("invalid Username");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

    public AuthResponse logout(String jwt) throws UserException {
        String token = jwtService.extractBearer(jwt);
        String username = jwtService.extractUserName(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            SecurityContextHolder.clearContext();
            User userFound = user.get();
            Token storedToken = tokenRepository.findByToken(token).orElse(null);
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
            return new AuthResponse(token,"Logout successfully!");
        }else{
            throw new UserException("User not found!");
        }
    }
}
