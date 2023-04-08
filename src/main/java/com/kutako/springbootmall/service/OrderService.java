package com.kutako.springbootmall.service;

import com.kutako.springbootmall.dto.CreateOrderRequest;
import com.kutako.springbootmall.model.Order;

public interface OrderService {
    Order getOrderById(Integer orderId);
    Integer createOrder(Integer userId , CreateOrderRequest createOrderRequest);
}
