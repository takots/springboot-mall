package com.kutako.springbootmall.dao;

import com.kutako.springbootmall.model.Order;
import com.kutako.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Order getOrderById(Integer orderId);
    List<OrderItem> getOrderItemByOrderId(Integer orderId);
    Integer createOrder(Integer userId ,Integer totalAmount);
    void createOrderItems(Integer orderId ,List<OrderItem> orderItemList);
}
