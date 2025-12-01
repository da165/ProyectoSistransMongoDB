package com.alpescab.mongo.controller;

import com.alpescab.mongo.model.*;
import com.alpescab.mongo.service.AlpescabService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AlpescabController {

    private final AlpescabService service;

    @PostMapping("/service-users")
    public ServiceUser createServiceUser(@RequestBody ServiceUser user) {
        return service.registerServiceUser(user);
    }

    @PostMapping("/drivers")
    public Driver createDriver(@RequestBody Driver driver) {
        return service.registerDriver(driver);
    }

    @PostMapping("/vehicles")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return service.registerVehicle(vehicle);
    }

    @PostMapping("/availabilities")
    public Availability createAvailability(@RequestBody Availability availability) {
        return service.registerAvailability(availability);
    }

    @PutMapping("/availabilities/{id}")
    public Availability updateAvailability(@PathVariable String id,
                                           @RequestBody Availability availability) {
        return service.updateAvailability(id, availability);
    }

    @PostMapping("/trips/request")
    public Trip requestTrip(@RequestBody RequestTripDTO dto) {
        return service.requestServiceAndStartTrip(
                dto.getServiceUserId(), dto.getServiceType(), dto.getLevel(),
                dto.getStartPoint(), dto.getEndPoints(),
                dto.getDistanceKm(), dto.getPricePerKm());
    }

    @PostMapping("/trips/{id}/finish")
    public Trip finishTrip(@PathVariable String id, @RequestParam double distanceKm) {
        return service.finishTrip(id, distanceKm);
    }

    @GetMapping("/users/{id}/trips")
    public List<Trip> getUserTrips(@PathVariable String id) {
        return service.getTripsByUser(id);
    }

    @GetMapping("/drivers/top")
    public List<AlpescabService.DriverStats> getTopDrivers() {
        return service.getTop20Drivers();
    }

    @GetMapping("/stats/usage")
    public List<AlpescabService.ServiceUsageStats> usage(
            @RequestParam String city,
            @RequestParam String from,
            @RequestParam String to) {

        return service.getUsageByCityAndDateRange(
                city,
                LocalDateTime.parse(from),
                LocalDateTime.parse(to));
    }
}

@Getter
@Setter
class RequestTripDTO {
    private String serviceUserId;
    private String serviceType;
    private String level;
    private GeoPoint startPoint;
    private java.util.List<GeoPoint> endPoints;
    private double distanceKm;
    private double pricePerKm;
}
