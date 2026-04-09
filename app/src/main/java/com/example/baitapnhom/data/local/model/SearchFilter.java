package com.example.baitapnhom.data.local.model;

public class SearchFilter {

    public enum SortOrder {
        NEWEST,       // Mới nhất
        PRICE_ASC,    // Giá tăng dần
        PRICE_DESC,   // Giá giảm dần
        NAME_ASC      // Tên A-Z
    }


    public final String keyword;

    public final int categoryId;

    public final double minPrice;
    public final double maxPrice;

    public final SortOrder sortOrder;

    public SearchFilter(String keyword, int categoryId,
                        double minPrice, double maxPrice,
                        SortOrder sortOrder) {
        this.keyword   = keyword   == null ? "" : keyword.trim();
        this.categoryId = categoryId;
        this.minPrice  = minPrice;
        this.maxPrice  = maxPrice;
        this.sortOrder = sortOrder == null ? SortOrder.NEWEST : sortOrder;
    }

    public static SearchFilter defaultFilter() {
        return new SearchFilter("", 0, -1, -1, SortOrder.NEWEST);
    }

    public SearchFilter withKeyword(String keyword) {
        return new SearchFilter(keyword, categoryId, minPrice, maxPrice, sortOrder);
    }

    public SearchFilter withCategory(int categoryId) {
        return new SearchFilter(keyword, categoryId, minPrice, maxPrice, sortOrder);
    }

    public SearchFilter withPriceRange(double min, double max) {
        return new SearchFilter(keyword, categoryId, min, max, sortOrder);
    }

    public SearchFilter withSortOrder(SortOrder order) {
        return new SearchFilter(keyword, categoryId, minPrice, maxPrice, order);
    }

    public boolean hasActiveFilter() {
        return !keyword.isEmpty() || categoryId != 0
                || minPrice >= 0 || maxPrice >= 0;
    }
}