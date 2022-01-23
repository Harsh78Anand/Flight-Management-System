package com.cognizant.flight_management.model;

import com.cognizant.flight_management.exception.InvalidFlightOperation;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Flight {

    public static final String BUSINESS = "business";
    public static final String ECONOMY = "economy";

    private String id;
    private String flightType;
    private List<Passenger> passengers;

}
