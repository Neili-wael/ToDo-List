package com.example.todo_list.Security.services;

import com.example.todo_list.Repository.UserRepository;
import com.example.todo_list.model.A_User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    UserRepository Ruser;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String Username) throws UsernameNotFoundException {
        A_User user = Ruser.findByUsername(Username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with this Mail : "+Username));
        return UserDetailsImp.build(user);
    }
}
