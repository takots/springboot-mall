package com.kutako.springbootmall.service.Impl;

import com.kutako.springbootmall.dao.OrderDao;
import com.kutako.springbootmall.dao.ProductDao;
import com.kutako.springbootmall.dao.UserDao;
import com.kutako.springbootmall.dto.BuyItem;
import com.kutako.springbootmall.dto.CreateOrderRequest;
import com.kutako.springbootmall.dto.OrderQueryParams;
import com.kutako.springbootmall.model.Order;
import com.kutako.springbootmall.model.OrderItem;
import com.kutako.springbootmall.model.Product;
import com.kutako.springbootmall.model.User;
import com.kutako.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        // 先取得 order
        List<Order> orderList = orderDao.getOrders(orderQueryParams);
        // 再去取得 order item
        for(Order order : orderList){
            List<OrderItem> orderItemList = orderDao.getOrderItemByOrderId(order.getOrderId());
            // 將 order item 放到每個 order 底下
            // 將 order 跟 order item 這兩個資訊結合再一起
            order.setOrderItemList(orderItemList);
        }
        return orderList;
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

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
        // 檢查 user 是否存在
        // 查詢資料庫中的 id 是否存在
        User user = userDao.getUserById(userId);
        if(user == null){
            log.warn("該 userId {} 不存在",userId);
            // 噴出 exception 中斷這次前端的請求
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){
            // 因為要查價格 ,記得注入 bean
            Product product = productDao.getProductById(buyItem.getProductId());

            // 庫存是否足夠 ,避免商品超賣 ,庫存數量是否足夠
            if(product == null){
                log.warn("商品 {} 不存在",buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if(product.getStock() < buyItem.getQuantity()){
                log.warn("商品 {} 庫存數量不足 ,無法購買 ,剩餘庫存數量 {} ,欲購買數量 {}",
                        buyItem.getProductId() ,product.getStock() ,buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 扣除商品的庫存數量
            productDao.updateStock(product.getProductId() ,product.getStock()-buyItem.getQuantity());

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
