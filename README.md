# intro

- 教程連結: https://hahow.in/courses/5fe22e7fe810e10fc483dd78/discussions?item=62a61d314b6fc80006a0a0f9
- Spring Boot 版本: 2.3.7.RELEASE
- MySQL 版本: 8.0.22
- 單元測試使用 H2 database
- IntelliJ 版本: Ultimate - 2020.3.3
- 單元測試版本: JUnit 5
- Dao 層使用 Spring JDBC
<br>

# 

- 商品功能 
    - 查詢商品列表
        - 查詢條件
        - 排序
        - 分頁
    - 新增/查詢/修改/刪除 (CRUD) 商品
        - Enum 的使用
        - 格式化返回的時間
- 帳號功能
    - 註冊新帳號
        - 檢查註冊的 email
        - 隱藏返回的密碼
    - 登入
        - 安全儲存密碼的方式
- 訂單功能
    - 創建訂單
        - 檢查 user 是否存在
        - 檢查商品庫存 ,避免使用者輸入錯誤資訊超額購買某些商品下來
    - 查詢訂單列表
        - 透過 userId 去察看這個 user 所購買的所有的訂單數據
- 單元測試、Git 版本控制

