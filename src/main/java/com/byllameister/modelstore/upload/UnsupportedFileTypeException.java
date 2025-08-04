package com.byllameister.modelstore.upload;

public class UnsupportedFileTypeException extends RuntimeException {
    public UnsupportedFileTypeException() {
        super("Unsupported file type");
    }
}
