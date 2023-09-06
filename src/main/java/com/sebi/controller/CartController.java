package com.sebi.controller;

import com.sebi.exception.ProductException;
import com.sebi.exception.UserException;
import com.sebi.model.Cart;
import com.sebi.model.User;
import com.sebi.repository.CartRepository;
import com.sebi.request.AddItemRequest;
import com.sebi.response.ApiResponse;
import com.sebi.service.CartService;
import com.sebi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/")
    @Operation(description = "find cart by user id")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt )throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    @Operation(description = "add item to cart")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req,
                                                     @RequestHeader("Authorization") String jwt) throws UserException, ProductException{
        User user=userService.findUserProfileByJwt(jwt);

        cartService.addCartItem(user.getId(),req);

        ApiResponse res = new ApiResponse();
        res.setMessage("item added to cart");
        res.setStatus(true);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }




}
