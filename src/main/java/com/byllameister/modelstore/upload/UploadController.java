package com.byllameister.modelstore.upload;

import com.byllameister.modelstore.common.ErrorDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@AllArgsConstructor
public class UploadController {
    private final UploadService uploadService;

    @PostMapping("/image")
    public ResponseEntity<UploadResponse> uploadImage(
            @RequestParam("image") MultipartFile file
    ) throws IOException {
        var upload = uploadService.uploadImage(file);
        return ResponseEntity.ok(upload);
    }

    @PostMapping("/model")
    public ResponseEntity<UploadResponse> uploadModel(
            @RequestParam("model") MultipartFile file
    ) throws IOException {
        var upload = uploadService.uploadModel(file);
        return ResponseEntity.ok(upload);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorDto> handleMissingServletRequestPartException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto("File is missing"));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDto> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(UnsupportedFileTypeException.class)
    public ResponseEntity<ErrorDto> handleUnsupportedFileExtension(UnsupportedFileTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(FileIsTooBigException.class)
    public ResponseEntity<ErrorDto> handleFileTooBigException(FileIsTooBigException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(FileIsEmptyException.class)
    public ResponseEntity<ErrorDto> handleFileIsEmptyException(FileIsEmptyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorDto> handleMultipartException(MultipartException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getMessage()));
    }
}
