package com.byllameister.modelstore.products;

import com.byllameister.modelstore.upload.FileSize;
import com.byllameister.modelstore.upload.FileType;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class PatchProductRequest {
    @NotBlankIfNotNull
    private String title;

    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @FileType(
            allowedTypes = {
                    "image/png",
                    "image/jpeg",
                    "image/webp"
            },
            message = "Image type is not supported"
    )
    @FileSize(maxBytes = 5 * 1024 * 1024, message = "Image is too big. Max size is 5MB.")
    private MultipartFile previewImage;

    @FileType(
            allowedTypes = {
                    "application/sla",
                    "application/vnd.ms-pki.stl",
                    "model/stl",
                    "application/octet-stream"
            },  allowedExtensions = {"stl"},
            message = "Unsupported file type"
    )
    @FileSize(maxBytes = 200 * 1024 * 1024, message = "File is too big")
    private MultipartFile file;

    private Long categoryId;
}
