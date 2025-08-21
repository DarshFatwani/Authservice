package org.example.service;

import org.example.entities.UserInfo;
import org.example.model.UserInfoDto;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Objects;

import java.util.UUID;

@Component
@RequiredArgsConstructor // generates a constructor for all final fields
public class UserDetailServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Entering loadUserByUsername...");
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("Username not found: {}", username);
            throw new UsernameNotFoundException("could not found user..!!");
        }
        log.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user);
    }

    public Boolean signupUser(UserInfoDto userInfoDto) {
        final String username = userInfoDto.getUsername().trim();
        final String email = userInfoDto.getEmail().trim().toLowerCase();

        if (userRepository.existsByUsername(username)) {
            log.warn("Signup failed: username already exists: {}", username);
            return false;
        }
        if (userRepository.existsByEmail(email)) {
            log.warn("Signup failed: email already exists: {}", email);
            return false;
        }

        final String encoded = passwordEncoder.encode(userInfoDto.getPassword());
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        String userId = UUID.randomUUID().toString();
        userRepository.save(new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>()));
        // pushEventToQueue
        return true;
    }


}
