package com.controlefinanceiro.financas.security;

import com.controlefinanceiro.financas.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userService.findByEmail(email);
    }


    public UserDetails loadUserById(Long id) {
        return userService.findById(id);
    }
}
