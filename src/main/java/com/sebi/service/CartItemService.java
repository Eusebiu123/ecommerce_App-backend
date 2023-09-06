package com.sebi.service;

import com.sebi.exception.CartItemException;
import com.sebi.exception.UserException;
import com.sebi.model.Cart;
import com.sebi.model.CartItem;
import com.sebi.model.Product;

public interface CartItemService {
    public CartItem createCartItem(CartItem cartItem);
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

    public CartItem isCartItemExist(Cart cart, Product product,String size,Long userId);
    public void removeCartItem(Long userId,Long cartItemId) throws CartItemException,UserException;

    public CartItem findCartItemById(Long cartItemId) throws CartItemException;


}
