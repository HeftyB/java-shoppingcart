package com.lambdaschool.shoppingcart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter
{
//    static final String CLIENT_ID = System.getenv("OAUTHCLIENTID");
//    static final String CLIENT_SECRET = System.getenv("OAUTHCLIENTSECRET");

    static final String CLIENT_ID = "lambda-client";// lambda-client
    static final String CLIENT_SECRET = "lambda-secret";// lambda-secret

    static final String GRANT_TYPE_PASSWORD = "password";
    /**
     * We are using the client id and client security combination to authorize the client.
     * The client id and security can be base64 encoded into a single API key or code
     */
    static final String AUTHORIZATION_CODE = "authorization_code";
    static final String SCOPE_CODE = "read";
    static final String SCOPE_WRITE = "write";
    static final String TRUST = "trust";
    static final int ACCESS_TOKEN_VALIDITY_SECONDS = -1;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws
                                                                     Exception
    {
        configurer.inMemory()
            .withClient(CLIENT_ID)
            .secret(encoder.encode(CLIENT_SECRET))
            .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE)
            .scopes(SCOPE_CODE, SCOPE_WRITE, TRUST)
            .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws
                                                                            Exception
    {
        endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);
        endpoints.pathMapping("/oauth/token", "/login");
    }
}
