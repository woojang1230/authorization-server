package com.woojang.service.authorizationserver.auth.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.woojang.service.authorizationserver.model.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationProviderService implements AuthenticationProvider {
    private final JpaUserDetailsService userDetailsService;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final CustomUserDetails user = this.userDetailsService.loadUserByUsername(username);
        return makeUserAuthentication(user, password);
    }

    private Authentication makeUserAuthentication(final CustomUserDetails user, final String password) {
        return checkPassword(user, password, bCryptPasswordEncoder);
    }

    private Authentication checkPassword(final CustomUserDetails user, final String rawPassword,
                                         final PasswordEncoder encoder) {
        if (encoder.matches(rawPassword, user.getPassword())) {
            return makeAuthenticationToken(user);
        }
        throw new BadCredentialsException("Bad credentials");
    }

    private UsernamePasswordAuthenticationToken makeAuthenticationToken(final CustomUserDetails user) {
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
