package ru.tsystems.javaschool.dto;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue()
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "city_name")
    private String name;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @OneToMany(mappedBy = "city")
    private Set<Truck> trucks;

    @OneToMany(mappedBy = "city")
    private Set<Driver> drivers;

    @OneToMany(mappedBy = "cityDep")
    private Set<Waypoint> waypointsDep;

    @OneToMany(mappedBy = "cityDest")
    private Set<Waypoint> waypointsDest;

    public City() {
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Set<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(Set<Truck> trucks) {
        this.trucks = trucks;
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }

    public Set<Waypoint> getWaypointsDep() {
        return waypointsDep;
    }

    public void setWaypointsDep(Set<Waypoint> waypointsDep) {
        this.waypointsDep = waypointsDep;
    }

    public Set<Waypoint> getWaypointsDest() {
        return waypointsDest;
    }

    public void setWaypointsDest(Set<Waypoint> waypointsDest) {
        this.waypointsDest = waypointsDest;
    }
}
