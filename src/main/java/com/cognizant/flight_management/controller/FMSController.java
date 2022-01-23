package com.cognizant.flight_management.controller;


import com.cognizant.flight_management.exception.InvalidFlightOperation;
import com.cognizant.flight_management.model.Flight;
import com.cognizant.flight_management.model.FlightService;
import com.cognizant.flight_management.model.Passenger;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/flights")
public class FMSController {

//    @Autowired
    private final FlightService flightService = new FlightService();

    public FMSController() {
        flightService.addFlight(new Flight("1", Flight.ECONOMY, new ArrayList<Passenger>()));
        flightService.addFlight(new Flight("2", Flight.BUSINESS, new ArrayList<Passenger>()));
    }

    public void addFlight(Flight flight) {
        flightService.addFlight(flight);
    }

    public Flight getFlight(String id) {
        return flightService.getFlight(id);
    }

    @PostMapping(value="/business", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean addBusinessFlightPassenger(@Param("flightId") String flightId, @RequestBody Passenger passenger) throws InvalidFlightOperation {
        log.info("called addBusinessFlightPassenger");
        if(flightService.addPassenger(flightId, passenger)) {
            log.info("end call - addBusinessFlightPassenger");
            return true;
        }
        else return false;
    }

    @PostMapping(value="/economy", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean addEconomyFlightPassenger(@Param("flightId") String flightId, @RequestBody Passenger passenger) throws InvalidFlightOperation {
        log.info("called addEconomyFlightPassenger");
        if(flightService.addPassenger(flightId, passenger)) {
            log.info("end call - addEconomyFlightPassenger");
            return true;
        }
        else return false;
    }

    @DeleteMapping(value="/business", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean removeBusinessFlightPassenger(@Param("flightId") String flightId, @RequestBody Passenger passenger) throws InvalidFlightOperation {
        if(flightService.removePassenger(flightId, passenger)) {
            log.info("end call - removeBusinessFlightPassenger");
            return true;
        }
        else return false;
    }

    @DeleteMapping(value="/economy", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean removeEconomyFlightPassenger(@Param("flightId") String flightId, @RequestBody Passenger passenger) throws InvalidFlightOperation {
        if(flightService.removePassenger(flightId, passenger)) {
            log.info("end call - removeEconomyFlightPassenger");
            return true;
        }
        return false;
    }

}
