package org.autoservice.controller;

import org.autoservice.dto.OrderServiceRequest;
import org.autoservice.dto.ServiceRequest;
import org.autoservice.model.Order;
import org.autoservice.model.Service;
import org.autoservice.service.OrderService;
import org.autoservice.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @PostMapping
    public ResponseEntity<String> createService(@RequestBody ServiceRequest serviceRequest) {
        Service service = new Service();
        service.setName(serviceRequest.name());
        service.setDescription(serviceRequest.description());
        service.setCost(serviceRequest.cost());
        serviceService.createService(service);
        return ResponseEntity.ok("Service created successfully!");
    }

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }
}