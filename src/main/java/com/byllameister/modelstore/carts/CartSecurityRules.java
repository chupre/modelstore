package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.common.SecurityRules;
import com.byllameister.modelstore.users.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CartSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                .requestMatchers(HttpMethod.GET, "/carts").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/carts/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/carts/**").hasRole(Role.ADMIN.name());
    }
}
