package com.sebi.controller;

import com.sebi.config.JwtService;
import com.sebi.exception.CartItemException;
import com.sebi.exception.OrderException;
import com.sebi.exception.UserException;
import com.sebi.model.Address;
import com.sebi.model.Order;
import com.sebi.model.User;
import com.sebi.repository.UserRepository;
import com.sebi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/")
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress,
                                             @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        String token = jwtService.extractBearer(jwt);
        String username = jwtService.extractUserName(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent())
        {
            User newUser = user.get();
            Order order = orderService.createOrder(newUser,shippingAddress);
            return new ResponseEntity<Order>(order, HttpStatus.CREATED);
        }else {
            throw new UserException("User not found!");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException{
        String token = jwtService.extractBearer(jwt);
        String username = jwtService.extractUserName(token);
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            User newUser = user.get();
            List<Order> orders = orderService.userOrderHistory(newUser.getId());
            return new ResponseEntity<>(orders,HttpStatus.CREATED);
        }else {
            throw new UserException("User not found!");
        }
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Order> findOrderById(
            @PathVariable("Id") Long orderId,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException{
        Order order=orderService.findOrderById(orderId);
        return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }

}
