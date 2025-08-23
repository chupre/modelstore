package com.byllameister.modelstore.payments;

import com.byllameister.modelstore.common.PageableUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PayoutService {
    private final PayoutRepository payoutRepository;
    private final PayoutMapper payoutMapper;

    public Page<PayoutDto> getSellerPayouts(Long userId, Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.PAYOUT_SORT_FIELDS);
        var payouts = payoutRepository.findAllBySellerUserId(userId, pageable);
        return payouts.map(payoutMapper::toDto);
    }
}
