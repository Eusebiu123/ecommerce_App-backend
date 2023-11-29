package com.sebi;

import com.sebi.model.*;
import com.sebi.repository.CartItemRepository;
import com.sebi.repository.CartRepository;
import com.sebi.repository.ProductRepository;
import com.sebi.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CartItemRepositoryTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private CartItemRepository underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @BeforeEach
    void setUp(){

    }

    @Test
    public void itShouldCheckIfCartItemExists(){
        Set<Size> sizes=new HashSet<>(); List<Rating> ratings=new ArrayList<>(); List<Review> reviews=new ArrayList<>();

        Category category=new Category();
        category.setName("nuj");
        Size a=new Size();
        a.setName("M");
        a.setQuantity(4);
        sizes.add(a);
        Rating rating=new Rating();
        rating.setRating(5);
        ratings.add(rating);
        Review review=new Review();
        review.setReview("fain");
        reviews.add(review);
        LocalDateTime localDate=LocalDateTime.now();
        Product product=new Product(45L,"titlu","desc",45,21,3,4,"idk","white",sizes,"imageurl",ratings,reviews,3,category,localDate);
        Product newProduct = productRepository.save(product);
        User user =new User();
        user.setId(3L);
        user.setFirstName("sebi");
        user.setLastName("pope");
        user.setPassword("beatrice");
        user.setEmail("sebi@1234");
        User newUser = userRepository.save(user);
        Cart cart=new Cart();
        CartItem cartItem= new CartItem();
        cartItem.setId(2L);
//        cartItem.setCart(cart);
        cartItem.setProduct(newProduct);
        cartItem.setSize("M");
        cartItem.setQuantity(5);
        cartItem.setPrice(120);
        cartItem.setDiscountedPrice(65);
        Set<CartItem> carts=new HashSet<>();
        carts.add(cartItem);
        cart.setUser(newUser);
        cart.setId(4L);
        cart.setCartItems(carts);
        Cart newCart = cartRepository.save(cart);
        cartItem.setCart(newCart);



        underTest.save(cartItem);
        CartItem cartExists = underTest.isCartItemExist(newCart,newProduct,"M",newUser.getId());
        //then
        Boolean expected;
        if (cartExists != null)
        {
            expected= true;
        }else{
            expected=false;
        }
        assertThat(expected).isFalse();
    }
}
