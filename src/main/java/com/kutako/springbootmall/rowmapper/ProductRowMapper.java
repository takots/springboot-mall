package com.kutako.springbootmall.rowmapper;

import com.kutako.springbootmall.constant.ProductCategory;
import com.kutako.springbootmall.model.Product;
import com.mysql.cj.protocol.Resultset;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int i) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));

        // 將 String 類型 轉換成 enum
        String categoryStr = rs.getString("category");
        ProductCategory category = ProductCategory.valueOf(categoryStr);
        product.setCategory(category);

//        product.setCategory(ProductCategory.valueOf(rs.getString("category")));
        product.setImageUrl(rs.getString("image_url"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));
        product.setCreateDate(rs.getTimestamp("created_date"));
        product.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
        return product;
    }
}
