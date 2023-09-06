package com.sebi.service;

import com.sebi.exception.ProductException;
import com.sebi.model.Review;
import com.sebi.model.User;
import com.sebi.request.ReviewRequest;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest req, User user) throws ProductException;

    public List<Review> getAllReview(Long productId);
}
