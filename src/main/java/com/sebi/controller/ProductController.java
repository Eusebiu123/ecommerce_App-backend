package com.sebi.controller;

import com.sebi.exception.ProductException;
import com.sebi.model.Product;
import com.sebi.response.ApiResponse;
import com.sebi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String category,
                                                                      @RequestParam List<String> color,@RequestParam List<String> size,
                                                                      @RequestParam Integer minPrice, @RequestParam Integer maxPrice,@RequestParam Integer minDiscount,
                                                                      @RequestParam String sort,@RequestParam String stock, @RequestParam Integer pageNumber,
                                                                      @RequestParam Integer pageSize){
        Page<Product> res= productService.getAllProduct(category,color,size,minPrice,maxPrice,minDiscount,sort,stock,pageNumber,pageSize);
        System.out.println("complete products");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException{
            Optional<Product> product = productService.findProductById(productId);
            if(product.isPresent()) {
                return new ResponseEntity<Product>(product.get(), HttpStatus.ACCEPTED);
            }else {
                throw new ProductException("Product not found with this id!");
            }

    }
    @GetMapping("products/all")
    public ResponseEntity<List<Product>> findAllProduct(){
        List<Product> products = productService.findAllProducts();

        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @DeleteMapping("/products/id/{productId}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId) throws ProductException{
        String res = productService.deleteProduct(productId);
        ApiResponse response = new ApiResponse();
        response.setMessage(res);
        response.setStatus(true);
        return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String q){
        List<Product> products = productService.searchProduct(q);

        return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
    }
}
