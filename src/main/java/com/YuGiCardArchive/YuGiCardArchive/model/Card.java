package com.YuGiCardArchive.YuGiCardArchive.model;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Card implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String attribute;
    @Column(nullable = false)
    String icon;
    @Column(nullable = false)
    String monsterType;
    @Column(nullable = false)
    String CardType;
    @Column(nullable = false)
    int LevelRank;
    @Column(nullable = false)
    int Pendulum;
    @Column(nullable = false)
    int Link;
    @Column(nullable = false)
    String EffectDescription;
    @Column(nullable = false)
    int ATK;
    @Column(nullable = false)
    int DEF;

    public Card(){}

    public Card(String name,String attribute, String icon, String monsterType, String CardType,
    int LevelRank, int Pendulum, int Link, String EffectDescription, int ATK, int DEF)
    {
        this.name = name; 
        this.attribute = attribute;
        this.icon = icon;
        this.monsterType = monsterType;
        this.CardType = CardType;
        this.LevelRank = LevelRank;
        this.Pendulum = Pendulum;
        this.Link = Link;
        this.EffectDescription = EffectDescription;
        this.ATK = ATK;
        this.DEF = DEF;
    }

    @Override
    public String toString(){
        return "Card{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", attribute='" + attribute + '\'' +
        ", icon='" + icon + '\'' +
        ", monsterType='" + monsterType + '\'' +
        ", CardType='" + CardType + '\'' +
        ", LevelRank='" + LevelRank + '\'' +
        ", Pendulum='" + Pendulum + '\'' +
        ", Link='" + Link + '\'' +
        ", EffectDescription='" + EffectDescription + '\'' +
        ", ATK='" + ATK + '\'' +
        ", DEF='" + DEF + '\'' +
        '}';
    }
}
