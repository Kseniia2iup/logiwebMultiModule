package ru.tsystems.javaschool.dto;

import javax.persistence.*;

@Entity
@Table(name = "waypoints")
public class Waypoint {

    @Id
    @GeneratedValue()
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_dep_id")
    private City cityDep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_dest_id")
    private City cityDest;

    public Waypoint() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public City getCityDep() {
        return cityDep;
    }

    public void setCityDep(City cityDep) {
        this.cityDep = cityDep;
    }

    public City getCityDest() {
        return cityDest;
    }

    public void setCityDest(City cityDest) {
        this.cityDest = cityDest;
    }
}
