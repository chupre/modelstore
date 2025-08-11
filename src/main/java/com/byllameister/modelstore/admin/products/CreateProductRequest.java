package com.byllameister.modelstore.admin.products;

import com.byllameister.modelstore.upload.FileNotEmpty;
import com.byllameister.modelstore.upload.FileSize;
import com.byllameister.modelstore.upload.FileType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class CreateProductRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @FileNotEmpty(message = "Image is required and must be not empty")
    @FileType(
            allowedTypes = {
                    "image/png",
                    "image/jpeg",
                    "image/webp"
            },
            message = "Image type is not supported"
    )
    @FileSize(maxBytes = 5 * 1024 * 1024)
    private MultipartFile previewImage;

    @FileNotEmpty(message = "Model file is required and must be not empty")
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

    @NotNull(message = "Owner is required")
    private Long ownerId;

    @NotNull(message = "Category is required")
    private Long categoryId;
}
