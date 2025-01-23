package com.sebi.service;

import com.sebi.exception.ProductException;
import com.sebi.model.Product;
import com.sebi.model.Rating;
import com.sebi.model.User;
import com.sebi.repository.RatingRepository;
import com.sebi.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImplementation implements RatingService{

    private RatingRepository ratingRepository;
    private ProductService productService;

    public RatingServiceImplementation(RatingRepository ratingRepository, ProductService productService) {
        this.ratingRepository = ratingRepository;
        this.productService = productService;
    }

    @Override
    public Rating createRating(RatingRequest req, User user) throws ProductException {
        Optional<Product> product = productService.findProductById(req.getProductId());
        if(product.isPresent()){
            Rating rating = new Rating();
            rating.setProduct(product.get());
            rating.setUser(user);
            rating.setRating(req.getRating());
            rating.setCreatedAt(LocalDateTime.now());
            return ratingRepository.save(rating);
        }
        throw new ProductException("product not found!");
    }

    @Override
    public List<Rating> getProductsRating(Long productId) {
        return ratingRepository.getAllProductsRating(productId);
    }
}
