package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.admin.carts.CartExposedResponse;
import com.byllameister.modelstore.admin.carts.CreateCartRequest;
import com.byllameister.modelstore.admin.carts.UpdateCartItemRequest;
import com.byllameister.modelstore.admin.carts.UpdateCartRequest;
import com.byllameister.modelstore.common.PageableValidator;
import com.byllameister.modelstore.products.ProductNotFoundException;
import com.byllameister.modelstore.products.ProductRepository;
import com.byllameister.modelstore.users.UserNotFoundException;
import com.byllameister.modelstore.users.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private final PageableValidator pageableValidator;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    private final Set<String> VALID_SORT_FIELDS = Set.of("createdAt");
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Page<CartExposedResponse> getCarts(Pageable pageable) {
        pageableValidator.validate(pageable, VALID_SORT_FIELDS);
        var carts = cartRepository.findAll(pageable);
        return carts.map(cartMapper::toCartExposedResponse);
    }

    public CartDto getCart(UUID id) {
        var cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new);
        return cartMapper.toDto(cart);
    }

    public CartExposedResponse getCartByUserId(Long userId) {
        var user =  userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        var cart = cartRepository.findByUser(user).orElseThrow(CartNotFoundException::new);
        return cartMapper.toCartExposedResponse(cart);
    }

    public CartDto createCart(CreateCartRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFoundException::new);

        if (cartRepository.existsByUser(user))
            throw new CartAlreadyExistException();

        var cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartDto updateCart(UUID id, UpdateCartRequest request) {
        var cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new);
        cartMapper.update(request, cart);
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public void deleteCart(UUID id) {
        var cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new);
        cartRepository.delete(cart);
    }

    public CartItemDto addItem(UUID id, @Valid AddCartItemRequest request) {
        var cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new);
        var product = productRepository.findById(request.getProductId()).orElseThrow(ProductNotFoundException::new);

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartItemDto updateItem(UUID id, Long productId, UpdateCartItemRequest request) {
        var cart = cartRepository.findById(id)
                .orElseThrow(CartNotFoundException::new);
        var product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        var cartItem = cart.getItem(product);
        if (cartItem == null) {
            throw new ProductNotFoundException();
        }

        var newProduct = productRepository.findById(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        if (cart.getItem(newProduct) != null) {
            throw new CartItemAlreadyExists();
        }


        cartItem.setProduct(newProduct);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public Long getUserId(UUID id) {
        var cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new);
        return cart.getUser().getId();
    }

    public void deleteCartItem(UUID id, Long productId) {
        var cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new);
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        cart.deleteItem(product);
        cartRepository.save(cart);
    }

    public void deleteAllCartItems(UUID id) {
        var cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new);
        cart.clear();
        cartRepository.save(cart);
    }
}
