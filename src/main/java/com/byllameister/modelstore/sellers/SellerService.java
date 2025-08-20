package com.byllameister.modelstore.sellers;

import com.byllameister.modelstore.common.PageableUtils;
import com.byllameister.modelstore.users.Role;
import com.byllameister.modelstore.users.User;
import com.byllameister.modelstore.users.UserNotFoundException;
import com.byllameister.modelstore.users.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SellerService {
    private final UserRepository userRepository;
    private final SellerMapper sellerMapper;
    private final SellerRepository sellerRepository;

    public SellerResponse becomeSeller(CreateSellerRequest request) {
        var seller = createSeller(request);

        var user = userRepository.findById(request.getUserId()).
                orElseThrow(UserNotFoundException::new);

        if (!user.getRole().equals(Role.ADMIN)) {
            user.setRole(Role.SELLER);
        }

        userRepository.save(user);

        return seller;
    }

    public SellerResponse createSeller(CreateSellerRequest request) {
        if (sellerRepository.existsByUserId(request.getUserId())) {
            throw new SellerAlreadyExistsException();
        }

        var seller = sellerMapper.toEntity(request);
        sellerRepository.save(seller);
        return sellerMapper.toDto(seller);
    }

    public SellerWithStatsResponse getCurrentSeller() {
        return sellerRepository.findSellerWithStats(User.getCurrentUserId())
                .orElseThrow(SellerNotFoundException::new);
    }

    public Page<SellerWithStatsResponse> getAllSellers(Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.SELLER_SORT_FIELDS);
        return sellerRepository.findSellersWithStats(pageable);
    }

    public SellerWithStatsResponse getSeller(Long id) {
        return sellerRepository.findSellerWithStats(id).orElseThrow(SellerNotFoundException::new);
    }

    public SellerResponse updateSeller(Long id, @Valid UpdateSellerRequest request) {
        var seller = sellerRepository.findById(id).orElseThrow(SellerNotFoundException::new);
        sellerMapper.update(request, seller);
        sellerRepository.save(seller);
        return sellerMapper.toDto(seller);
    }

    public void deleteSeller(Long id) {
        var seller = sellerRepository.findById(id).orElseThrow(SellerNotFoundException::new);
        var user = userRepository.findById(seller.getUser().getId()).orElseThrow(UserNotFoundException::new);
        sellerRepository.delete(seller);
        user.setRole(Role.BUYER);
        userRepository.save(user);
    }
}
