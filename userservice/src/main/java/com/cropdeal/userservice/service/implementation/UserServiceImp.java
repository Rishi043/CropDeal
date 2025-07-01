package com.cropdeal.userservice.service.implementation;


import com.cropdeal.userservice.repository.UserRepository;
import com.cropdeal.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    final private UserRepository authUserDAO;

    @Override
    public UserDetailsService userDetailService()
    {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username)
            {
                return authUserDAO.findByEmail(username)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found"));
            }
        };
    }
}