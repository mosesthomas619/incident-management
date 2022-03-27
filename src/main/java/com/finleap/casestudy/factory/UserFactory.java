package com.finleap.casestudy.factory;

import com.finleap.casestudy.domain.IncidentUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserFactory {

    /** created for use in the unitTests. */

    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static List<IncidentUser> getUsers() {
        List<IncidentUser> incidentUsers = new ArrayList<>();

        IncidentUser u1 = new IncidentUser();
        u1.setUserId("1");
        u1.setUsername("user1");
        u1.setPassword(encoder.encode("pass1"));

        IncidentUser u2 = new IncidentUser();
        u2.setUserId("2");
        u2.setUsername("user2");
        u2.setPassword(encoder.encode("pass2"));

        IncidentUser u3 = new IncidentUser();
        u3.setUserId("3");
        u3.setUsername("user3");
        u3.setPassword(encoder.encode("pass3"));

        IncidentUser u4 = new IncidentUser();
        u4.setUserId("4");
        u4.setUsername("user4");
        u4.setPassword(encoder.encode("pass4"));
        u4.setAssigned(true);


        IncidentUser u5 = new IncidentUser();
        u5.setUserId("5");
        u5.setUsername("user5");
        u5.setPassword(encoder.encode("pass5"));
        u5.setAssigned(true);


        incidentUsers.add(u1);
        incidentUsers.add(u2);
        incidentUsers.add(u3);
        incidentUsers.add(u4);
        incidentUsers.add(u5);

        return incidentUsers;

    }
}
