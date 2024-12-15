package org.autoservice.dto;

import org.autoservice.model.Order.Status;

public record OrderRequest(
        Long mechanicId,
        Long carId,
        Status status) {
}