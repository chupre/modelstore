package com.byllameister.modelstore.upload;

public class FileIsEmptyException extends RuntimeException {
    public FileIsEmptyException() {
        super("File is empty");
    }
}
