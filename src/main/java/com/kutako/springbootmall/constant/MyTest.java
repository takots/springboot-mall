package com.kutako.springbootmall.constant;

public class MyTest {
    public static void main(String[] args) {
        ProductCategory category=ProductCategory.FOOD;
        String s = category.name();
        System.out.println(s); // FOOD

        // String 類型如何去轉換成 enum
        String s2="CAR";
        ProductCategory category2 = ProductCategory.valueOf(s2);
    }
}
