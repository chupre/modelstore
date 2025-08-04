package com.byllameister.modelstore.upload;

public class FileIsTooBigException extends RuntimeException {
    public FileIsTooBigException() {
        super("File size is too big");
    }
}
