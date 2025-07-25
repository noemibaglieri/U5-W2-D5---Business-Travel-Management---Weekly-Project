package noemibaglieri.controllers;

import noemibaglieri.entities.Trip;
import noemibaglieri.enums.TripStatus;
import noemibaglieri.exceptions.BadEnumException;
import noemibaglieri.exceptions.ValidationException;
import noemibaglieri.payloads.NewTripDTO;
import noemibaglieri.services.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripsController {

    @Autowired
    private TripsService tripsService;

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripsService.findAll();
    }

    @GetMapping("/{tripId}")
    public Trip getTripById(@PathVariable long tripId) {
        return tripsService.findById(tripId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Trip createTrip(@RequestBody @Validated NewTripDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return tripsService.save(body);
    }

    @PutMapping("/{tripId}")
    public Trip updateTrip(@PathVariable long tripId, @RequestBody @Validated NewTripDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return tripsService.findByIdAndUpdate(tripId, body);
    }

    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrip(@PathVariable long tripId) {
        tripsService.findByIdAndDelete(tripId);
    }

    @PatchMapping("/{tripId}/status")
    public Trip updateTripStatus(@PathVariable long tripId, @RequestParam String status) {
        try {
            TripStatus newStatus = TripStatus.valueOf(status.toUpperCase());
            return tripsService.updateTripStatus(tripId, newStatus);
        } catch (IllegalArgumentException ex) {
            throw new BadEnumException("Invalid trip status: " + status + ". Allowed values: IN_PROGESS, COMPLETED.");
        }
    }

    @GetMapping("/status")
    public List<Trip> getTripsByStatus(@RequestParam String status) {
        try {
            TripStatus enumStatus = TripStatus.valueOf(status.toUpperCase());
            return tripsService.findByStatus(enumStatus);
        } catch (IllegalArgumentException ex) {
            throw new BadEnumException("Invalid trip status: " + status + ". Allowed values: IN_PROGESS, COMPLETED.");
        }
    }
}

