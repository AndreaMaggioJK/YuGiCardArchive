package com.YuGiCardArchive.YuGiCardArchive.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.YuGiCardArchive.YuGiCardArchive.model.Card;
import com.YuGiCardArchive.YuGiCardArchive.service.CardAlreadyExistsException;
import com.YuGiCardArchive.YuGiCardArchive.service.CardsService;

@RestController
@RequestMapping("/cards")
public class CardsController {

    private final CardsService cardsService;

    public CardsController(CardsService cardsService){
        this.cardsService = cardsService;
    }

    /**
     * Retrieves all cards.
     *
     * @return a ResponseEntity containing the list of cards if successful, or an appropriate error response
     */
    @GetMapping("/all")
    public ResponseEntity<List<Card>> getAllCards () {
        List<Card> cards = cardsService.findAllCards();
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    /**
     * Retrieves the next number of cards.
     *
     * @param number the number of cards to retrieve
     * @return the list of next cards
     */
    @GetMapping("/getTop")
        public List<Card> getNextCards(@RequestParam("number") int number) {
        return cardsService.getNextCards(number);
    }
    

   /**
     * Adds a new card.
     * The "s" in the path represents that the method is protected by authentication
     * 
     * @param card the card to add
     * @return a ResponseEntity containing the added card if successful, or an appropriate error response
     *         Possible error responses:
     *         - HttpStatus.CREATED (201): The card was successfully added.
     *         - HttpStatus.CONFLICT (409): If the card already exists.
     */
    @PostMapping("/s/add")
    public ResponseEntity<Card> addCard(@RequestBody Card card) {
        try{
            Card newCard = cardsService.addCard(card);
            return new ResponseEntity<>(newCard, HttpStatus.CREATED);
        }catch(CardAlreadyExistsException e){
            // If the card already exists, return conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Deletes a card based on its name.
     *
     * @param name The name of the card to be deleted.
     * @return A ResponseEntity indicating whether the deletion was successful (true) or not (false).
     */
    @DeleteMapping("/s/delete")
    public ResponseEntity<Boolean> deleteCard(@RequestParam("name") String name) {
        boolean deletionSuccessful = cardsService.deleteCardByName(name);
        if (deletionSuccessful) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Retrieves a list of cards based on filter criteria.
     *
     * @param name           (optional) The name of the card.
     * @param attribute      (optional) The attribute of the card.
     * @param icon           (optional) The icon of the card.
     * @param monsterType    (optional) The monster type of the card.
     * @param cardType       (optional) The card type.
     * @param levelRankFrom  (optional) The minimum level/rank of the card.
     * @param levelRankTo    (optional) The maximum level/rank of the card.
     * @param pendulumFrom   (optional) The minimum pendulum scale of the card.
     * @param pendulumTo     (optional) The maximum pendulum scale of the card.
     * @param linkFrom       (optional) The minimum link rating of the card.
     * @param linkTo         (optional) The maximum link rating of the card.
     * @param atkFrom        (optional) The minimum ATK of the card.
     * @param atkTo          (optional) The maximum ATK of the card.
     * @param defFrom        (optional) The minimum DEF of the card.
     * @param defTo          (optional) The maximum DEF of the card.
     * @return A list of cards that match the provided filter criteria.
     */
    @GetMapping("/filter")
    public List<Card> getCardsByFilter(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) String attribute,
                                       @RequestParam(required = false) String icon,
                                       @RequestParam(required = false) String monsterType,
                                       @RequestParam(required = false) String cardType,
                                       @RequestParam(required = false) Integer levelRankFrom,
                                       @RequestParam(required = false) Integer levelRankTo,
                                       @RequestParam(required = false) Integer pendulumFrom,
                                       @RequestParam(required = false) Integer pendulumTo,
                                       @RequestParam(required = false) Integer linkFrom,
                                       @RequestParam(required = false) Integer linkTo,
                                       @RequestParam(required = false) Integer atkFrom,
                                       @RequestParam(required = false) Integer atkTo,
                                       @RequestParam(required = false) Integer defFrom,
                                       @RequestParam(required = false) Integer defTo) {
        List<Card> res = cardsService.getCardsByFilter(name, attribute, icon, monsterType, cardType,
                levelRankFrom, levelRankTo, pendulumFrom, pendulumTo,
                linkFrom, linkTo, atkFrom, atkTo, defFrom, defTo);
        System.out.println("RESULT QUERY: "+ res);
        return res;
    }

    
}
