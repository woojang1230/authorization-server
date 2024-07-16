package com.woojang.service.authorizationserver.config;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager manager;
    private final UserDetailsService userDetailsService;
    private final String publicKeyFile;
    private final String privateKeyFile;
    private final String algorithm;

    public AuthServerConfig(final PasswordEncoder bCryptPasswordEncoder, final AuthenticationManager manager,
                            @Value("${jwt.key.public-key}") final String publicKeyFile,
                            @Value("${jwt.key.private-key}") final String privateKeyFile,
                            @Value("${jwt.key.algorithm}") final String algorithm,
                            final UserDetailsService userDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.manager = manager;
        this.publicKeyFile = publicKeyFile;
        this.privateKeyFile = privateKeyFile;
        this.algorithm = algorithm;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret(this.bCryptPasswordEncoder.encode("secret"))
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(600)
                .refreshTokenValiditySeconds(1200)
                .scopes("read")
                .and()
                .withClient("gateway")
                .secret(this.bCryptPasswordEncoder.encode("gatewaysecret"))
                .and()
                .withClient("backend")
                .secret(this.bCryptPasswordEncoder.encode("backendsecret"));
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(this.manager)
                .userDetailsService(this.userDetailsService)
                .tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter());
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(new KeyPair(getPublicKey(), getPrivateKey()));
        return converter;
    }

    private PrivateKey getPrivateKey() {
        try {
            String privateKeyPEM = extractPrivateSecretText(this.privateKeyFile);
            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return keyFactory().generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PublicKey getPublicKey() {
        try {
            String publicKeyPEM = extractPublicSecretText(this.publicKeyFile);
            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return keyFactory().generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private KeyFactory keyFactory() throws NoSuchAlgorithmException {
        return KeyFactory.getInstance(this.algorithm);
    }

    private String extractPublicSecretText(final String pemKeyPath) throws IOException {
        return new String(getResource(pemKeyPath).getInputStream().readAllBytes())
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
    }

    private String extractPrivateSecretText(final String pemKeyPath) throws IOException {
        return new String(getResource(pemKeyPath).getInputStream().readAllBytes())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
    }

    private Resource getResource(String path) {
        if (Paths.get(path).isAbsolute()) {
            return new FileSystemResource(path);
        } else {
            return new ClassPathResource(path);
        }
    }
}
