package com.favour.web_lab4_backend.services;

import com.favour.web_lab4_backend.repositories.UserRepository;
import com.favour.web_lab4_backend.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Create a list of authorities/roles, assigning 'ROLE_USER' by default
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        // Use the complete UserDetails constructor with the assigned authorities
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true, // enabled - MUST be true for login to work
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities // Includes ROLE_USER
        );
    }

    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
