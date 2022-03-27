package com.finleap.casestudy;

import com.finleap.casestudy.domain.IncidentDO;
import com.finleap.casestudy.domain.IncidentRequest;
import com.finleap.casestudy.domain.IncidentUser;
import com.finleap.casestudy.domain.MessageResponse;
import com.finleap.casestudy.exception.*;
import com.finleap.casestudy.factory.IncidentFactory;
import com.finleap.casestudy.factory.UserFactory;
import com.finleap.casestudy.repository.IncidentRepository;
import com.finleap.casestudy.repository.UserRepository;
import com.finleap.casestudy.service.IncidentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTests {

	@Mock
	private IncidentRepository incidentRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private IncidentServiceImpl incidentService;


	@Test
	public void shouldNotCreateIncidentIfAssigneeNotFound() {

		List<IncidentRequest> requests = IncidentFactory.getIncidentRequests();

		when(incidentRepository.findByTitle(any())).thenReturn(Optional.empty());
		when(userRepository.findByUsername(any())).thenReturn(Optional.empty());


		Exception exception = Assertions.assertThrows(UserNotFoundException.class,
				() -> incidentService.createIncident(requests.get(0), "user1"));

		String expectedMessage = "Assignee not found";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	public void shouldNotCreateIncidentIfUserAlreadyAssigned() {

		List<IncidentRequest> requests = IncidentFactory.getIncidentRequests();
		List<IncidentUser> users = UserFactory.getUsers();
		when(incidentRepository.findByTitle(any())).thenReturn(Optional.empty());
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(users.get(4)));


		Exception exception = Assertions.assertThrows(UserAlreadyAssignedException.class,
				() -> incidentService.createIncident(requests.get(0), "user1"));

		String expectedMessage = "is already assigned an incident";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void shouldNotCreateIncidentIfIncidentAlreadyExists() {

		List<IncidentRequest> requests = IncidentFactory.getIncidentRequests();
		List<IncidentDO> incidents = IncidentFactory.getIncidents();
		when(incidentRepository.findByTitle(any())).thenReturn(Optional.of(incidents.get(0)));

		Exception exception = Assertions.assertThrows(ValidationException.class,
				() -> incidentService.createIncident(requests.get(0), "user1"));

		String expectedMessage = "already present";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	public void shouldCreateIncidentForValidCase() {

		List<IncidentRequest> requests = IncidentFactory.getIncidentRequests();
		List<IncidentDO> incidents = IncidentFactory.getIncidents();
		when(incidentRepository.findByTitle(any())).thenReturn(Optional.empty());


		MessageResponse response = incidentService.createIncident(requests.get(4), "user1");

		String expectedMessage = "Successfully created";
		String actualMessage = response.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	public void shouldNotUpdateIncidentIfIncidentDoesNotExist() {

		List<IncidentRequest> requests = IncidentFactory.getIncidentRequests();
		List<IncidentDO> incidents = IncidentFactory.getIncidents();
		when(incidentRepository.findById(any())).thenReturn(Optional.empty());

		Exception exception = Assertions.assertThrows(NoIncidentFoundException.class,
				() -> incidentService.updateIncident(requests.get(0), "user1"));

		String expectedMessage = "No incident found for id";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void shouldNotUpdateIncidentIfNotCreatorAndAssigneeDoesNotExist() {

		List<IncidentRequest> requests = IncidentFactory.getIncidentRequests();
		List<IncidentDO> incidents = IncidentFactory.getIncidents();
		List<IncidentUser> users = UserFactory.getUsers();
		when(incidentRepository.findById(any())).thenReturn(Optional.of(incidents.get(0)));

		Exception exception = Assertions.assertThrows(UserUnauthorisedException.class,
				() -> incidentService.updateIncident(requests.get(0), "user1"));

		String expectedMessage = "is not authorised to update this incident";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}



	@Test
	public void shouldNotUpdateIncidentIfNotCreatorAndNotAssignee() {

		List<IncidentRequest> requests = IncidentFactory.getIncidentRequests();
		List<IncidentDO> incidents = IncidentFactory.getIncidents();
		List<IncidentUser> users = UserFactory.getUsers();
		when(incidentRepository.findById(any())).thenReturn(Optional.of(incidents.get(1)));

		Exception exception = Assertions.assertThrows(UserUnauthorisedException.class,
				() -> incidentService.updateIncident(requests.get(0), "user1"));

		String expectedMessage = "is not authorised to update this incident";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	public void shouldNotUpdateIncidentStatusIfUserIsJustCreator() {

		List<IncidentRequest> requests = IncidentFactory.getIncidentRequests();
		List<IncidentDO> incidents = IncidentFactory.getIncidents();
		when(incidentRepository.findById(any())).thenReturn(Optional.of(incidents.get(1)));

		Exception exception = Assertions.assertThrows(UserUnauthorisedException.class,
				() -> incidentService.updateIncident(requests.get(1), "Userone"));

		String expectedMessage = "Only the assignee of the incident Usertwo can update its status";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

}
