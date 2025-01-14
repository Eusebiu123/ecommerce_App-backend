package com.sebi.service;

import com.sebi.exception.CartItemException;
import com.sebi.exception.OrderException;
import com.sebi.exception.UserException;
import com.sebi.model.Address;
import com.sebi.model.Order;
import com.sebi.model.User;

import java.util.List;

public interface OrderService {
    public Order createOrder(User user, Address shippingAddress) throws CartItemException, UserException;
    public Order findOrderById(Long orderId) throws OrderException;

    public List<Order> userOrderHistory(Long userId);

    public Order placedOrder(Long orderId) throws OrderException;

    public Order confirmedOrder(Long orderId) throws OrderException;
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliveredOrder(Long orderId) throws OrderException;
    public Order canceledOrder(Long orderId) throws OrderException;
    public List<Order> getAllOrders();
    public void deleteOrder(Long orderId) throws OrderException;
}
