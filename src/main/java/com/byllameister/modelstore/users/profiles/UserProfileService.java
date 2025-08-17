package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.upload.UploadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final UploadService uploadService;

    public UserProfileDto getUserProfile(Long userId) {
        var profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(UserProfileNotFoundException::new);
        return  userProfileMapper.toDto(profile);
    }

    public UserProfileDto updateProfile(Long userId, UpdateUserProfileRequest request) throws IOException {
        var profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(UserProfileNotFoundException::new);

        String avatarUrl = null;
        if (request.getAvatarImage() != null) {
            uploadService.deleteFile(profile.getAvatarUrl());
            avatarUrl = uploadService.uploadImage(request.getAvatarImage());
        }

        profile.setAvatarUrl(avatarUrl);
        profile.setBio(request.getBio());

        userProfileRepository.save(profile);
        return userProfileMapper.toDto(profile);
    }
}
