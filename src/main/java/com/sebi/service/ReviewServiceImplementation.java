package com.sebi.service;

import com.sebi.exception.ProductException;
import com.sebi.model.Product;
import com.sebi.model.Review;
import com.sebi.model.User;
import com.sebi.repository.ProductRepository;
import com.sebi.repository.ReviewRepository;
import com.sebi.request.ReviewRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReviewServiceImplementation implements ReviewService{

    private ReviewRepository reviewRepository;
    private ProductService productService;

    private ProductRepository productRepository;

    public ReviewServiceImplementation(ReviewRepository reviewRepository, ProductService productService, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.productRepository=productRepository;
    }

    @Override
    public Review createReview(ReviewRequest req, User user) throws ProductException {
        Optional<Product> product = productService.findProductById(req.getProductId());
        if(product.isPresent()){
            Review review = new Review();
            review.setUser(user);
            review.setProduct(product.get());
            review.setReview(req.getReview());
            review.setCreatedAt(LocalDateTime.now());

            return reviewRepository.save(review);
        }
       throw new ProductException("Product not found!");
    }

    @Override
    public List<Review> getAllReview(Long productId) {
        return reviewRepository.getAllProductsReview(productId);
    }
}
