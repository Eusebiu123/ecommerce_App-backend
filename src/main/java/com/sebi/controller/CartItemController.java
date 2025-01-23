package com.sebi.controller;

import com.sebi.config.JwtService;
import com.sebi.exception.CartItemException;
import com.sebi.exception.UserException;
import com.sebi.model.CartItem;
import com.sebi.model.User;
import com.sebi.repository.CartItemRepository;
import com.sebi.repository.UserRepository;
import com.sebi.response.ApiResponse;
import com.sebi.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @DeleteMapping("/{itemId}")
    @Operation(description = "Remove Cart Item From Cart")
    public ResponseEntity<ApiResponse> DeleteOrderHandler(@PathVariable Long itemId,
                                                          @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        String token = jwtService.extractBearer(jwt);
        String username = jwtService.extractUserName(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            Long userId = user.get().getId();
            System.out.println("asta e userul: "+userId);
            cartItemService.removeCartItem(userId,itemId);
            ApiResponse res = new ApiResponse();
            res.setMessage("item deleted successfully");
            res.setStatus(true);

            return new ResponseEntity<>(res, HttpStatus.OK);
        }else {
            throw new UserException("User not found!");
        }
    }

    @PutMapping("/{cartItemId}")
    @Operation(description = "Update Item To Cart")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem,
                                                   @PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws UserException,CartItemException{
        String token = jwtService.extractBearer(jwt);
        String username = jwtService.extractUserName(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            User newUser = user.get();
            CartItem updatedCartItem = cartItemService.updateCartItem(newUser.getId(),cartItemId,cartItem);
            return new ResponseEntity<>(updatedCartItem,HttpStatus.OK);
        }else {
            throw new UserException("User not found!");
        }
    }
}
