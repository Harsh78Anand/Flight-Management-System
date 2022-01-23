package com.cognizant.flight_management;

import com.cognizant.flight_management.controller.FMSController;
import com.cognizant.flight_management.exception.InvalidFlightOperation;
import com.cognizant.flight_management.model.Flight;
import com.cognizant.flight_management.model.Passenger;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@ExceptionHandler
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FMSController.class)
class FlightManagementApplicationTests {
	@Autowired FMSController fmsController;
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper mapper;

	@Test
	@DisplayName("Context Loads")
	void contextLoads() {
		assertTrue(true);
	}

	@Test
	void addEconomyFlight() {
		log.info("START = addEconomyFlight");
		fmsController.addFlight(new Flight("3", Flight.ECONOMY, new ArrayList<Passenger>()));
		assertEquals(Flight.ECONOMY, fmsController.getFlight("1").getFlightType());
		log.info("END = addEconomyFlight");
	}

	@Test
	void addBusinessFlight() {
		log.info("START = addEconomyFlight");
		fmsController.addFlight(new Flight("4", Flight.BUSINESS, new ArrayList<Passenger>()));
		assertEquals(Flight.BUSINESS, fmsController.getFlight("2").getFlightType());
		log.info("END = addEconomyFlight");
	}

	@Test
	void addVipPassengerToEconomyFlight() {
		log.info("START = addVipPassengerToEconomyFlight");
		Passenger passenger = new Passenger("Mayur", true);
		try {
			log.info("START = addVipPassengerToEconomyFlight");
			mockMvc.perform(MockMvcRequestBuilders
					.post("/flights/economy")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.param("flightId", "1")
					.content(mapper.writeValueAsString(passenger))
			).andExpect(MockMvcResultMatchers.status().isOk());
			log.info("END = addVipPassengerToEconomyFlight");
		} catch (Exception e) {
			log.error("[Exception e] => "+e.getClass().toString()+" ===== "+e.getMessage());
			log.error("ERROR = addVipPassengerToEconomyFlight");
			assertTrue(false);
		}
	}

	@Test
	void addVipPassengerToBusinessFlight() {
		Passenger passenger = new Passenger("Mayur", true);

		try {
			log.info("START = addVipPassengerToBusinessFlight");
			mockMvc.perform(MockMvcRequestBuilders
					.post("/flights/business")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.param("flightId", "2")
					.content(mapper.writeValueAsString(passenger))
			).andExpect(MockMvcResultMatchers.status().isOk());
			log.info("END = addVipPassengerToBusinessFlight");
		} catch (Exception e) {
			log.error(e.getClass().toString()+" ===== "+e.getMessage());
			log.error("ERROR = addVipPassengerToBusinessFlight");
			assertTrue(false);
		}
	}

	@Test
	void addNonVipPassengerToEconomyFlight() {
		Passenger passenger = new Passenger("Jay", false);
		try {
			log.info("START = addNonVipPassengerToEconomyFlight");
			mockMvc.perform(MockMvcRequestBuilders
					.post("/flights/economy")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.param("flightId", "1")
					.content(mapper.writeValueAsString(passenger))
			).andExpect(MockMvcResultMatchers.status().isOk());
			log.info("END = addNonVipPassengerToEconomyFlight");
		} catch (Exception e) {
			log.error("[Exception e] => "+e.getClass().toString()+" ===== "+e.getMessage());
			log.error("ERROR = addNonVipPassengerToEconomyFlight");
			assertTrue(false);
		}

	}

	@Test
	void addNonVipPassengerToBusinessFlight() {
		Passenger passenger = new Passenger("Jay", false);
		try {
			log.info("START = addNonVipPassengerToBusinessFlight");
			mockMvc.perform(MockMvcRequestBuilders
							.post("/flights/business")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.param("flightId", "2")
							.content(mapper.writeValueAsString(passenger))
			).andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidFlightOperation));
			log.info("END = addNonVipPassengerToBusinessFlight");
		} catch (Exception e) {
			log.error("[Exception e] => "+e.getClass().toString()+" ===== "+e.getMessage());
			log.error("END = addNonVipPassengerToBusinessFlight");
			assertTrue(e.getCause() instanceof InvalidFlightOperation);
		}
	}

	@Test
	void removeVipPassengerFromEconomyFlight() {
		Passenger passenger = new Passenger("Mayur", true);
		try {
			log.info("START = removeVipPassengerFromEconomyFlight");
			fmsController.addEconomyFlightPassenger("1", passenger);
			mockMvc.perform(MockMvcRequestBuilders
					.delete("/flights/economy")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.param("flightId", "1")
					.content(mapper.writeValueAsString(passenger))
			).andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidFlightOperation));
			log.info("END = removeVipPassengerFromEconomyFlight");
		} catch (Exception e) {
			log.error("[Exception e] => "+e.getClass().toString()+" ===== "+e.getMessage());
			log.error("ERROR = removeVipPassengerFromEconomyFlight");
			assertTrue(e.getCause() instanceof InvalidFlightOperation);
		}
	}

	@Test
	void removeVipPassengerFromBusinessFlight() {
		Passenger passenger = new Passenger("Mayur", true);
		try {
			log.info("START = removeVipPassengerFromBusinessFlight");
			mockMvc.perform(MockMvcRequestBuilders
					.delete("/flights/business")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.param("flightId", "2")
							.content(mapper.writeValueAsString(passenger))
			).andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidFlightOperation));
			log.info("END = removeVipPassengerFromBusinessFlight");
		} catch (Exception e) {
			log.error("[Exception e] => "+e.getClass().toString()+" ===== "+e.getMessage());
			log.error("ERROR = removeVipPassengerFromBusinessFlight");
			assertTrue(e.getCause() instanceof InvalidFlightOperation);
		}
	}

	@Test
	void removeNonVipPassengerFromEconomyFlight() {
		Passenger passenger = new Passenger("Jay", false);
		try {
			log.info("START = removeNonVipPassengerFromEconomyFlight");
			mockMvc.perform(MockMvcRequestBuilders
					.delete("/flights/economy")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.param("flightId", "1")
					.content(mapper.writeValueAsString(passenger))
			).andExpect(MockMvcResultMatchers.status().isOk());
			log.info("END = removeNonVipPassengerFromEconomyFlight");
		} catch (Exception e) {
			log.error("[Exception e] => "+e.getClass().toString()+" ===== "+e.getMessage());
			log.error("ERROR = removeNonVipPassengerFromEconomyFlight");
			assertTrue(false);
		}
	}

	@Test
	void removeNonVipPassengerFromBusinessFlight() {
		Passenger passenger = new Passenger("Jay", false);
		try {
			log.info("START = removeNonVipPassengerFromBusinessFlight");
			mockMvc.perform(MockMvcRequestBuilders
					.delete("/flights/business")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.param("flightId", "2")
							.content(mapper.writeValueAsString(passenger))
			).andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidFlightOperation));
			log.info("END = removeNonVipPassengerFromBusinessFlight");
		} catch (Exception e) {
			log.error("[Exception e] => "+e.getClass().toString()+" ===== "+e.getMessage());
			log.error("ERROR = removeNonVipPassengerFromBusinessFlight");
			assertTrue(e.getCause() instanceof InvalidFlightOperation);
		}
	}

}
