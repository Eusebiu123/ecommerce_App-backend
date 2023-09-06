package com.sebi.service;


import com.sebi.exception.UserException;
import com.sebi.model.User;

public interface UserService {
    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;

}
