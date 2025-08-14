package com.byllameister.modelstore.orders;

import com.byllameister.modelstore.common.PageableUtils;
import com.byllameister.modelstore.users.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    public Page<OrderDto> getCurrentUserOrders(Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.ORDER_SORT_FIELDS);
        var orders = orderRepository.findByCustomerId(User.getCurrentUserId(), pageable);
        return orders.map(orderMapper::toDto);
    }

    public Page<OrderDto> getAllOrders(Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.ORDER_SORT_FIELDS);
        var orders = orderRepository.findAll(pageable);
        return orders.map(orderMapper::toDto);
    }

    public OrderDto getOrder(Long id) {
        var order =  orderRepository.findById(id).
                orElseThrow(OrderNotFoundException::new);

        return orderMapper.toDto(order);
    }
}
