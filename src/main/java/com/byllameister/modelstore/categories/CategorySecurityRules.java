package com.byllameister.modelstore.categories;

import com.byllameister.modelstore.common.SecurityRules;
import com.byllameister.modelstore.users.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CategorySecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/categories/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/categories/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole(Role.ADMIN.name());
    }
}
