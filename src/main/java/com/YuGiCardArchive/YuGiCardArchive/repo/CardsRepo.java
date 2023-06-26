package com.YuGiCardArchive.YuGiCardArchive.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.YuGiCardArchive.YuGiCardArchive.model.Card;

import java.util.List;


public interface CardsRepo extends JpaRepository<Card, Long> {

    @Query("SELECT c FROM Card c " +
            "WHERE (:name IS NULL OR c.name LIKE %:name%) " +
            "AND (:attribute IS NULL OR c.attribute = :attribute) " +
            "AND (:icon IS NULL OR c.icon = :icon) " +
            "AND (:monsterType IS NULL OR c.monsterType = :monsterType) " +
            "AND (:cardType IS NULL OR c.CardType = :cardType) " +
            "AND (:levelRankFrom IS NULL OR c.LevelRank >= :levelRankFrom) " +
            "AND (:levelRankTo IS NULL OR c.LevelRank <= :levelRankTo) " +
            "AND (:pendulumFrom IS NULL OR c.Pendulum >= :pendulumFrom) " +
            "AND (:pendulumTo IS NULL OR c.Pendulum <= :pendulumTo) " +
            "AND (:linkFrom IS NULL OR c.Link >= :linkFrom) " +
            "AND (:linkTo IS NULL OR c.Link <= :linkTo) " +
            "AND (:atkFrom IS NULL OR c.ATK >= :atkFrom) " +
            "AND (:atkTo IS NULL OR c.ATK <= :atkTo) " +
            "AND (:defFrom IS NULL OR c.DEF >= :defFrom) " +
            "AND (:defTo IS NULL OR c.DEF <= :defTo)")
    List<Card> getCardByFilter(@Param("name") String name, @Param("attribute") String attribute, @Param("icon") String icon,
    @Param("monsterType") String monsterType, @Param("cardType") String cardType,
    @Param("levelRankFrom") Integer levelRankFrom, @Param("levelRankTo") Integer levelRankTo,
    @Param("pendulumFrom") Integer pendulumFrom, @Param("pendulumTo") Integer pendulumTo,
    @Param("linkFrom") Integer linkFrom, @Param("linkTo") Integer linkTo,
    @Param("atkFrom") Integer atkFrom, @Param("atkTo") Integer atkTo,
    @Param("defFrom") Integer defFrom, @Param("defTo") Integer defTo);

    List<Card> findTop25ByOrderByIdAsc(Pageable pageable);

    boolean existsByName(String name);

    void deleteByName(String name);
}
