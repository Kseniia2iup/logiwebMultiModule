package ru.tsystems.javaschool.dto;

import ru.tsystems.javaschool.dto.enums.CargoStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cargoes")
public class Cargo {

    @Id
    @GeneratedValue()
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "cargo_name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "weight_kg")
    @NotNull
    private Integer weight;

    @Column(name = "delivery_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CargoStatus delivery_status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(mappedBy = "cargo")
    private Waypoint waypoint;

    public Cargo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public CargoStatus getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(CargoStatus delivery_status) {
        this.delivery_status = delivery_status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(Waypoint waypoint) {
        this.waypoint = waypoint;
    }
}
