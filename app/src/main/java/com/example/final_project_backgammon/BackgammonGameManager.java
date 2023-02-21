package com.example.final_project_backgammon;

import static com.example.final_project_backgammon.ChipRow.COLOR.BROWN;
import static com.example.final_project_backgammon.ChipRow.COLOR.WHITE;

import java.util.ArrayList;

public class BackgammonGameManager {

    public BackgammonGameManager(){};
    private int currentDice;
    private int otherDice;

    private boolean roundFinished = false;

    private int[] upperBoard = new int[13];  // counter of chips in each column in upper board
    private int[] bottomBoard = new int[13]; // counter of chips in each column in bottom board

    private ChipRow.COLOR[] upperChips = new ChipRow.COLOR[13];  // arrays for color of chips in each column
    private ChipRow.COLOR[] bottomChips = new ChipRow.COLOR[13]; // arrays for color of chips in each column
    private ArrayList<ChipsRow> chipsRows;

    public ArrayList<ChipsRow> getChipsRows() {
        return chipsRows;
    }

    public void setChipsRows(ArrayList<ChipsRow> chipsRows) {
        this.chipsRows = chipsRows;
    }

    public int getOtherDice() {
        return otherDice;
    }

    public void setOtherDice(int otherDice) {
        this.otherDice = otherDice;
    }

    public int getCurrentDice() {
        return currentDice;
    }

    public void setCurrentDice(int currentDice) {
        this.currentDice = currentDice;
    }

    public boolean isRoundFinished() {
        return roundFinished;
    }

    public void setRoundFinished(boolean roundFinished) {
        this.roundFinished = roundFinished;
    }

    public boolean checkMovement(ChipRow chipRow, boolean direction) {
        int tempCol;
        if (direction == false && chipRow.getColor() == ChipRow.COLOR.BROWN) { // brown chip is on upper board
            tempCol = chipRow.getY() - getCurrentDice();
            if(handleColLeft(chipRow, tempCol, 0))
                return true;
        }
        if (direction == false && chipRow.getColor() == ChipRow.COLOR.WHITE) { // white chip in on bottom board
            tempCol = chipRow.getY() - getCurrentDice();
            if(handleColLeft(chipRow, tempCol, 0))
                return true;
        }
        if (direction == true && chipRow.getColor() == ChipRow.COLOR.BROWN) { // brown chip is on bottom board
            tempCol = chipRow.getY() + getCurrentDice();
            if(handleColRight(chipRow, tempCol, 12))
                return true;
        }
        if (direction == true && chipRow.getColor() == ChipRow.COLOR.WHITE) { // white chip is on upper board
            tempCol = chipRow.getY() + getCurrentDice();
            if(handleColRight(chipRow, tempCol, 12))
                return true;
        }
    return false;
    }

    private boolean handleColRight(ChipRow chipRow, int tempCol, int limit) {
        if (chipRow.getY() < 5 && tempCol >= 6)
            tempCol++;
        if (tempCol > limit) { // limit is 12
            int rightMove = limit - chipRow.getY();
            int goLeft = currentDice - rightMove;
            tempCol = limit - goLeft + 1;
            if(tempCol == 6) {
                tempCol++;
            }
            if(chipRow.getColor() == WHITE) {
                // check validation - for white chipRow in upper board and new position in bottomBoard
                if(checkMoveValidationForRightAndWhiteDifferentBoardPosition(chipRow, tempCol))
                    return true;
            }
            if(chipRow.getColor() == BROWN) {
                // check validation - for brown chipRow in bottomBoard and new position in upperBoard
                if(checkMoveValidationForRightAndBrownDifferentBoardPosition(chipRow, tempCol))
                    return true;
            }
        } else { // if (tempCol <= limit)
            if(chipRow.getColor() == WHITE) {
                // check validation - for white chipRow in upperBoard and new position still in upperBoard
                if(checkMoveValidationForRightAndWhiteSameBoardPosition(chipRow, tempCol))
                    return true;
            }
            if(chipRow.getColor() == BROWN) {
                // check validation - for brown chipRow in bottomBoard and new position still in bottomBoard
                if(checkMoveValidationForRightAndBrownSameBoardPosition(chipRow, tempCol))
                    return true;
            }
        }
        return false;
    }

    private boolean checkMoveValidationForRightAndWhiteDifferentBoardPosition(ChipRow chipRow, int tempCol) {
        if (bottomBoard[tempCol] > 1 && bottomChips[tempCol] != chipRow.getColor()) { // not valid step
            return false;
        }
        if (bottomBoard[tempCol] == 0) { // tempCol is empty
            bottomBoard[tempCol]++;
            bottomChips[tempCol] = chipRow.getColor();
            updateUpper(chipRow, tempCol);
            updateBottomAndWhite(tempCol);
            return true;
        }
        if (bottomBoard[tempCol] == 1) { // tempCol has one chip
            if (bottomChips[tempCol] != chipRow.getColor()) { // white will eat brown
                bottomChips[tempCol] = chipRow.getColor();
                updateUpper(chipRow, tempCol);
                updateBottomAndWhite(tempCol);
                chipsRows.get(32).getBrowns().get(tempCol).setIsVisible(true); // update the eaten chip
                return true;
            }
            else { // upperChips[tempCol] = chipRow.getColor()
                bottomBoard[tempCol]++;
                updateUpper(chipRow, tempCol);
                updateBottomAndWhite(tempCol);
                return true;
            }
        }
        if(bottomBoard[tempCol] > 1) { // it can't be with different colors (checked it already)
            bottomBoard[tempCol]++;
            updateUpper(chipRow, tempCol);
            updateBottomAndWhite(tempCol);
            return true;
        }
        return false;
    }
    private boolean checkMoveValidationForRightAndBrownDifferentBoardPosition(ChipRow chipRow, int tempCol) {
        if(upperBoard[tempCol] > 1 && upperChips[tempCol] != chipRow.getColor()) { // not valid step
            return false;
        }
        if(upperBoard[tempCol] == 0) { // tempCol is empty
            upperBoard[tempCol]++;
            upperChips[tempCol] = chipRow.getColor();
            updateBottom(chipRow, tempCol);
            updateUpperAndBrown(tempCol);
            return true;
        }
        if(upperBoard[tempCol] == 1) { // tempCol has one chip
            if (upperChips[tempCol] != chipRow.getColor()) { // brown will eat white
                upperChips[tempCol] = chipRow.getColor();
                updateBottom(chipRow, tempCol);
                updateUpperAndBrown(tempCol);
                chipsRows.get(32).getWhites().get(tempCol).setIsVisible(true); // update the eaten chip
                return true;
            }
            else { // bottomChips[tempCol] = chipRow.getColor()
                upperBoard[tempCol]++;
                updateBottom(chipRow, tempCol);
                updateUpperAndBrown(tempCol);
                return true;
            }
        }
        if(upperBoard[tempCol] > 1) { // it can't be with different colors (checked it already)
            upperBoard[tempCol]++;
            updateBottom(chipRow, tempCol);
            updateUpperAndBrown(tempCol);
            return true;
        }
        return false;
    }
    private boolean checkMoveValidationForRightAndWhiteSameBoardPosition(ChipRow chipRow, int tempCol) {
        if(upperBoard[tempCol] > 1 && upperChips[tempCol] != chipRow.getColor()) { // not valid step
            return false;
        }
        if(upperBoard[tempCol] == 0) { // tempCol is empty
            upperBoard[tempCol]++;
            upperChips[tempCol] = chipRow.getColor();
            updateUpper(chipRow, tempCol);
            updateUpperAndWhite(tempCol);
            return true;
        }
        if(upperBoard[tempCol] == 1) { // tempCol has one chip
            if (upperChips[tempCol] != chipRow.getColor()) { // white will eat brown
                upperChips[tempCol] = chipRow.getColor();
                updateUpper(chipRow, tempCol);
                updateUpperAndWhite(tempCol);
                chipsRows.get(32).getBrowns().get(tempCol).setIsVisible(true); // update the eaten chip
                return true;
            }
            else { // upperChips[tempCol] = chipRow.getColor()
                upperBoard[tempCol]++;
                updateUpper(chipRow, tempCol);
                updateUpperAndWhite(tempCol);
                return true;
            }
        }
        if(upperBoard[tempCol] > 1) { // it can't be with different colors (check it already)
            upperBoard[tempCol]++;
            updateUpper(chipRow, tempCol);
            updateUpperAndWhite(tempCol);
            return true;
        }
        return false;
    }
    private boolean checkMoveValidationForRightAndBrownSameBoardPosition(ChipRow chipRow, int tempCol) {
        if (bottomBoard[tempCol] > 1 && bottomChips[tempCol] != chipRow.getColor()) { // not valid step
            return false;
        }
        if (bottomBoard[tempCol] == 0) { // tempCol is empty
            bottomBoard[tempCol]++;
            bottomChips[tempCol] = chipRow.getColor();
            updateBottom(chipRow, tempCol);
            updateBottomAndBrown(tempCol);
            return true;
        }
        if (bottomBoard[tempCol] == 1) { // tempCol has one chip
            if (bottomChips[tempCol] != chipRow.getColor()) { // white will eat brown
                bottomChips[tempCol] = chipRow.getColor();
                updateBottom(chipRow, tempCol);
                updateBottomAndBrown(tempCol);
                chipsRows.get(32).getWhites().get(tempCol).setIsVisible(true); // update the eaten chip
                return true;
            }
            else { // bottomChips[tempCol] = chipRow.getColor()
                bottomBoard[tempCol]++;
                updateBottom(chipRow, tempCol);
                updateBottomAndBrown(tempCol);
                return true;
            }
        }
        if(bottomBoard[tempCol] > 1) { // it can't be with different colors (check it already)
            bottomBoard[tempCol]++;
            updateBottom(chipRow, tempCol);
            updateBottomAndBrown(tempCol);
            return true;
        }
        return false;
    }


    private boolean handleColLeft(ChipRow chipRow, int tempCol, int limit) {
        if(tempCol < limit) // limit is 0
            return false;
        if(chipRow.getY() > 5 && tempCol <= 6) {
            tempCol--;
        }
        if(chipRow.getColor() == WHITE) {
            if(checkMoveValidationForLeftAndWhite(chipRow, tempCol))
                return true;
        }
        if(chipRow.getColor() == BROWN) {
            if(checkMoveValidationForLeftAndBrown(chipRow, tempCol))
                return true;
            }
        return false;
    }

    private boolean checkMoveValidationForLeftAndWhite(ChipRow chipRow, int tempCol) {
        if (bottomBoard[tempCol] > 1 && bottomChips[tempCol] != chipRow.getColor()) { // not valid step
            return false;
        }
        if (bottomBoard[tempCol] == 0) { // tempCol is empty
            bottomBoard[tempCol]++;
            bottomChips[tempCol] = chipRow.getColor();
            updateBottom(chipRow, tempCol);
            updateBottomAndWhite(tempCol);
            return true;
        }
        if (bottomBoard[tempCol] == 1) { // tempCol has one chip
            if (bottomChips[tempCol] != chipRow.getColor()) { // white will eat brown
                bottomChips[tempCol] = chipRow.getColor();
                updateBottom(chipRow, tempCol);
                updateBottomAndWhite(tempCol);
                chipsRows.get(32).getBrowns().get(tempCol).setIsVisible(true); // update the eaten chip
                return true;
            } else { // bottomChips[tempCol] = chipRow.getColor()
                bottomBoard[tempCol]++;
                updateBottom(chipRow, tempCol);
                updateBottomAndWhite(tempCol);
                return true;
            }
        }
        if (bottomBoard[tempCol] > 1) { // it can't be with different colors (check it already)
            bottomBoard[tempCol]++;
            updateBottom(chipRow, tempCol);
            updateBottomAndWhite(tempCol);
            return true;
        }
        return false;
    }
    private boolean checkMoveValidationForLeftAndBrown(ChipRow chipRow, int tempCol) {
        if(upperBoard[tempCol] > 1 && upperChips[tempCol] != chipRow.getColor()) { // not valid step
            return false;
        }
        if(upperBoard[tempCol] == 0) { // tempCol is empty
            upperBoard[tempCol]++;
            upperChips[tempCol] = chipRow.getColor();
            updateUpper(chipRow, tempCol);
            updateUpperAndBrown(tempCol);
            return true;
        }
        if(upperBoard[tempCol] == 1) { // tempCol has one chip
            if(upperChips[tempCol] != chipRow.getColor()) { // white will eat brown
                upperChips[tempCol] = chipRow.getColor();
                updateUpper(chipRow, tempCol);
                updateUpperAndBrown(tempCol);
                chipsRows.get(32).getWhites().get(tempCol).setIsVisible(true); // update the eaten chip
                return true;
            }
            else { // upperChips[tempCol] = chipRow.getColor()
                upperBoard[tempCol]++;
                updateUpper(chipRow, tempCol);
                updateUpperAndBrown(tempCol);
                return true;
            }
        }
        if(upperBoard[tempCol] > 1) { // it can't be with different colors (check it already)
            upperBoard[tempCol]++;
            updateUpper(chipRow, tempCol);
            updateUpperAndBrown(tempCol);
            return true;
        }
        return false;
    }

    private void updateBottom(ChipRow chipRow, int tempCol) {
        bottomBoard[chipRow.getY()]--;
        if(bottomBoard[chipRow.getY()] == 0)
            bottomChips[chipRow.getY()] = null;
        chipRow.setIsVisible(false);
    }
    private void updateUpper(ChipRow chipRow, int tempCol) {
        upperBoard[chipRow.getY()]--;
        if(upperBoard[chipRow.getY()] == 0)
            upperChips[chipRow.getY()] = null;
        chipRow.setIsVisible(false);
    }

    private void updateUpperAndWhite(int tempCol) {
        chipsRows.get(upperBoard[tempCol] - 1).getWhites().get(tempCol).setIsVisible(true);
    }

    private void updateBottomAndWhite(int tempCol) {
            chipsRows.get(31 - bottomBoard[tempCol]).getWhites().get(tempCol).setIsVisible(true);
    }

    private void updateUpperAndBrown(int tempCol) {
        chipsRows.get(upperBoard[tempCol] - 1).getBrowns().get(tempCol).setIsVisible(true);
    }

    private void updateBottomAndBrown(int tempCol) {
        chipsRows.get(31 - bottomBoard[tempCol]).getBrowns().get(tempCol).setIsVisible(true);
    }

    public void init() {
        chipsRows = new ArrayList<>();
        for(int x = 0; x < 32; x++) {
            ChipsRow tempChipsRow = new ChipsRow();
            setChips(x, tempChipsRow.getWhites(), WHITE);
            setChips(x, tempChipsRow.getBrowns(), BROWN);
            chipsRows.add(tempChipsRow);
        }
        initGame();
    }

    private void initGame() {
        for (int i = 0; i < chipsRows.size(); i ++) {
            if (i == 0 || i == 1) {
                setChipsVisibility(0, chipsRows.get(i).getWhites());
                setChipsVisibility(5, chipsRows.get(i).getBrowns());
                setChipsVisibility(8, chipsRows.get(i).getBrowns());
                setChipsVisibility(12, chipsRows.get(i).getWhites());
                upperBoard[0]++;
                upperBoard[5]++;
                upperBoard[8]++;
                upperBoard[12]++;
                if(upperChips[0] == null)
                    upperChips[0] = WHITE;
                if(upperChips[5] == null)
                    upperChips[5] = BROWN;
                if(upperChips[8] == null)
                    upperChips[8] = BROWN;
                if ((upperChips[12]) == null)
                    upperChips[12] = WHITE;
            }
            if(i == 2) {
                setChipsVisibility(5, chipsRows.get(i).getBrowns());
                setChipsVisibility(8, chipsRows.get(i).getBrowns());
                setChipsVisibility(12, chipsRows.get(i).getWhites());
                upperBoard[5]++;
                upperBoard[8]++;
                upperBoard[12]++;
            }
            if(i == 3 || i == 4) {
                setChipsVisibility(5, chipsRows.get(i).getBrowns());
                setChipsVisibility(12, chipsRows.get(i).getWhites());
                upperBoard[5]++;
                upperBoard[12]++;
            }
            if(i == 29 || i == 30) {
                setChipsVisibility(0, chipsRows.get(i).getBrowns());
                setChipsVisibility(5, chipsRows.get(i).getWhites());
                setChipsVisibility(8, chipsRows.get(i).getWhites());
                setChipsVisibility(12, chipsRows.get(i).getBrowns());
                bottomBoard[0]++;
                bottomBoard[5]++;
                bottomBoard[8]++;
                bottomBoard[12]++;
                if(bottomChips[0] == null)
                    bottomChips[0] = BROWN;
                if(bottomChips[5] == null)
                    bottomChips[5] = WHITE;
                if(bottomChips[8] == null)
                    bottomChips[8] = WHITE;
                if(bottomChips[12] == null)
                    bottomChips[12] = BROWN;
            }
            if(i == 28) {
                setChipsVisibility(5, chipsRows.get(i).getWhites());
                setChipsVisibility(8, chipsRows.get(i).getWhites());
                setChipsVisibility(12, chipsRows.get(i).getBrowns());
                bottomBoard[5]++;
                bottomBoard[8]++;
                bottomBoard[12]++;
            }
            if(i == 27 || i == 26) {
                setChipsVisibility(5, chipsRows.get(i).getWhites());
                setChipsVisibility(12, chipsRows.get(i).getBrowns());
                bottomBoard[5]++;
                bottomBoard[12]++;
            }
        }
    }

    private void setChipsVisibility(int j, ArrayList<ChipRow> chipsColor) {
        chipsColor.get(j).setIsVisible(true);
    }

    private void setChips(int x, ArrayList<ChipRow> chips, ChipRow.COLOR color) {
        for(int y = 0; y < 13;y++)
            chips.add(new ChipRow().
                    setX(x).
                    setY(y).
                    setColor(color).
                    setIsVisible(false));
    }

    private boolean isChipsHome(int[] board, ChipRow.COLOR[] colors, ChipRow.COLOR color ) {
        int sum = 0;
        for(int i = 0; i < 6; i++) {
            if(colors[i] == color)
                sum+=board[i];
        }

        return sum == 15;
    }

}






