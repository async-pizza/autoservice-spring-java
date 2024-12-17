package org.autoservice.dto;

import org.autoservice.model.Order.Status;

import java.util.List;

public record OrderRequest(
        Long mechanicId,
        Long carId,
        Status status,
        List<OrderServiceRequest> services) {
}