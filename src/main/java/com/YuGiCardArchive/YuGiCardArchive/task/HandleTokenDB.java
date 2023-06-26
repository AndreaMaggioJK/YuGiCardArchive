package com.YuGiCardArchive.YuGiCardArchive.task;


import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.YuGiCardArchive.YuGiCardArchive.token.TokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HandleTokenDB {

    private final TokenRepository tokenRepository;

    /**
     * Scheduled method to delete expired tokens from the database.
     * This method runs every day at a fixed rate.
     */
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Runs every day
    public void deleteExpiredTokenInDB() {
        System.out.println("deleteExpiredTokenInDB at: "+new Date());
        tokenRepository.deleteExpiredTokens();
    }
}
