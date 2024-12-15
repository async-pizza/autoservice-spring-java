package org.autoservice.controller;

import org.autoservice.dto.OrderServiceRequest;
import org.autoservice.model.Car;
import org.autoservice.model.Order;
import org.autoservice.model.Service;
import org.autoservice.service.OrderService;
import org.autoservice.service.OrderServiceService;
import org.autoservice.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/order-services")
public class OrderServiceController {
    @Autowired
    private ServiceService serviceService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderServiceService orderServiceService;

    @PostMapping
    public ResponseEntity<String> addServiceToOrder(@RequestBody OrderServiceRequest orderServiceRequest) {
        Order order = orderService.getOrderById(orderServiceRequest.orderId());

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Service service = serviceService.getServiceById(orderServiceRequest.serviceId());

        if (service == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }

        org.autoservice.model.OrderService orderServiceEntity = new org.autoservice.model.OrderService();
        orderServiceEntity.setOrder(order);
        orderServiceEntity.setService(service);
        orderServiceEntity.setQuantity(orderServiceRequest.quantity());
        orderServiceService.createOrderService(orderServiceEntity);
        return ResponseEntity.ok("Service added to order successfully!");

    }
}
