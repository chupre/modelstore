package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.users.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserProfilePermissionEvaluator {
    private final UserProfileService profileService;

    public boolean hasAccess(Long userId) {
        var profile = profileService.getUserProfile(userId);
        return profile.getUserId().equals(User.getCurrentUserId()) || User.isCurrentUserAdmin();
    }
}
