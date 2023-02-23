package com.example.final_project_backgammon;

import java.util.HashMap;

public class WaitingRooms {
    private HashMap<String, Room> waitingList;

    public WaitingRooms(){
        waitingList = new HashMap<>();

    }


    public HashMap<String, Room> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(HashMap<String, Room> waitingList) {
        this.waitingList = waitingList;
    }
}
