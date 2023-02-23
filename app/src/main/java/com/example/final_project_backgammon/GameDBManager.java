package com.example.final_project_backgammon;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameDBManager {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref= db.getReference("accounts");

    private CallBack_playTurnProtocol callBack_playTurnProtocol;

    private CallBack_waitTurnProtocol callBack_waitTurnProtocol;

    private CallBack_updateChipsProtocol callBack_updateChipsProtocol;
    public GameDBManager(){}

    public void setCallBack_playTurnProtocol(CallBack_playTurnProtocol callBack_playTurnProtocol) {
        this.callBack_playTurnProtocol = callBack_playTurnProtocol;
    }

    public void setCallBack_waitTurnProtocol(CallBack_waitTurnProtocol callBack_waitTurnProtocol) {
        this.callBack_waitTurnProtocol = callBack_waitTurnProtocol;
    }

    public void setCallBack_updateChipsProtocol(CallBack_updateChipsProtocol callBack_updateChipsProtocol) {
        this.callBack_updateChipsProtocol = callBack_updateChipsProtocol;
    }
    //public void writeTurnToDB(Room room){ //TODO after player finished round he should update other users turn
    //    //ref.child("waitingList").child(room.getRoomId()).child("game").child("turn")
    //}

    public void readTurnFromDB(Room room, UserDetails currentUser){
        ref.child("waitingList").child(room.getRoomId()).child("game").child("turn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.getValue(Integer.class) == 0)
                    {
                        if(currentUser.getId().equals(room.getRoomId())){
                            //host turn to play
                            callBack_playTurnProtocol.playTurn(currentUser);
                        }
                        else {
                            callBack_waitTurnProtocol.waitTurn(currentUser);
                        }
                    }

                    else if(snapshot.getValue(Integer.class) == 1){
                        if(currentUser.getId().equals((room.getPlayer2().getId()))){
                            callBack_playTurnProtocol.playTurn(currentUser);

                        }
                        else {
                            callBack_waitTurnProtocol.waitTurn(currentUser);
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void writeBoardChangesToDB(Room room) {
        ref
                .child("waitingList")
                .child(room.getRoomId())
                .child("game")
                .setValue(room.getGame());
    }

    public void updateTurn(Room room, int i) {
        ref
                .child("waitingList")
                .child(room.getRoomId())
                .child("game")
                .child("turn")
                .setValue(i);
    }

    public void readUpdatedBoard(Room room) {
        ref
                .child("waitingList")
                .child(room.getRoomId())
                .child("game")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            room.setGame(snapshot.getValue(BoardGame.class));
                            if(room.getGame().getTurnOff() != null)
                                callBack_updateChipsProtocol.updateChips(room);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void initiateTurn(Room room) {
        ref
                .child("waitingList")
                .child(room.getRoomId())
                .child("game")
                .child("turn")
                .setValue(0);
    }
}
