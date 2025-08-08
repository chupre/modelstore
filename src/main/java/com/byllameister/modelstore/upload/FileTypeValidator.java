package com.byllameister.modelstore.upload;

import io.jsonwebtoken.lang.Arrays;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {
    Set<String> allowedTypes;
    Set<String> allowedExtensions;

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.allowedTypes = new HashSet<>(Arrays.asList(constraintAnnotation.allowedTypes()));
        this.allowedExtensions = new HashSet<>(Arrays.asList(constraintAnnotation.allowedExtensions()));
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (allowedExtensions.isEmpty() || file == null)
            return true;

        return allowedTypes.contains(file.getContentType()) &&
                allowedExtensions.contains(getExtension(file));
    }

    private String getExtension(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            return ""; // No extension found
        }
        return originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
    }
}
