package com.kutako.springbootmall.service;

import com.kutako.springbootmall.constant.ProductCategory;
import com.kutako.springbootmall.dto.ProductQueryParams;
import com.kutako.springbootmall.dto.ProductRequest;
import com.kutako.springbootmall.model.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId ,ProductRequest productRequest);
    void deleteProductById(Integer productId);
    List<Product> getProducts(ProductQueryParams productQueryParams);
    Integer countProduct(ProductQueryParams productQueryParams);
}
