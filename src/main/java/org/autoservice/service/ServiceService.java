package org.autoservice.service;

import org.autoservice.model.Service;
import org.autoservice.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }
}