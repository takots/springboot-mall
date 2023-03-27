package com.kutako.springbootmall.dao;

import com.kutako.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
}
