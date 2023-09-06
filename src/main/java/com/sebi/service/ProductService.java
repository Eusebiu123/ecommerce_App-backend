package com.sebi.service;

import com.sebi.exception.ProductException;
import com.sebi.model.Product;
import com.sebi.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest req);

    public String deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long productId,Product req) throws ProductException;

    public Product findProductById(Long id) throws ProductException;

    public List<Product> findProductByCategory(String category);

    public Page<Product> getAllProduct(String category,List<String> colors,List<String> sizes,Integer minPrice,Integer maxPrice,Integer minDiscount,String sort,String stock,Integer pageNumber,Integer pagesSize);


    public List<Product> searchProduct(String q);

    public List<Product> findAllProducts();
}
