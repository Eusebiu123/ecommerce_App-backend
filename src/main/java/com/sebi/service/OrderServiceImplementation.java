package com.sebi.service;

import com.sebi.exception.CartItemException;
import com.sebi.exception.OrderException;
import com.sebi.exception.UserException;
import com.sebi.model.*;
import com.sebi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImplementation  implements OrderService{
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;
    private CartItemService cartItemService;
    private CartService cartService;
    private ProductService productService;
    private OrderRepository orderRepository;
    private AddressRepository addressRepository;
    private UserRepository userRepository;
    private OrderItemService orderItemService;
    private OrderItemRepository orderItemRepository;

    public OrderServiceImplementation(CartService cartService, OrderRepository orderRepository, AddressRepository addressRepository, UserRepository userRepository, OrderItemService orderItemService, OrderItemRepository orderItemRepository,ProductRepository productRepository,CartItemRepository cartItemRepository,CartRepository cartRepository,CartItemService cartItemService) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.cartItemRepository=cartItemRepository;
        this.cartRepository=cartRepository;
        this.cartItemService=cartItemService;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress) throws CartItemException, UserException {
        shippingAddress.setUser(user);
        Address address = addressRepository.save(shippingAddress);
        user.getAddress().add(address);
        userRepository.save(user);

        Cart cart = cartService.findUserCart(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem item : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();

            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());

            OrderItem createdOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(createdOrderItem);

            //update product
            String sizeToBeUpdated = item.getSize();
            int quantityOfSize = item.getQuantity();
            Product productToBeUpdated = item.getProduct();
            Set<Size> sizes = new HashSet<>();
            sizes = productToBeUpdated.getSizes();
            Set<Size> newSizes = new HashSet<>();
            for( Size i : sizes){
                if(Objects.equals(i.getName(), sizeToBeUpdated)){
                      int newQuantity = i.getQuantity()-quantityOfSize;
                      if(newQuantity>0) {
                          i.setQuantity(newQuantity);
                          newSizes.add(i);
                      }
                }
                else{
                    newSizes.add(i);
                }
            }
            productToBeUpdated.setSizes(newSizes);
            productRepository.save(productToBeUpdated);
        }
        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscounte(cart.getDiscounte());
        createdOrder.setTotalItem(cart.getTotalItem());

        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.getPaymentDetails().setStatus("PENDING");
        createdOrder.setCreateAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(createdOrder);

        for(OrderItem item: orderItems){
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        //delete user items from cart
        Long userId = user.getId();
        cartRepository.deleteById(userId);
        cartService.createCart(user);

        return savedOrder;

    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> opt = orderRepository.findById(orderId);

        if(opt.isPresent()){
            return opt.get();
        }throw new OrderException("order not exist with id "+orderId);
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
       List<Order> orders = orderRepository.getUsersOrders(userId);
       return orders;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("PLACED");
        order.getPaymentDetails().setStatus("COMPLETED");
        return order;
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CONFIRMED");

        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("DELIVERED");
        return orderRepository.save(order);
    }

    @Override
    public Order canceledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CANCELLED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order= findOrderById(orderId);
        orderRepository.deleteById(orderId);
    }
}
