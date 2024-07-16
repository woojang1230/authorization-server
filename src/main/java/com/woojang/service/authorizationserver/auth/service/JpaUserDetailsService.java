package com.woojang.service.authorizationserver.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.woojang.service.authorizationserver.model.CustomUserDetails;
import com.woojang.service.authorizationserver.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public CustomUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(CustomUserDetails::makeUser)
                .orElseThrow(() -> new UsernameNotFoundException("Problem during authentication!"));
    }
}
