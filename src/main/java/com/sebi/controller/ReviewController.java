package com.sebi.controller;

import com.sebi.config.JwtService;
import com.sebi.exception.ProductException;
import com.sebi.exception.UserException;
import com.sebi.model.Review;
import com.sebi.model.User;
import com.sebi.repository.UserRepository;
import com.sebi.request.ReviewRequest;
import com.sebi.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;
    private JwtService jwtService;
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException{
        String token = jwtService.extractBearer(jwt);
        String username = jwtService.extractUserName(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            User newUser = user.get();
            Review review = reviewService.createReview(req, newUser);
            return new ResponseEntity<>(review, HttpStatus.CREATED);
        }else{
            throw new UserException("User not found!");
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductsReview(@PathVariable Long productId) throws UserException,ProductException{
        List<Review> reviews = reviewService.getAllReview(productId);
        return new ResponseEntity<>(reviews,HttpStatus.ACCEPTED);
    }
}
