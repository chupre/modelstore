package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.upload.FileSize;
import com.byllameister.modelstore.upload.FileType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserProfileRequest {
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
    private String bio;
}
