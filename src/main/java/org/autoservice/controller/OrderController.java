package org.autoservice.controller;

import jakarta.servlet.http.Cookie;
import org.autoservice.dto.OrderRequest;
import org.autoservice.model.Car;
import org.autoservice.model.Order;
import org.autoservice.model.User;
import org.autoservice.security.SessionManager;
import org.autoservice.service.OrderService;
import org.autoservice.service.UserService;
import org.autoservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        User mechanic = userService.findById(orderRequest.mechanicId());
        if (mechanic == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mechanic not found");
        }
        Car car = carService.findById(orderRequest.carId());
        if (car == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
        }
        Order order = new Order();
        order.setClient(getUserFromSession(request));
        order.setMechanic(mechanic);
        order.setCar(car);
        order.setStatus(orderRequest.status());
        orderService.createOrder(order);
        return ResponseEntity.ok("Order created successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id, HttpServletRequest request) {
        // Authenticate user
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
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