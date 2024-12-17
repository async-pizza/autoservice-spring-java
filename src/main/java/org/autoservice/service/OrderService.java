package org.autoservice.service;

import org.autoservice.model.Order;
import org.autoservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        order.setCreationDate(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(Long userId, String role) {
        if ("MECHANIC".equals(role)) {
            return orderRepository.findByMechanicId(userId);
        } else if ("CLIENT".equals(role)) {
            return orderRepository.findByClientId(userId);
        } else {
            return Collections.emptyList();
        }
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public void updateOrder(Order order) {
        orderRepository.save(order);
    }
}