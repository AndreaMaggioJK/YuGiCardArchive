package com.YuGiCardArchive.YuGiCardArchive.service;

public class CardAlreadyExistsException extends RuntimeException {
    public CardAlreadyExistsException(String message) {
        super(message);
    }
}
