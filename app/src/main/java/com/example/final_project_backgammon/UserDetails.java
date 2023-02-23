package com.example.final_project_backgammon;

import java.io.Serializable;

public class UserDetails implements Serializable {

    public enum CHIP{
        APP,
        WHITE,
        BROWN
    }

    private String id;
    private String name;
    private int totalGames;
    private int numOfWins;
    private float winLoseRatio;
    private int coins;

    private CHIP chipColor= CHIP.APP;


    public UserDetails(){
    }

    public String getId() {
        return id;
    }

    public UserDetails setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDetails setName(String name) {
        this.name = name;
        return this;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public UserDetails setTotalGames(int totalGames) {
        this.totalGames = totalGames;
        return this;
    }

    public int getNumOfWins() {
        return numOfWins;
    }

    public UserDetails setNumOfWins(int numOfWins) {
        this.numOfWins = numOfWins;
        return this;
    }

    public float getWinLoseRatio() {
        return winLoseRatio;
    }

    public UserDetails setWinLoseRatio(float winLoseRatio) {
        this.winLoseRatio = winLoseRatio;
        return this;
    }

    public int getCoins() {
        return coins;
    }

    public UserDetails setCoins(int coins) {
        this.coins = coins;
        return this;
    }

    public CHIP getChipColor() {
        return chipColor;
    }

    public UserDetails setChipColor(CHIP chipColor) {
        this.chipColor = chipColor;
        return this;
    }
}
