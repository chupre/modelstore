package com.byllameister.modelstore.upload;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UploadService {
    public String uploadImage(MultipartFile file) throws IOException {
        return uploadFile(file, "/images/");
    }

    public String uploadModel(MultipartFile file) throws IOException {
        return uploadFile(file, "/models/");
    }

    private String uploadFile(MultipartFile file, String directory) throws IOException {
        String fileName = UUID.randomUUID().toString();
        Path uploadPath = Paths.get("uploads" + directory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return directory + fileName;
    }

    public void deleteFile(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get("uploads" + filePath));
    }
}
