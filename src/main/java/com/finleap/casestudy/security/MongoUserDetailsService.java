package com.finleap.casestudy.security;



import com.finleap.casestudy.domain.IncidentUser;
import com.finleap.casestudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MongoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<IncidentUser> userOptional = userRepository.findByUsername(userName);
        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User " + userName + " not found");
        } else {
            IncidentUser incidentUser = userOptional.get();
            UserDetails details = new UserPrincipal(incidentUser);
            return details;
        }

    }
}