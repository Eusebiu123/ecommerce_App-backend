package com.sebi.service;

import com.sebi.exception.CartItemException;
import com.sebi.exception.UserException;
import com.sebi.model.Cart;
import com.sebi.model.CartItem;
import com.sebi.model.Product;
import com.sebi.model.User;
import com.sebi.repository.CartItemRepository;
import com.sebi.repository.CartRepository;
import com.sebi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class CartItemServiceImplementation implements CartItemService{
    private CartItemRepository cartItemRepository;
    private UserService userService;
    private UserRepository userRepository;
    private CartRepository cartRepository;

    @Override
    public CartItem createCartItem(CartItem cartItem) {
//        cartItem.setQuantity(1);///
        cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*cartItem.getQuantity());

        CartItem createdCartItem = cartItemRepository.save(cartItem);
        return createdCartItem;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findCartItemById(id);
        Optional<User> user = userRepository.findById(item.getUserId());
        if(user.isPresent()){
            if(user.get().getId().equals(userId)){
                item.setQuantity(cartItem.getQuantity());
                item.setPrice(item.getQuantity()*item.getProduct().getPrice());
                item.setDiscountedPrice(item.getProduct().getDiscountedPrice()*item.getQuantity());

            }
            return  cartItemRepository.save(item);
        }
       throw new UserException("User not found!");
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
      CartItem cartItem=cartItemRepository.isCartItemExist(cart,product,size,userId);
      return cartItem;
    }
    @Transactional
    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        Optional<User> user = userRepository.findById(cartItem.get().getUserId());
        Cart cart = cartItem.get().getCart();
        if (user.isPresent()) {
            Optional<User> req = userRepository.findById(userId);
            if(req.isPresent()) {
                if (user.get().getId().equals(req.get().getId())) {
                    cart.getCartItems().remove(cartItem.get());
                    cartItem.get().setCart(null);
                    var cartItemsSet = cart.getCartItems();
                    cart.setCartItems(cartItemsSet);
                    cartRepository.save(cart);
                } else {
                        throw new UserException("you can't remove another users item");
                }
            }else{
                    throw new UserException("User with id from jwt not found!");
            }
        }else {
                throw new UserException("User not found!");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = cartItemRepository.findById(cartItemId);

        if(opt.isPresent())
        {
            return opt.get();
        }
        throw new CartItemException("cartItem not found with id: "+cartItemId);
    }
}
