package com.example.final_project_backgammon;

import java.util.ArrayList;
import java.util.List;

public class ChipsRow {
    ArrayList<ChipRow> browns = new ArrayList<>();
    ArrayList<ChipRow> whites = new ArrayList<>();



    public ChipsRow() {}

    public ArrayList<ChipRow> getBrowns() {
        return browns;
    }

    public void setBrowns(ArrayList<ChipRow> browns) {
        this.browns = browns;
    }

    public ArrayList<ChipRow> getWhites() {
        return whites;
    }

    public void setWhites(ArrayList<ChipRow> whites) {
        this.whites = whites;
    }
}
