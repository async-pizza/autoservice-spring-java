package org.autoservice.controller;

import jakarta.servlet.http.Cookie;
import org.autoservice.dto.OrderRequest;
import org.autoservice.dto.OrderServiceRequest;
import org.autoservice.dto.OrderUpdateRequest;
import org.autoservice.model.Car;
import org.autoservice.model.Order;
import org.autoservice.model.Service;
import org.autoservice.model.User;
import org.autoservice.security.SessionManager;
import org.autoservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private OrderServiceService orderServiceService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        User client = getUserFromSession(request);
        if (client == null || !client.getRole().equals("CLIENT")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to create orders");
        }

        Car car = carService.findById(orderRequest.carId());
        if (car == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
        }

        User mechanic = userService.findById(orderRequest.mechanicId());
        if (mechanic == null || !mechanic.getRole().equals("MECHANIC")) {
            return ResponseEntity.badRequest().body("Invalid mechanic selection");
        }

        Order order = new Order();
        order.setClient(client);
        order.setMechanic(mechanic);
        order.setCar(car);
        order.setStatus(orderRequest.status());
        orderService.createOrder(order);

        for (OrderServiceRequest serviceRequest : orderRequest.services()) {
            Service service = serviceService.getServiceById(serviceRequest.serviceId());
            if (service == null) {
                continue;
            }
            org.autoservice.model.OrderService orderServiceEntity = new org.autoservice.model.OrderService();
            orderServiceEntity.setOrder(order);
            orderServiceEntity.setService(service);
            orderServiceEntity.setQuantity(serviceRequest.quantity());
            orderServiceService.createOrderService(orderServiceEntity);
        }

        return ResponseEntity.ok("Order created successfully with services!");
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(HttpServletRequest request) {
        User user = getUserFromSession(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Order> orders = orderService.getOrdersByUser(user.getId(), user.getRole());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id, HttpServletRequest request) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @RequestBody OrderUpdateRequest orderUpdateRequest,
                                              HttpServletRequest request) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        // Update the order
        order.setStatus(orderUpdateRequest.status());
        order.setCompletionDate(orderUpdateRequest.completionDate());
        orderService.updateOrder(order);
        return ResponseEntity.ok("Order updated successfully.");
    }

    private User getUserFromSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    return SessionManager.getUserFromSession(sessionId);
                }
            }
        }
        return null;
    }
}