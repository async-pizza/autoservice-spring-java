package org.autoservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "mechanic_id")
    private User mechanic;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderService> services;

    public enum Status {
        IN_PROGRESS, COMPLETED, CANCELLED
    }

    public void setCreationDate(LocalDateTime now) {
        this.creationDate = now;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Getter for creationDate
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    // Getter and Setter for completionDate
    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    // Getter and Setter for mechanic
    public User getMechanic() {
        return mechanic;
    }

    public void setMechanic(User mechanic) {
        this.mechanic = mechanic;
    }

    // Getter and Setter for car
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    // toString method for easier debugging
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", creationDate=" + creationDate +
                ", completionDate=" + completionDate +
                ", client=" + client +
                ", mechanic=" + mechanic +
                ", car=" + car +
                '}';
    }
}