package org.autoservice.service;

import org.autoservice.model.Order;
import org.autoservice.model.User;
import org.autoservice.model.Car;
import org.autoservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    public Order createOrder(Order order) {
        // Set creation date
        order.setCreationDate(LocalDateTime.now());
        // Additional business logic if needed
        return orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
}