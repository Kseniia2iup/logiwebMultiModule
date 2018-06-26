package ru.tsystems.javaschool.dto;

import ru.tsystems.javaschool.dto.enums.DriverStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "driver_name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "worked_this_month")
    private Integer workedThisMonth;

    @Column(name = "e_mail")
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "shift_begined")
    private Date shiftBegined;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "shift_ended")
    private Date shiftEnded;

    @Column(name = "phase_of_work", nullable = false)
    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name="id", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id")
    private Truck currentTruck;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public Driver() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getWorkedThisMonth() {
        return workedThisMonth;
    }

    public void setWorkedThisMonth(Integer workedThisMonth) {
        this.workedThisMonth = workedThisMonth;
    }

    public Date getShiftBegined() {
        return shiftBegined;
    }

    public void setShiftBegined(Date shiftBegined) {
        this.shiftBegined = shiftBegined;
    }

    public Date getShiftEnded() {
        return shiftEnded;
    }

    public void setShiftEnded(Date shiftEnded) {
        this.shiftEnded = shiftEnded;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Truck getCurrentTruck() {
        return currentTruck;
    }

    public void setCurrentTruck(Truck currentTruck) {
        this.currentTruck = currentTruck;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
