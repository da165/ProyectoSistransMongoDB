package com.alpescab.mongo.service;

import com.alpescab.mongo.model.*;
import com.alpescab.mongo.repository.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlpescabService {

    private final ServiceUserRepository serviceUserRepo;
    private final DriverRepository driverRepo;
    private final VehicleRepository vehicleRepo;
    private final AvailabilityRepository availabilityRepo;
    private final TripRepository tripRepo;
    private final ReviewRepository reviewRepo;

    public ServiceUser registerServiceUser(ServiceUser user) {
        user.setId(null);
        return serviceUserRepo.save(user);
    }

    public Driver registerDriver(Driver driver) {
        driver.setId(null);
        driver.setRatingAverage(0);
        driver.setRatingCount(0);
        return driverRepo.save(driver);
    }

    public Vehicle registerVehicle(Vehicle vehicle) {
        driverRepo.findById(vehicle.getDriverId())
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        vehicle.setId(null);
        return vehicleRepo.save(vehicle);
    }

    public Availability registerAvailability(Availability availability) {
        validateNoOverlappingAvailability(availability);
        availability.setId(null);
        return availabilityRepo.save(availability);
    }

    public Availability updateAvailability(String id, Availability updated) {
        Availability current = availabilityRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Availability not found"));
        updated.setId(id);
        validateNoOverlappingAvailability(updated);
        return availabilityRepo.save(updated);
    }

    private void validateNoOverlappingAvailability(Availability newAv) {
        List<Availability> existing = availabilityRepo.findByDriverId(newAv.getDriverId());
        for (Availability av : existing) {
            if (av.getId() != null && av.getId().equals(newAv.getId())) continue;
            if (av.getDayOfWeek().equals(newAv.getDayOfWeek())
                    && av.getServiceType().equals(newAv.getServiceType())
                    && rangesOverlap(av.getStartTime(), av.getEndTime(),
                    newAv.getStartTime(), newAv.getEndTime())) {
                throw new IllegalArgumentException("Overlapping availability");
            }
        }
    }

    private boolean rangesOverlap(String s1, String e1, String s2, String e2) {
        return s1.compareTo(e2) < 0 && s2.compareTo(e1) < 0;
        }

    @Transactional
    public Trip requestServiceAndStartTrip(String serviceUserId,
                                           String serviceType,
                                           String level,
                                           GeoPoint start,
                                           List<GeoPoint> endPoints,
                                           double distanceKm,
                                           double pricePerKm) {

        ServiceUser user = serviceUserRepo.findById(serviceUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getPaymentMethods() == null || user.getPaymentMethods().isEmpty()) {
            throw new IllegalStateException("User has no payment method");
        }

        Driver driver = driverRepo.findAll().stream()
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No driver available"));

        Vehicle vehicle = vehicleRepo.findByDriverId(driver.getId()).stream()
                .filter(v -> v.getLevel().equals(level))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No vehicle with given level"));

        double totalPrice = distanceKm * pricePerKm;

        Trip trip = Trip.builder()
                .serviceUserId(user.getId())
                .driverId(driver.getId())
                .vehicleId(vehicle.getId())
                .serviceType(serviceType)
                .level(level)
                .startPoint(start)
                .endPoints(endPoints)
                .distanceKm(distanceKm)
                .price(totalPrice)
                .status("IN_PROGRESS")
                .startTime(LocalDateTime.now())
                .build();

        return tripRepo.save(trip);
    }

    @Transactional
    public Trip finishTrip(String tripId, double realDistanceKm) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        trip.setEndTime(LocalDateTime.now());
        trip.setDurationSeconds(Duration.between(trip.getStartTime(), trip.getEndTime()).getSeconds());
        trip.setDistanceKm(realDistanceKm);
        trip.setStatus("FINISHED");
        return tripRepo.save(trip);
    }

    public Review addReview(Review review) {
        review.setId(null);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepo.save(review);

        List<Review> reviews = reviewRepo.findByTargetUserId(review.getTargetUserId());
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0);
        long count = reviews.size();

        driverRepo.findById(review.getTargetUserId()).ifPresent(d -> {
            d.setRatingAverage(avg);
            d.setRatingCount(count);
            driverRepo.save(d);
        });

        return saved;
    }

    public List<Trip> getTripsByUser(String userId) {
        return tripRepo.findByServiceUserId(userId);
    }

    public List<DriverStats> getTop20Drivers() {
        return driverRepo.findAll().stream()
                .map(d -> new DriverStats(d, tripRepo.countByDriverId(d.getId())))
                .sorted(Comparator.comparingLong(DriverStats::getTripsCount).reversed())
                .limit(20)
                .toList();
    }

    public List<ServiceUsageStats> getUsageByCityAndDateRange(String city,
                                                              LocalDateTime from,
                                                              LocalDateTime to) {
        List<Trip> trips = tripRepo.findByStartTimeBetweenAndStartPoint_City(from, to, city);
        long total = trips.size();
        return trips.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Trip::getServiceType, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .map(e -> new ServiceUsageStats(
                        e.getKey(),
                        e.getValue(),
                        total == 0 ? 0.0 : (100.0 * e.getValue() / total)))
                .sorted((a, b) -> Long.compare(b.count(), a.count()))
                .toList();
    }

    @Getter
    @AllArgsConstructor
    public static class DriverStats {
        private Driver driver;
        private long tripsCount;
    }

    @Getter
    @AllArgsConstructor
    public static class ServiceUsageStats {
        private String serviceType;
        private long count;
        private double percentage;
    }
}
