package com.byllameister.modelstore.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomUserPrincipal {
    private Long userId;
    private Boolean verified;
}
