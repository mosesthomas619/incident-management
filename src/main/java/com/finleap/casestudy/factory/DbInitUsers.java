package com.finleap.casestudy.factory;

import com.finleap.casestudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DbInitUsers {

    @Autowired
    private UserRepository repo;

    @PostConstruct
    private void postConstruct() {
        if (repo.count() == 0) {
            repo.save(UserFactory.getUsers().get(0));
            repo.save(UserFactory.getUsers().get(1));
            repo.save(UserFactory.getUsers().get(2));
            repo.save(UserFactory.getUsers().get(3));
            repo.save(UserFactory.getUsers().get(4));
        }
    }
}
