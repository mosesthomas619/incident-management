package com.finleap.casestudy.service;


import com.finleap.casestudy.domain.MessageResponse;
import com.finleap.casestudy.domain.IncidentUser;

import java.util.List;

public interface UserService {

    MessageResponse createUser(IncidentUser incidentUser);

    MessageResponse editUser(IncidentUser incidentUser);

    MessageResponse deleteUser(String userName);
}


