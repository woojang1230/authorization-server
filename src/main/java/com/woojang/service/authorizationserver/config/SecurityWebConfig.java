package com.woojang.service.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.woojang.service.authorizationserver.auth.service.AuthenticationProviderService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationProviderService authenticationProvider;
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.authenticationProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .mvcMatchers("/user/guest").permitAll()
                .mvcMatchers("/swagger-ui/**").permitAll()
                .mvcMatchers("/swagger-resources/**").permitAll()
                .mvcMatchers("/v3/api-docs").permitAll()
                .anyRequest().authenticated();
    }
}
