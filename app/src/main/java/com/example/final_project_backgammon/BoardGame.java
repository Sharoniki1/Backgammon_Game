package com.example.final_project_backgammon;

import java.io.Serializable;

public class BoardGame implements Serializable {
    public int dice1;
    public int dice2;
    public Coordinate turnOff;
    public Coordinate turnOn;

    public Coordinate eaten;
    public int turn = -1;

    public BoardGame(){}


    public int getDice1() {
        return dice1;
    }

    public BoardGame setDice1(int dice1) {
        this.dice1 = dice1;
        return this;
    }

    public int getDice2() {
        return dice2;
    }

    public BoardGame setDice2(int dice2) {
        this.dice2 = dice2;
        return this;
    }

    public Coordinate getTurnOff() {
        return turnOff;
    }

    public BoardGame setTurnOff(Coordinate turnOff) {
        this.turnOff = turnOff;
        return this;
    }

    public Coordinate getTurnOn() {
        return turnOn;
    }

    public BoardGame setTurnOn(Coordinate turnOn) {
        this.turnOn = turnOn;
        return this;
    }

    public Coordinate getEaten() {
        return eaten;
    }

    public BoardGame setEaten(Coordinate eaten) {
        this.eaten = eaten;
        return this;
    }

    public int getTurn() {
        return turn;
    }

    public BoardGame setTurn(int turn) {
        this.turn = turn;
        return this;
    }
}
