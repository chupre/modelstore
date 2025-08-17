package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.users.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserProfilePermissionEvaluator {
    private final UserProfileService profileService;

    public boolean hasAccess(Long profileId) {
        var userId = profileService.getUserProfile(profileId).getUserId();
        return userId.equals(User.getCurrentUserId()) || User.isCurrentUserAdmin();
    }
}
