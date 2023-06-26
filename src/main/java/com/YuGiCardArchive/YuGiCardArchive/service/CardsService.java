package com.YuGiCardArchive.YuGiCardArchive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.YuGiCardArchive.YuGiCardArchive.model.Card;
import com.YuGiCardArchive.YuGiCardArchive.repo.CardsRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CardsService {
    
    @Autowired
    private final CardsRepo cardsRepo;

    
    public CardsService(CardsRepo cardsRepo){
        this.cardsRepo = cardsRepo;
    }

    /**
     * @param card
     * save card in the repository cardsRepo
     * @return card
     */
    public Card addCard(Card card) throws CardAlreadyExistsException {
    if ( cardsRepo.existsByName(card.getName())) {
        throw new CardAlreadyExistsException("Card already exists");
    }
        return cardsRepo.save(card);
    }

    public List<Card> findAllCards() {
        return cardsRepo.findAll();
    }
    
    public List<Card> getCardsByFilter(String name, String attribute, String icon, String monsterType, String cardType,
                                       Integer levelRankFrom, Integer levelRankTo, Integer pendulumFrom, Integer pendulumTo,
                                       Integer linkFrom, Integer linkTo, Integer atkFrom, Integer atkTo, Integer defFrom,
                                       Integer defTo) {
        return cardsRepo.getCardByFilter(name, attribute, icon, monsterType, cardType, levelRankFrom, 
        levelRankTo, pendulumFrom, pendulumTo, linkFrom, linkTo, atkFrom, atkTo, defFrom, defTo);
    }

    /**
     * Retrieves the next 25 cards based on the provided page number.
     *
     * @param pageNumber The page number indicating which set of 25 cards to retrieve.
     * @return A list of cards representing the next 25 cards.
     */
    public List<Card> getNextCards(int pageNumber) {
        // Calculate the starting index for the page using the pageNumber
        int startIndex = pageNumber * 25;

        // Create a Pageable object with the starting index and page size of 1
        Pageable pageable = PageRequest.of(startIndex, 25);

        // Retrieve the next 25 cards from the repository ordered by their IDs in ascending order
        return cardsRepo.findTop25ByOrderByIdAsc(pageable);
    }

    /**
     * Deletes a card by its name.
     *
     * @param name The name of the card to be deleted.
     * @return true if the card was successfully deleted, false otherwise.
     */
    public boolean deleteCardByName(String name) {
        try {
            // Check if a card with the given name exists in the repository
            if (cardsRepo.existsByName(name)) {
                // Delete the card by name
                cardsRepo.deleteByName(name);
                return true;
            }
            // If the card does not exist, return false
            return false;
        } catch (Exception e) {
            // If an exception occurs during the deletion process, return false
            return false;
        }
    }

}
