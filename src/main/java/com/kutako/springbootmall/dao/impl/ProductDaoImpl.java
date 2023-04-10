package com.kutako.springbootmall.dao.impl;

import com.kutako.springbootmall.constant.ProductCategory;
import com.kutako.springbootmall.dao.ProductDao;
import com.kutako.springbootmall.dto.ProductQueryParams;
import com.kutako.springbootmall.dto.ProductRequest;
import com.kutako.springbootmall.model.Product;
import com.kutako.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Override
    public Product getProductById(Integer productId) {
        String SqlStr = "SELECT product_id, product_name, category, image_url, price, stock, description," +
                " created_date, last_modified_date FROM product WHERE product_id= :productId";
        System.out.println("SqlStr> " + SqlStr);
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        List<Product> productList = namedParameterJdbcTemplate.query(SqlStr, map, new ProductRowMapper());
        if(productList.size()>0){
            return productList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String SqlStr="INSERT INTO product (product_name, category, image_url, price, stock, description,created_date, last_modified_date)"
                +"VALUES (:productName, :category, :imageUrl, :price, :stock, :description ,:createdDate, :lastModifiedDate)";
        Map<String ,Object> map = new HashMap<>();
        // 前端傳過來的 productRequest 的參數一個一個取出來放到 map 裡面
        map.put("productName" ,productRequest.getProductName());
        // 因為是 Enum
        map.put("category",productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        Date now = new Date();
        map.put("createdDate",now);
        map.put("lastModifiedDate",now);

        // 使用 keyHolder 去儲存資料庫中自動生成的 id
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SqlStr ,new MapSqlParameterSource(map) ,keyHolder);
        int productId = keyHolder.getKey().intValue();
        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String SqlStr="UPDATE product SET product_name=:productName , category=:category, image_url=:imageUrl"
                +", price=:price, stock=:stock, description=:description, last_modified_date=:lastModifiedDate"
                +" WHERE product_id=:productId";
        Map<String ,Object> map = new HashMap<>();
        map.put("productId", productId);
        // 前端傳過來的 productRequest 的參數一個一個取出來放到 map 裡面
        map.put("productName" ,productRequest.getProductName());
        // 因為是 Enum
        map.put("category",productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        map.put("lastModifiedDate",new Date());

        namedParameterJdbcTemplate.update(SqlStr ,map);
    }

    @Override
    public void deleteProductById(Integer productId) {
        String SqlStr="DELETE FROM product WHERE product_id=:productId";
        Map<String ,Object> map = new HashMap<>();
        map.put("productId", productId);
        namedParameterJdbcTemplate.update(SqlStr ,map);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        // 全部查詢出來
        String SqlStr = "SELECT product_id, product_name, category, image_url, price, stock, description,"
                      + " created_date, last_modified_date FROM product WHERE 1=1 ";
        Map<String ,Object> map = new HashMap<>();

        String orderBy = productQueryParams.getOrderBy();
        String sort = productQueryParams.getSort();
        Integer limit = productQueryParams.getLimit();
        Integer offset = productQueryParams.getOffset();

        // 查詢條件 Filtering
        SqlStr = addFilteringSql(SqlStr ,map ,productQueryParams);

        // 排序 Sorting
        SqlStr += " ORDER BY " + orderBy + " " + sort;

        // 分頁 Pagination
        SqlStr += " LIMIT :limit OFFSET :offset";
        map.put("limit", limit);
        map.put("offset", offset);

        List<Product> productList = namedParameterJdbcTemplate.query(SqlStr, map, new ProductRowMapper());
        return productList;
    }

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String SqlStr = "SELECT COUNT(*) FROM product WHERE 1=1";
        Map<String ,Object> map = new HashMap<>();
        // 查詢條件 Filtering
        SqlStr = addFilteringSql(SqlStr ,map ,productQueryParams);
        // 通常用在取 count 值的時候 , Integer.class 表示要將 count 的值轉換成 Integer 類型
        Integer total = namedParameterJdbcTemplate.queryForObject(SqlStr ,map ,Integer.class);

        return total;
    }

    @Override
    public void updateStock(Integer productId, Integer stock) {
        String SqlStr=" UPDATE product SET stock = :stock ,last_modified_date = :lastModifiedDate "
                     +" WHERE product_id = :productId";
        Map<String ,Object> map = new HashMap<>();
        map.put("productId" ,productId);
        map.put("stock" ,stock);
        map.put("lastModifiedDate" ,new Date());
        namedParameterJdbcTemplate.update(SqlStr ,map);
    }

    private String addFilteringSql(String SqlStr ,Map<String ,Object> map ,ProductQueryParams productQueryParams){
        ProductCategory category = productQueryParams.getProductCategory();
        String search = productQueryParams.getSearch();
        // 查詢條件 Filtering
        if(category != null){
            SqlStr += " AND category=:category";
            map.put("category",category.name());
        }

        if(search != null){
            SqlStr += " AND product_name LIKE :search";
            map.put("search","%"+search+"%");
        }
        return SqlStr;
    }
}
