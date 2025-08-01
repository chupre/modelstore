package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.common.PageableValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private final PageableValidator pageableValidator;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    private final Set<String> VALID_SORT_FIELDS = Set.of("createdAt");

    public List<CartDto> getCarts(Pageable pageable) {
        pageableValidator.validate(pageable, VALID_SORT_FIELDS);
        var carts = cartRepository.findAll(pageable).getContent();
        return cartMapper.toDtos(carts);
    }

    public CartDto getCart(UUID id) {
        var cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new);
        return cartMapper.toDto(cart);
    }
}
