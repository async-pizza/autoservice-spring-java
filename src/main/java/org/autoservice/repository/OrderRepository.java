package org.autoservice.repository;

import org.autoservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMechanicId(Long mechanicId);
    List<Order> findByClientId(Long clientId);
}