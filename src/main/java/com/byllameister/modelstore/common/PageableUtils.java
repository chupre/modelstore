package com.byllameister.modelstore.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public class PageableUtils {
    public PageableUtils() {}

    public static final Set<String> CART_SORT_FIELDS = Set.of("createdAt");
    public static final Set<String> CATEGORY_SORT_FIELDS = Set.of("id", "name");
    public static final Set<String> ORDER_SORT_FIELDS = Set.of("id", "totalPrice", "createdAt");
    public static final Set<String> PRODUCT_SORT_FIELDS = Set.of("id", "title", "price", "categoryId", "createdAt", "likesCount");
    public static final Set<String> SELLER_PRODUCT_SORT_FIELDS = Set.of("id", "title", "price", "categoryId", "createdAt", "likesCount", "sales", "revenue");
    public static final Set<String> SELLER_SORT_FIELDS = Set.of();
    public static final Set<String> USER_SORT_FIELDS = Set.of("id", "username", "email");
    public static final Set<String> COMMENT_SORT_FIELDS = Set.of("id", "comment", "createdAt");

    public static void validate(Pageable pageable, Set<String> validSortFields) {
        for (Sort.Order order : pageable.getSort()) {
            if (!validSortFields.contains(order.getProperty())) {
                throw new IllegalArgumentException("Invalid sort field");
            }
        }

        if (pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size must be less than 100");
        }
    }
}
