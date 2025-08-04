package com.byllameister.modelstore.upload;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UploadService {
    private final Set<String> ALLOWED_IMAGE_MIME_TYPES = Set.of(
            "image/png",
            "image/jpeg",
            "image/webp"
    );

    private final Set<String> ALLOWED_MODEL_MIME_TYPES = Set.of(
            "application/sla",
            "application/vnd.ms-pki.stl",
            "model/stl",
            "application/octet-stream"
    );

    private final Set<String> ALLOWED_MODEL_EXTENSIONS = Set.of("stl");

    public UploadResponse uploadImage(MultipartFile file) throws IOException {
        var upload = Upload.builder()
                .file(file)
                .directory("/images/")
                .maxSizeMb(5 * 1024 * 1024)
                .supportedMimeTypes(ALLOWED_IMAGE_MIME_TYPES)
                .build();

        return uploadFile(upload);
    }

    public UploadResponse uploadModel(MultipartFile file) throws IOException {
        var extension = getExtension(file);
        if (!ALLOWED_MODEL_EXTENSIONS.contains(extension)) {
            throw new UnsupportedFileTypeException();
        }

        var upload = Upload.builder()
                .file(file)
                .directory("/models/")
                .maxSizeMb(200 * 1024 * 1024)
                .supportedMimeTypes(ALLOWED_MODEL_MIME_TYPES)
                .build();

        return uploadFile(upload);
    }

    private UploadResponse uploadFile(Upload upload) throws IOException {
        var file = upload.getFile();

        if (file == null || file.getSize() == 0) {
            throw new FileIsEmptyException();
        }

        if (file.getContentType() == null ||
                !upload.getSupportedMimeTypes().contains(file.getContentType()))
        {
            throw new UnsupportedFileTypeException();
        }

        if (file.getSize() > upload.getMaxSizeMb()) {
            throw new FileIsTooBigException();
        }

        String fileName = UUID.randomUUID().toString();
        Path uploadPath = Paths.get("uploads" + upload.getDirectory());

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return new UploadResponse(upload.getDirectory() + "/" + fileName);
    }

    private String getExtension(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            return ""; // No extension found
        }
        return originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
    }
}
