package com.sebi.repository;

import com.sebi.model.Cart;
import com.sebi.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.id =:userId")
    public Cart findByUserId(@Param("userId") Long userId);


    void deleteAllById(Long id);

}
