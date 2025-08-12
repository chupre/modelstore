package com.byllameister.modelstore.auth;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public class VerifiedAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final boolean requireValidated;

    private VerifiedAuthorizationManager(boolean requireValidated) {
        this.requireValidated = requireValidated;
    }

    public static VerifiedAuthorizationManager requireVerified() {
        return new VerifiedAuthorizationManager(true);
    }

    public static VerifiedAuthorizationManager requireUnverified() {
        return new VerifiedAuthorizationManager(false);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        var auth = authentication.get();
        if (!(auth.getPrincipal() instanceof CustomUserPrincipal principal)) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(principal.getVerified() == requireValidated);
    }
}
