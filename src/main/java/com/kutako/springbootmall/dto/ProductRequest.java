package com.kutako.springbootmall.dto;

import com.kutako.springbootmall.constant.ProductCategory;

import javax.validation.constraints.NotNull;
import java.util.Date;

// 這個 class 用途是去接前端傳過來的參數
public class ProductRequest {

//   資料庫自動生成 ,不需要前端傳過來的
//    private  Integer productId;
    @NotNull
    private String productName;
    @NotNull
    private ProductCategory category;
    @NotNull
    private String imageUrl;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stock;
    // 不是每一個商品都會有詳細的說明 ,所以允許為空值
    private String description;
//    時間到時候讓資料庫自動產生
//    private Date createDate;
//    private Date lastModifiedDate;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
