package com.byllameister.modelstore.sellers;

import com.byllameister.modelstore.auth.VerifiedAuthorizationManager;
import com.byllameister.modelstore.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import static org.springframework.security.authorization.AuthorizationManagers.allOf;
import static org.springframework.security.authorization.AuthorizationManagers.anyOf;
import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;

@Component
public class SellerSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry r) {
        r.requestMatchers(HttpMethod.POST, "/sellers")
                .access(anyOf(
                        allOf(
                                VerifiedAuthorizationManager.requireVerified(),
                                hasRole("BUYER")
                        ),
                        hasRole("ADMIN")
                ));

        r.requestMatchers("/sellers/**")
                .access(anyOf(
                        allOf(
                                VerifiedAuthorizationManager.requireVerified(),
                                hasRole("SELLER")
                        ),
                        hasRole("ADMIN")
                ));
    }
}

