package com.sebi.controller;

import com.sebi.exception.CartItemException;
import com.sebi.exception.OrderException;
import com.sebi.exception.UserException;
import com.sebi.model.CartItem;
import com.sebi.model.User;
import com.sebi.repository.CartItemRepository;
import com.sebi.response.ApiResponse;
import com.sebi.service.CartItemService;
import com.sebi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemRepository cartItemRepository;


    @DeleteMapping("/{itemId}")
    @Operation(description = "Remove Cart Item From Cart")
    public ResponseEntity<ApiResponse> DeleteOrderHandler(@PathVariable Long itemId,
                                                          @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        Long userId = user.getId();
        cartItemService.removeCartItem(userId,itemId);
        ApiResponse res = new ApiResponse();
        res.setMessage("item deleted successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    @Operation(description = "Update Item To Cart")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem,
                                                   @PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws UserException,CartItemException{
        User user = userService.findUserProfileByJwt(jwt);
        CartItem updatedCartItem = cartItemService.updateCartItem(user.getId(),cartItemId,cartItem);

        return new ResponseEntity<>(updatedCartItem,HttpStatus.OK);
    }
}
