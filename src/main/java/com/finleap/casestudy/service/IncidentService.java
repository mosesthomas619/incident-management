package com.finleap.casestudy.service;


import com.finleap.casestudy.domain.IncidentDO;
import com.finleap.casestudy.domain.IncidentRequest;
import com.finleap.casestudy.domain.MessageResponse;

import java.util.List;

public interface IncidentService {

    MessageResponse createIncident(IncidentRequest incidentRequest, String createdBy);

    List<IncidentDO> getAllIncidents();

    MessageResponse updateIncident(IncidentRequest incident, String user);

    MessageResponse deleteIncident(String incidentId, String user);
}
