package com.byllameister.modelstore.products.interaction;

import com.byllameister.modelstore.auth.VerifiedAuthorizationManager;
import com.byllameister.modelstore.common.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ProductInteractionSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.POST,"/interactions/products/**")
                .access(VerifiedAuthorizationManager.requireVerified());
        registry.requestMatchers(HttpMethod.DELETE,"/interactions/products/**")
                .access(VerifiedAuthorizationManager.requireVerified());
        registry.requestMatchers(HttpMethod.PUT,"/interactions/products/**")
                .access(VerifiedAuthorizationManager.requireVerified());
    }
}
