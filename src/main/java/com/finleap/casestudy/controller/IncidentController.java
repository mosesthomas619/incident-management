package com.finleap.casestudy.controller;

import com.finleap.casestudy.domain.IncidentDO;
import com.finleap.casestudy.domain.IncidentRequest;
import com.finleap.casestudy.domain.IncidentWrapper;
import com.finleap.casestudy.domain.MessageResponse;
import com.finleap.casestudy.service.IncidentService;
import com.finleap.casestudy.service.IncidentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/incidentapi/v1", produces = {"application/json"})
public class IncidentController {

    private final IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentServiceImpl incidentService) {
        this.incidentService = incidentService;
    }


    /*add incident using a post call which accepts the incidentRequest object. */
    @RequestMapping(value = "/incident", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<MessageResponse> createIncident(@RequestBody @Valid IncidentRequest incidentRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MessageResponse messageResponse = incidentService.createIncident(incidentRequest, authentication.getName());
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }


    /* Get all incidents */
    @RequestMapping(value = "/incidents", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<IncidentWrapper> getAllIncidents() {
        List<IncidentDO> allIncidents = incidentService.getAllIncidents();
        return new ResponseEntity<>(new IncidentWrapper(allIncidents), HttpStatus.OK);
    }


    /*update incident using a put call which accepts the incidentRequest object. */
    @RequestMapping(value = "/incident/{incidentId}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<MessageResponse> updateIncident(@PathVariable String incidentId, @RequestBody @Valid IncidentRequest incidentRequest) {
        incidentRequest.setId(incidentId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MessageResponse messageResponse = incidentService.updateIncident(incidentRequest, authentication.getName());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


    /*delete incident using a delete call which accepts the incident id. */
    @RequestMapping(value = "/incident/{incidentId}", method = RequestMethod.DELETE)
    public ResponseEntity<MessageResponse> deleteIncident(@PathVariable String incidentId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MessageResponse messageResponse = incidentService.deleteIncident(incidentId, authentication.getName());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
