package com.sebi.service;

import com.sebi.model.User;
import com.sebi.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomeUserServiceImplementation implements UserDetailsService {
    private UserRepository userRepository;
    public CustomeUserServiceImplementation(UserRepository userRepository )
    {
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<User> user=userRepository.findByUsername(username);
            if(user.isEmpty()){
                throw new UsernameNotFoundException("user not found with email "+username);
            }
            List<GrantedAuthority> authorities = new ArrayList<>();
            return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),authorities);
    }
}
