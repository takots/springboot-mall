package com.kutako.springbootmall.service;

import com.kutako.springbootmall.dto.CreateOrderRequest;
import com.kutako.springbootmall.dto.OrderQueryParams;
import com.kutako.springbootmall.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Integer countOrder(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderId);
    Integer createOrder(Integer userId , CreateOrderRequest createOrderRequest);
}
