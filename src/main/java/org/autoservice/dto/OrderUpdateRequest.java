package org.autoservice.dto;

import org.autoservice.model.Order.Status;

import java.time.LocalDateTime;
import java.util.List;

public record OrderUpdateRequest(
        Status status,
        LocalDateTime completionDate) {
}