package com.sebi.controller;

import com.sebi.config.JwtService;
import com.sebi.exception.ProductException;
import com.sebi.exception.UserException;
import com.sebi.model.Cart;
import com.sebi.model.User;
import com.sebi.repository.CartRepository;
import com.sebi.repository.UserRepository;
import com.sebi.request.AddItemRequest;
import com.sebi.response.ApiResponse;
import com.sebi.service.CartService;
import com.sebi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@Tag(name="Cart Management",description= "find user cart, add item to cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private  JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    @Operation(description = "find cart by user id")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt )throws UserException{
        String token = jwtService.extractBearer(jwt);
        String username = jwtService.extractUserName(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            User newUser = user.get();
            Cart cart = cartService.findUserCart(newUser.getId());
            return new ResponseEntity<Cart>(cart, HttpStatus.OK);
        }else{
            throw new UserException("User not found!");
        }
    }

    @PutMapping("/add")
    @Operation(description = "add item to cart")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req,
                                                     @RequestHeader("Authorization") String jwt) throws UserException, ProductException{
        String token = jwtService.extractBearer(jwt);
        String username = jwtService.extractUserName(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            User newUser = user.get();
            cartService.addCartItem(newUser.getId(),req);
            ApiResponse res = new ApiResponse();
            res.setMessage("item added to cart");
            res.setStatus(true);
            return new ResponseEntity<>(res,HttpStatus.OK);
        }else{
            throw new UserException("User not found!");
        }
    }
}
