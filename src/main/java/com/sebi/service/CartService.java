package com.sebi.service;

import com.sebi.exception.ProductException;
import com.sebi.model.Cart;
import com.sebi.model.User;
import com.sebi.request.AddItemRequest;

public interface CartService {
    public Cart createCart(User user);

    public String addCartItem(Long userId, AddItemRequest req) throws ProductException;
    public Cart findUserCart(Long userId);
}
