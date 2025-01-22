package com.sebi.controller;

import com.sebi.config.JwtService;
import com.sebi.exception.ProductException;
import com.sebi.exception.UserException;
import com.sebi.model.Rating;
import com.sebi.model.User;
import com.sebi.repository.UserRepository;
import com.sebi.request.RatingRequest;
import com.sebi.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RatingService ratingService;

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException{
        String token = jwtService.extractBearer(jwt);
        String username = jwtService.extractUserName(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            User newUser = user.get();
            Rating rating=ratingService.createRating(req,newUser);
            return new ResponseEntity<Rating>(rating, HttpStatus.CREATED);
        }else {
            throw new UserException("User not found!");
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductsRating(@PathVariable Long productId,
                                                          @RequestHeader("Authorization") String jwt) throws UserException,ProductException{
        List<Rating> ratings=ratingService.getProductsRating(productId);
        return new ResponseEntity<>(ratings,HttpStatus.CREATED);

    }
}
