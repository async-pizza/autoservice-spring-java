package org.autoservice.service;

import org.autoservice.repository.OrderServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceService {
    @Autowired
    private OrderServiceRepository orderServiceRepository;

    public org.autoservice.model.OrderService createOrderService(org.autoservice.model.OrderService orderService) {
        return orderServiceRepository.save(orderService);
    }
}
