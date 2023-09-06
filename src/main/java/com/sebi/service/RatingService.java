package com.sebi.service;


import com.sebi.exception.ProductException;
import com.sebi.model.Rating;
import com.sebi.model.User;
import com.sebi.request.RatingRequest;

import java.util.List;

public interface RatingService {
    public Rating createRating(RatingRequest req, User user) throws ProductException;

    public List<Rating> getProductsRating(Long productId);
}
