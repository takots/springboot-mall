package com.kutako.springbootmall.controller;

import com.kutako.springbootmall.constant.ProductCategory;
import com.kutako.springbootmall.dto.ProductQueryParams;
import com.kutako.springbootmall.dto.ProductRequest;
import com.kutako.springbootmall.model.Product;
import com.kutako.springbootmall.service.ProductService;
import com.kutako.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(
            // 查詢條件 Filtering
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,

            // 排序 Sorting
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,

            // 分頁 Pagination
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){

        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setProductCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        // 取得 product list 商品列表數據
        List<Product> productList = productService.getProducts(productQueryParams);

        // 取得 product 總數
        Integer total = productService.countProduct(productQueryParams);

        // 分頁
        Page<Product> page = new Page<Product>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList); // 查詢到的商品數據的值

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        // @PathVariable 從 url 路徑傳進來的
        Product product = productService.getProductById(productId);
        if(product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
    // @RequestBody 接著前端傳過來的 json 參數
    // 有加上驗證請求參數的話 一定要記得加上 @Valid ,這樣 ProductRequest 的 @NotNull 才會真的生效
        // 新增一筆回傳 id
        Integer productId = productService.createProduct(productRequest);
        // 查詢這筆的相關資訊
        Product product = productService.getProductById(productId);
        // 狀態碼 201 ,並將資訊放到 body 回傳
        System.out.println("product> " + product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId
                                                ,@RequestBody @Valid ProductRequest productRequest){
        // 檢查 product 商品是否存在
        Product product = productService.getProductById(productId);
        // 不存在回傳404
        if(product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // 修改商品的數據
        productService.updateProduct(productId ,productRequest);
        Product updateProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable Integer productId){
        // 不需要回傳 404 給前端 -> 屬於不正確的設計
        productService.deleteProductById(productId);
        // 204 NO_CONTENT
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
