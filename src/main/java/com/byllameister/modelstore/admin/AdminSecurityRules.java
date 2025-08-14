package com.byllameister.modelstore.admin;

import com.byllameister.modelstore.auth.VerifiedAuthorizationManager;
import com.byllameister.modelstore.common.SecurityRules;
import com.byllameister.modelstore.users.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AdminSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .requestMatchers("/admin/**").access(VerifiedAuthorizationManager.requireVerified());

    }
}
