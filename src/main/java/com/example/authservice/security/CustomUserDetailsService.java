package com.example.authservice.security;

import com.example.authservice.model.User;
import com.example.authservice.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User found = userService.getByEmailThrowsException(email);
            List<GrantedAuthority> grantedAuthorities = found.getRoles()
                    .stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new UserDetailsImpl(found.getEmail(), found.getPassword(), grantedAuthorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("No user found!");
        }
    }
}
