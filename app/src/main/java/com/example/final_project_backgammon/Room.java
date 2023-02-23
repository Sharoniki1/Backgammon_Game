package com.example.final_project_backgammon;

import java.io.Serializable;
import java.util.HashMap;

public class Room implements Serializable {
    public String roomId;
    public UserDetails host;
    public UserDetails player2;
    public BoardGame game;
    public boolean ready= false; // if two players have joined room


    public Room(){
        ready= false;
        game = new BoardGame();
        //  players = new HashMap<>();
    }

    public String getRoomId() {
        return roomId;
    }

    public Room setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }

    public UserDetails getHost() {
        return host;
    }

    public Room setHost(UserDetails host) {
        this.host = host;
        return this;
    }

    public UserDetails getPlayer2() {
        return player2;
    }

    public Room setPlayer2(UserDetails player2) {
        this.player2 = player2;
        return this;
    }


    public BoardGame getGame() {
        return game;
    }

    public Room setGame(BoardGame game) {
        this.game = game;
        return this;
    }

    public boolean isReady() {
        return ready;
    }

    public Room setReady(boolean ready) {
        this.ready = ready;
        return this;
    }
}
