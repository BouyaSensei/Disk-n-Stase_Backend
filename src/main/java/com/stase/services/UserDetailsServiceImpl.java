package com.stase.services;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stase.entities.User;
import com.stase.repositories.UserRepository;

// adapte l'entite User de la bdd au format attendu par Spring Security :
// les utilisateurs crees via le CRUD peuvent s'authentifier sur l'API
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
            .findByUsernameIgnoreCase(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "Aucun utilisateur trouve avec le nom : " + username
                    )
            );
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword()) // hash BCrypt deja stocke en bdd
            .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
            .build();
    }
}
