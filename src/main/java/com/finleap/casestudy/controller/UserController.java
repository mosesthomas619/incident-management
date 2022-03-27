package com.finleap.casestudy.controller;

import com.finleap.casestudy.domain.IncidentUser;
import com.finleap.casestudy.domain.UserWrapper;
import com.finleap.casestudy.domain.MessageResponse;
import com.finleap.casestudy.service.UserServiceImpl;
import com.finleap.casestudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/userapi/v1", produces = {"application/json"})
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    /*add user using a post call which accepts the user object. */
    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<MessageResponse> createUser(@RequestBody @Valid IncidentUser incidentUser) {
        MessageResponse messageResponse = userService.createUser(incidentUser);
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }


    /*update user using a put call which accepts the user object. */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable String userId, @RequestBody @Valid IncidentUser incidentUser) {
        incidentUser.setUserId(userId);
        MessageResponse messageResponse = userService.editUser(incidentUser);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


    /*delete user using a delete call which accepts the username. */
    @RequestMapping(value = "/user/{userName}", method = RequestMethod.DELETE)
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable String userName) {
        MessageResponse messageResponse = userService.deleteUser(userName);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


}
