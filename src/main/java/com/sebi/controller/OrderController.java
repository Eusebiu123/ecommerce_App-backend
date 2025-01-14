package com.sebi.controller;

import com.sebi.exception.CartItemException;
import com.sebi.exception.OrderException;
import com.sebi.exception.UserException;
import com.sebi.model.Address;
import com.sebi.model.Order;
import com.sebi.model.User;
import com.sebi.service.OrderService;
import com.sebi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @PostMapping("/")
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress,
                                             @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        User user=userService.findUserProfileByJwt(jwt);
        Order order = orderService.createOrder(user,shippingAddress);

        System.out.println("order "+order);
        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.userOrderHistory(user.getId());
        return new ResponseEntity<>(orders,HttpStatus.CREATED);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Order> findOrderById(
            @PathVariable("Id") Long orderId,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException{
        User user = userService.findUserProfileByJwt(jwt);
        Order order=orderService.findOrderById(orderId);

        return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }

}
