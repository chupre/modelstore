package com.byllameister.modelstore.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PageableValidator {
    public void validate(Pageable pageable, Set<String> validSortFields) {
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
