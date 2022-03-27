package com.finleap.casestudy.factory;



import com.finleap.casestudy.domain.IncidentDO;
import com.finleap.casestudy.domain.IncidentRequest;
import com.finleap.casestudy.domain.Status;

import java.util.ArrayList;
import java.util.List;

public class IncidentFactory {

    /** created for use in the unitTests. */

    public static List<IncidentDO> getIncidents() {
        List<IncidentDO> incidents = new ArrayList<>();

        IncidentDO i1 = new IncidentDO();
        i1.setId("1");
        i1.setStatus(Status.NEW);
        i1.setTitle("incident 1");
        i1.setCreatedBy("Userone");


        IncidentDO i2 = new IncidentDO();
        i2.setId("2");
        i2.setStatus(Status.ASSIGNED);
        i2.setTitle("incident 2");
        i2.setCreatedBy("Userone");
        i2.setAssignee("Usertwo");

        IncidentDO i3 = new IncidentDO();
        i3.setId("3");
        i3.setStatus(Status.NEW);
        i3.setTitle("incident 3");

        IncidentDO i4 = new IncidentDO();
        i4.setId("4");
        i4.setStatus(Status.NEW);
        i4.setTitle("incident 4");


        IncidentDO i5 = new IncidentDO();
        i5.setId("5");
        i5.setStatus(Status.NEW);
        i5.setTitle("incident 5");

        incidents.add(i1);
        incidents.add(i2);
        incidents.add(i3);
        incidents.add(i4);
        incidents.add(i5);

        return incidents;

    }


    public static List<IncidentRequest> getIncidentRequests() {
        List<IncidentRequest> incidents = new ArrayList<>();

        IncidentRequest i1 = new IncidentRequest();
        i1.setId("1");
        i1.setStatus(Status.NEW);
        i1.setTitle("incident 1");
        i1.setAssignee("user");


        IncidentRequest i2 = new IncidentRequest();
        i2.setId("2");
        i2.setStatus(Status.CLOSED);
        i2.setTitle("incident 2");

        IncidentRequest i3 = new IncidentRequest();
        i3.setId("3");
        i3.setStatus(Status.NEW);
        i3.setTitle("incident 3");

        IncidentRequest i4 = new IncidentRequest();
        i4.setId("4");
        i4.setStatus(Status.NEW);
        i4.setTitle("incident 4");


        IncidentRequest i5 = new IncidentRequest();
        i5.setId("5");
        i5.setStatus(Status.NEW);
        i5.setTitle("incident 5");

        incidents.add(i1);
        incidents.add(i2);
        incidents.add(i3);
        incidents.add(i4);
        incidents.add(i5);

        return incidents;

    }

}
