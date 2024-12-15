package org.autoservice.dto;

public record OrderServiceRequest(Long orderId, Long serviceId, int quantity) {
}