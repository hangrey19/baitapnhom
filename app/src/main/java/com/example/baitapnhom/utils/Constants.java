package com.example.baitapnhom.utils;

public class Constants {
    public static final String ARG_PRODUCT_ID  = "product_id";
    public static final String ARG_CATEGORY_ID = "category_id";
    public static final String ARG_ORDER_ID    = "order_id";

    public static final String SORT_NEWEST     = "newest";
    public static final String SORT_POPULAR    = "popular";
    public static final String SORT_PRICE_ASC  = "price_asc";
    public static final String SORT_PRICE_DESC = "price_desc";
    public static final String SORT_RATING     = "rating";

    public static final String[] SORT_OPTIONS = { "Mới nhất", "Phổ biến", "Giá thấp", "Giá cao", "Đánh giá cao" };
    public static final String[] SORT_KEYS    = { SORT_NEWEST, SORT_POPULAR, SORT_PRICE_ASC, SORT_PRICE_DESC, SORT_RATING };

    public static final int MAX_QUANTITY = 99;
    public static final int MIN_QUANTITY = 1;
}