package org.autoservice.dto;

public record CarRequest (String brand,
        String model,
        int year,
        String licensePlate) {
}