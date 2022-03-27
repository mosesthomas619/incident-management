package com.finleap.casestudy.service;



import com.finleap.casestudy.domain.*;
import com.finleap.casestudy.exception.*;
import com.finleap.casestudy.repository.IncidentRepository;
import com.finleap.casestudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Service
public class IncidentServiceImpl implements IncidentService {

    public IncidentRepository incidentRepository;

    public UserRepository userRepository;

    @Autowired
    public IncidentServiceImpl(IncidentRepository incidentRepository, UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MessageResponse createIncident(IncidentRequest incidentRequest, String createdBy) {
        Optional<IncidentDO> incidentOptional = incidentRepository.findByTitle(incidentRequest.getTitle());
        if (incidentOptional.isPresent()) {
            throw new ValidationException("Incident with title: " + incidentRequest.getTitle() + " already present.");
        }
        IncidentDO newIncident = new IncidentDO();
        newIncident.setCreatedBy(createdBy);
        newIncident.setCreatedAt(Instant.now());
        IncidentDO incidentDO = mapToIncidentDO(incidentRequest, newIncident);
        return validateAndSaveIncident(incidentDO, false);
    }



    @Override
    public List<IncidentDO> getAllIncidents() {
        List<IncidentDO> incidents = incidentRepository.findAll();
        if(incidents.isEmpty()) {
            throw new NoIncidentFoundException("No incidents found.");
        }
        return incidents;
    }



    @Override
    public MessageResponse updateIncident(IncidentRequest incidentRequest, String user) {

        Optional<IncidentDO> incidentOptional = incidentRepository.findById(incidentRequest.getId());
        if(incidentOptional.isEmpty()) {
            throw new NoIncidentFoundException("No incident found for id: " + incidentRequest.getId());

        } else if((null == incidentOptional.get().getAssignee()  || incidentOptional.get().getAssignee().isEmpty())   //no assignee in db and authenticated user is not creator
                && !incidentOptional.get().getCreatedBy().equals(user)) {
            throw new UserUnauthorisedException("User " + user + " is not authorised to update this incident");

        } else if((null == incidentOptional.get().getAssignee()  || incidentOptional.get().getAssignee().isEmpty())   //no assignee in db and authenticated user is the creator
                && incidentOptional.get().getCreatedBy().equals(user)) {
            IncidentDO incidentFromDb = incidentOptional.get();
            if(null != incidentRequest.getStatus() &&  !incidentRequest.getStatus().equals(incidentFromDb.getStatus())) {
                throw new UserUnauthorisedException("Only the assignee of a incident can update its status and the incident is not assigned yet");
            }
            IncidentDO incidentDO = mapToIncidentDO(incidentRequest, incidentFromDb);
            return validateAndSaveIncident(incidentDO, true);

        } else if(!incidentOptional.get().getAssignee().equals(user)  && !incidentOptional.get().getCreatedBy().equals(user)) {   //authenticated user is not assignee or creator
            throw new UserUnauthorisedException("User " + user + " is not authorised to update this incident");

        } else if(incidentOptional.get().getAssignee().equals(user)) {    // authenticated user is assignee in db
            IncidentDO incidentFromDb = incidentOptional.get();
            IncidentDO incidentDO = mapToIncidentDO(incidentRequest, incidentFromDb);
            if(incidentDO.getStatus() == Status.NEW || incidentDO.getStatus() == Status.CLOSED) {
                incidentDO.setAssignee(null);
                untagUser(user);
            }
            return validateAndSaveIncident(incidentDO, true);

        } else if(incidentOptional.get().getCreatedBy().equals(user)) {    // creator of the incident is the authenticated user and assignee not null
            IncidentDO incidentFromDb = incidentOptional.get();
            if(null != incidentRequest.getStatus() &&  !incidentRequest.getStatus().equals(incidentFromDb.getStatus())) {
                throw new UserUnauthorisedException("Only the assignee of the incident " + incidentFromDb.getAssignee() + " can update its status");
            }
            untagUser(incidentFromDb.getAssignee());
            IncidentDO incidentDO = mapToIncidentDO(incidentRequest, incidentFromDb);
            return validateAndSaveIncident(incidentDO, true);
        }

        return new MessageResponse("Successfully updated incident with id: " + incidentRequest.getId());
    }




    @Override
    public MessageResponse deleteIncident(String incidentId, String user) {
        Optional<IncidentDO> incidentOptional = incidentRepository.findById(incidentId);
        if(incidentOptional.isEmpty()) {
            throw new NoIncidentFoundException("No incident found with id: " + incidentId);
        } else if(!incidentOptional.get().getAssignee().equals(user)  && !incidentOptional.get().getCreatedBy().equals(user)) {   //creator or assignee can only delete
            throw new UserUnauthorisedException("User " + user + " is not authorised to delete this incident");
        }
        if(null != incidentOptional.get().getAssignee() && !incidentOptional.get().getAssignee().isEmpty()) {
            untagUser(incidentOptional.get().getAssignee());
        }
        incidentRepository.deleteById(incidentId);
        return new MessageResponse("Successfully deleted incident with id: " + incidentId);
    }






    private IncidentDO mapToIncidentDO(IncidentRequest incidentRequest , IncidentDO incidentDO) {
        if(null != incidentRequest.getId() && !incidentRequest.getId().isEmpty()) {
            incidentDO.setId(incidentRequest.getId());
        } else {
            incidentDO.setId("INC" + String.valueOf(System.currentTimeMillis()));
        }
        if(null != incidentRequest.getTitle() && !incidentRequest.getTitle().isEmpty()) {
            incidentDO.setTitle(incidentRequest.getTitle());
        }
        incidentDO.setAssignee(incidentRequest.getAssignee());
        incidentDO.setStatus(incidentRequest.getStatus());
        incidentDO.setLastUpdated(Instant.now());
        return incidentDO;
    }





    public MessageResponse validateAndSaveIncident(IncidentDO incidentDO, boolean updateFlag) {

        String action = (updateFlag) ? "updated " : "created ";

        if(null != incidentDO.getAssignee() && !incidentDO.getAssignee().isEmpty()) {
            Optional<IncidentUser> userOptional = userRepository.findByUsername(incidentDO.getAssignee());
            if(userOptional.isEmpty()) {
                throw new UserNotFoundException("Assignee not found");
            }
            if(userOptional.get().isAssigned()) {
                throw new UserAlreadyAssignedException("User " + incidentDO.getAssignee() + " is already assigned an incident");
            } else {
                IncidentUser incidentUser = userOptional.get();
                incidentDO.setStatus(Status.ASSIGNED);
                incidentUser.setAssigned(true);
                incidentRepository.save(incidentDO);
                userRepository.save(incidentUser);
                return new MessageResponse("Successfully " + action  + incidentDO.toString());
            }
        }
        if(incidentDO.getStatus() != Status.CLOSED) {
            incidentDO.setStatus(Status.NEW);
        }
        incidentRepository.save(incidentDO);
        return new MessageResponse("Successfully " + action + incidentDO.toString());
    }



    private void untagUser(String user) {
        IncidentUser incidentUser = userRepository.findByUsername(user).get();
        incidentUser.setAssigned(false);
        userRepository.save(incidentUser);
    }
}
