package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.upload.UploadService;
import com.byllameister.modelstore.users.DuplicateUsernameException;
import com.byllameister.modelstore.users.UserNotFoundException;
import com.byllameister.modelstore.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final UploadService uploadService;
    private final UserRepository userRepository;

    public UserProfileDto getUserProfile(Long userId) {
        var profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(UserProfileNotFoundException::new);
        return userProfileMapper.toDto(profile);
    }

    public UserProfileDto updateProfile(Long userId, UpdateUserProfileRequest request) throws IOException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException();
        }

        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);


        var profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(UserProfileNotFoundException::new);

        if (request.getAvatarImage() != null) {
            uploadService.deleteFile(profile.getAvatarUrl());
            var avatarUrl = uploadService.uploadImage(request.getAvatarImage());
            profile.setAvatarUrl(avatarUrl);
        }

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
            userRepository.save(user);

            profile.setName(request.getUsername());
        }

        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }

        userProfileRepository.save(profile);
        return userProfileMapper.toDto(profile);
    }
}
