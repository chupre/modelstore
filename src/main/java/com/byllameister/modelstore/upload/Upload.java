package com.byllameister.modelstore.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@AllArgsConstructor
@Data
@Builder
public class Upload {
    private MultipartFile file;
    private String directory;
    private long maxSizeMb;
    private Set<String> supportedMimeTypes;
}
