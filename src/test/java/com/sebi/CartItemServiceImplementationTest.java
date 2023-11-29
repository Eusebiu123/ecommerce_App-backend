package com.sebi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sebi.model.Cart;
import com.sebi.model.CartItem;
import com.sebi.model.Product;
import com.sebi.repository.CartItemRepository;
import com.sebi.repository.CartRepository;
import com.sebi.service.CartItemServiceImplementation;
import com.sebi.service.UserService;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceImplementationTest {
    @Mock
    private CartItemRepository cartItemRepository;
    private UserService userService;
    private CartItemServiceImplementation underTest;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    void setUp(){
        underTest=new CartItemServiceImplementation(cartItemRepository,userService,cartRepository);
    }
    @Test
    void createCartItemTest(){
       Long id=3L;
       Cart cart=new Cart();
       Product product=new Product();
       String size="M";
       int quantity=5;
       Integer price=120;
       Integer discountedPrice=123;
       Long userId=1L;
       CartItem cartItem= new CartItem(id,cart,product,size,quantity,price,discountedPrice,userId);

       underTest.createCartItem(cartItem);

        ArgumentCaptor<CartItem> cartItemArgumentCaptor = ArgumentCaptor.forClass(CartItem.class);
        verify(cartItemRepository).save(cartItemArgumentCaptor.capture());
        CartItem capturedCartItem = cartItemArgumentCaptor.getValue();
        assertThat(capturedCartItem).isEqualTo(cartItem);
    }
}
