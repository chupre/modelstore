package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.upload.FileSize;
import com.byllameister.modelstore.upload.FileType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserProfileRequest {
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Username must contain only letters, digits, or underscores (no spaces or special characters)"
    )
    private String username;

    @FileType(
            allowedTypes = {
                    "image/png",
                    "image/jpeg",
                    "image/webp"
            },
            message = "Image type is not supported"
    )
    @FileSize(maxBytes = 5 * 1024 * 1024)
    private MultipartFile avatarImage;

    @Size(max = 500, message = "Bio can't be bigger than 500 characters")
    private String bio;
}
