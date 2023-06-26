package com.YuGiCardArchive.YuGiCardArchive.auth;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String message) {
        super(message);
    }
}
