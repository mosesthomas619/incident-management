package com.finleap.casestudy;


import com.finleap.casestudy.domain.IncidentUser;
import com.finleap.casestudy.domain.MessageResponse;
import com.finleap.casestudy.exception.UserAlreadyAssignedException;
import com.finleap.casestudy.exception.ValidationException;
import com.finleap.casestudy.factory.UserFactory;
import com.finleap.casestudy.repository.IncidentRepository;
import com.finleap.casestudy.repository.UserRepository;
import com.finleap.casestudy.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {


    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void shouldCreateUser() {

        List<IncidentUser> users = UserFactory.getUsers();
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(users.get(0));


        MessageResponse response = userService.createUser(users.get(0));

        String expectedMessage = "Successfully created user";
        String actualMessage = response.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void shouldNotCreateUserIfUserAlreadyExist() {

        List<IncidentUser> users = UserFactory.getUsers();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(users.get(0)));

        Exception exception = Assertions.assertThrows(ValidationException.class,
                () -> userService.createUser(users.get(0)));

        String expectedMessage = "User with name: user1 already present.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void shouldNotDeleteUserIfUserAlreadyAssignedAnIncident() {

        List<IncidentUser> users = UserFactory.getUsers();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(users.get(4)));

        Exception exception = Assertions.assertThrows(UserAlreadyAssignedException.class,
                () -> userService.deleteUser(users.get(4).getUsername()));

        String expectedMessage = "Cannot delete this user as it is assigned an incident";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void shouldDeleteUser() {

        List<IncidentUser> users = UserFactory.getUsers();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(users.get(1)));


        MessageResponse response = userService.deleteUser(users.get(1).getUsername());

        String expectedMessage = "Successfully deleted user";
        String actualMessage = response.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
