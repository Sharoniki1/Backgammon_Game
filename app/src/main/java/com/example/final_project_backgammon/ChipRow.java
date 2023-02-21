package com.example.final_project_backgammon;


public class ChipRow {

    public enum COLOR{BROWN, WHITE}
    private int x;
    private int y;
    private COLOR color;

    private boolean isVisible;

    public boolean getIsVisible() {
        return isVisible;
    }

    public ChipRow setIsVisible(boolean visible) {
        this.isVisible = visible;
        return this;
    }

    public ChipRow() {}

    public COLOR getColor() {
        return color;
    }

    public ChipRow setColor(COLOR color) {
        this.color = color;
        return this;
}

    public int getX() {
        return x;
    }

    public ChipRow setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public ChipRow setY(int y) {
        this.y = y;
        return this;
    }
}
