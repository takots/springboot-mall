package com.kutako.springbootmall.service.Impl;

import com.kutako.springbootmall.dao.OrderDao;
import com.kutako.springbootmall.dao.ProductDao;
import com.kutako.springbootmall.dto.BuyItem;
import com.kutako.springbootmall.dto.CreateOrderRequest;
import com.kutako.springbootmall.model.Order;
import com.kutako.springbootmall.model.OrderItem;
import com.kutako.springbootmall.model.Product;
import com.kutako.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);
        List<OrderItem> orderItemList = orderDao.getOrderItemByOrderId(orderId);
        // 合併數據
        // 一張訂單會有多個訂單明細
        order.setOrderItemList(orderItemList);
        return order;
    }

    @Transactional // 同時發生 或 同時不發生
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){
            // 因為要查價格 ,記得注入 bean
            Product product = productDao.getProductById(buyItem.getProductId());

            // 計算總價
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount += amount;

            // 轉換 BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);
            orderItemList.add(orderItem);
        }

        Integer orderId = orderDao.createOrder(userId ,totalAmount);
        orderDao.createOrderItems(orderId ,orderItemList);
        return orderId;
    }
}
