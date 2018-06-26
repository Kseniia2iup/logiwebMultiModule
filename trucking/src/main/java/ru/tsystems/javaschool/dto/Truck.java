package ru.tsystems.javaschool.dto;

import ru.tsystems.javaschool.dto.enums.TruckStatus;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "trucks")
public class Truck {

    @Id
    @GeneratedValue()
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "reg_number", length = 7, unique = true, nullable = false)
    private String regNumber;

    @Column(name = "shift_period", nullable = false)
    private Integer shiftPeriod;

    @Column(name = "capacity_ton", nullable = false)
    private Integer capacityTon;

    @Column(name = "truck_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private TruckStatus condition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "currentTruck", fetch = FetchType.LAZY)
    private Set<Driver> drivers;

    @OneToOne(mappedBy = "truck")
    private Order order;

    public Truck() {
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Integer getShiftPeriod() {
        return shiftPeriod;
    }

    public void setShiftPeriod(Integer shiftPeriod) {
        this.shiftPeriod = shiftPeriod;
    }

    public Integer getCapacityTon() {
        return capacityTon;
    }

    public void setCapacityTon(Integer capacityTon) {
        this.capacityTon = capacityTon;
    }

    public TruckStatus getCondition() {
        return condition;
    }

    public void setCondition(TruckStatus condition) {
        this.condition = condition;
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
