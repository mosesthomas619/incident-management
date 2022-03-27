package com.finleap.casestudy.service;

import com.finleap.casestudy.domain.IncidentDO;
import com.finleap.casestudy.domain.MessageResponse;
import com.finleap.casestudy.domain.IncidentUser;
import com.finleap.casestudy.exception.UserAlreadyAssignedException;
import com.finleap.casestudy.exception.UserNotFoundException;
import com.finleap.casestudy.exception.ValidationException;
import com.finleap.casestudy.repository.IncidentRepository;
import com.finleap.casestudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    public UserRepository userRepository;

    public IncidentRepository incidentRepository;

    BCryptPasswordEncoder encoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, IncidentRepository incidentRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.incidentRepository = incidentRepository;
        this.encoder = encoder;
    }

    @Override
    public MessageResponse createUser(IncidentUser incidentUser) {
        Optional<IncidentUser> userOptional = userRepository.findByUsername(incidentUser.getUsername());
        if (userOptional.isPresent()) {
            throw new ValidationException("User with name: " + incidentUser.getUsername() + " already present.");
        }
        if(null == incidentUser.getUserId() || incidentUser.getUserId().isEmpty()) {
            incidentUser.setUserId(String.valueOf(System.currentTimeMillis()));
        }
        incidentUser.setPassword(encoder.encode(incidentUser.getPassword()));
        userRepository.save(incidentUser);
        return new MessageResponse("Successfully created user: " + incidentUser.getUsername() + " with id: " + incidentUser.getUserId());
    }


    @Override
    public MessageResponse editUser(IncidentUser incidentUser) {
        Optional<IncidentUser> userOptional = userRepository.findById(incidentUser.getUserId());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("No users found with id "+ incidentUser.getUserId());
        }
        if(userOptional.get().isAssigned() && incidentUser.isAssigned() != userOptional.get().isAssigned()) {
            incidentUser.setAssigned(true);  // not allowed to change the isAssigned field since user is already assigned an incident
        }
        if(!(userOptional.get().getUsername().equals(incidentUser.getUsername())) && userOptional.get().isAssigned()) {  //if username is changed
            List<IncidentDO> incidentAssigneeList = incidentRepository.findByAssignee(userOptional.get().getUsername());
            if(!incidentAssigneeList.isEmpty()) {
                updateAssigneeNameInIncidentRepo(incidentAssigneeList, incidentUser.getUsername());
            }
            List<IncidentDO> incidentCreatorList = incidentRepository.findByCreatedBy(userOptional.get().getUsername());
            if(!incidentCreatorList.isEmpty()) {
                updateCreatedByNameInIncidentRepo(incidentCreatorList, incidentUser.getUsername());
            }
        }
        incidentUser.setPassword(encoder.encode(incidentUser.getPassword()));
        userRepository.delete(userOptional.get());  // to avoid unique key issue
        userRepository.save(incidentUser);
        return new MessageResponse("Successfully updated user: " + incidentUser.getUsername() + " with id: " + incidentUser.getUserId());
    }

    @Override
    public MessageResponse deleteUser(String userName) {
        Optional<IncidentUser> userOptional = userRepository.findByUsername(userName);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("No users found with name "+ userName);
        }
        if(userOptional.get().isAssigned()) {
           throw new UserAlreadyAssignedException("Cannot delete this user as it is assigned an incident");
        }
        userRepository.delete(userOptional.get());
        return new MessageResponse("Successfully deleted user: " + userName);
    }


    private void updateAssigneeNameInIncidentRepo(List<IncidentDO> list, String newAssigneeName) {
        list.forEach(incident -> incident.setAssignee(newAssigneeName));
        list.forEach(incident -> incidentRepository.save(incident));
    }

    private void updateCreatedByNameInIncidentRepo(List<IncidentDO> list, String newCreatorName) {
        list.forEach(incident -> incident.setCreatedBy(newCreatorName));
        list.forEach(incident -> incidentRepository.save(incident));
    }
}
