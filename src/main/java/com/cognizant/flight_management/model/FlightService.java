package com.cognizant.flight_management.model;

import com.cognizant.flight_management.exception.InvalidFlightOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
@Getter
@Setter
@Service
public class FlightService {

    private final List<Flight> flights = new ArrayList<Flight>();

    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    public Flight getFlight(String id) {
        for(Flight f : flights) {
            if(f.getId().equalsIgnoreCase(id)) return f;
        }
        return null;
    }

    public boolean addPassenger(String flightId, Passenger passenger) throws InvalidFlightOperation {
        log.info("START addPassenger => flightId="+flightId+", passenger="+passenger);
        if(passenger==null) {
            InvalidFlightOperation exception = new InvalidFlightOperation("Cannot Add a NULL passenger to a flight");
            log.error(exception.getMessage());
            throw exception;
        }
        int index = -1;
        for(int i = 0 ; i < flights.size() ; i++) {
            if(flights.get(i).getId().equalsIgnoreCase(flightId)) {
                index = i;
                break;
            }
        }
        if(index<0) {
            InvalidFlightOperation exception =  new InvalidFlightOperation("Cannot Add the passenger. Flight not found");
            log.error(exception.getMessage());
            throw exception;
        }
        if(!passenger.isVip()) {
            if(flights.get(index).getFlightType().equalsIgnoreCase(Flight.BUSINESS)) {
                InvalidFlightOperation exception = new InvalidFlightOperation("Cannot Add a NON-VIP passenger to a BUSINESS flight");
                log.error(exception.getMessage());
                throw exception;
            }
            else flights.get(index).getPassengers().add(passenger);
        } else flights.get(index).getPassengers().add(passenger);
        log.info("END");
        return true;
    }

    public boolean removePassenger(String flightId, Passenger passenger) throws InvalidFlightOperation {
        log.info("START removePassenger => flightId="+flightId+", passenger="+passenger);
        if(passenger==null) {
            InvalidFlightOperation exception = new InvalidFlightOperation("Cannot Remove a NULL passenger from a flight");
            log.error(exception.getMessage());
            throw exception;
        }
        int index = -1;
        for(int i = 0 ; i < flights.size() ; i++) {
            if(flights.get(i).getId().equalsIgnoreCase(flightId)) {
                index = i;
                break;
            }
        }
        if(index==-1) {
            InvalidFlightOperation exception =  new InvalidFlightOperation("Cannot Remove the passenger. Flight not found");
            log.error(exception.getMessage());
            throw exception;
        }
        if(passenger.isVip()) {
            InvalidFlightOperation exception =  new InvalidFlightOperation("Cannot Remove a NON-VIP passenger from a flight");
            log.error(exception.getMessage());
            throw exception;
        }
        if(flights.get(index).getFlightType().equalsIgnoreCase(Flight.BUSINESS)) {
            InvalidFlightOperation exception =   new InvalidFlightOperation("Cannot Remove a NON-VIP passenger from a Business flight");
            log.error(exception.getMessage());
            throw exception;
        }
        else {
            flights.get(index).getPassengers().remove(passenger);
            log.info("PASSENGER REMOVED FROM FLIGHT");
            return true;
        }
    }

}
