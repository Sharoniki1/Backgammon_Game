package com.example.final_project_backgammon;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomsDBManager {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref= db.getReference("accounts");

    private WaitingRooms allRooms;
    private UserDetails theUser;

    private CallBack_setAdapterProtocol callBack_setAdapterProtocol;
    private CallBack_hostGameProtocol callBack_hostGameProtocol;



    public RoomsDBManager(){}

    public void setCallBack_hostGameProtocol(CallBack_hostGameProtocol callBack_hostGameProtocol) {
        this.callBack_hostGameProtocol = callBack_hostGameProtocol;
    }

    public void saveRoomToDB(Room room){
        ref.child("waitingList").child(room.getRoomId()).setValue(room);
    }

    public void readRoomsFromDB(){
        allRooms = new WaitingRooms();
        ref.child("waitingList").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                        Room room = roomSnapshot.getValue(Room.class);
                        if(!roomSnapshot.getKey().equals(theUser.getId())){
                            allRooms.getWaitingList().put(roomSnapshot.getKey(), room);
                        }
                    }
                }
                ArrayList<Room> rooms = new ArrayList<Room>(allRooms.getWaitingList().values());
                callBack_setAdapterProtocol.setAdapter(rooms);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void setTheUser(UserDetails theUser) {
        this.theUser = theUser;
    }

    public void setCallBack_setAdapterProtocol(CallBack_setAdapterProtocol callBack_setAdapterProtocol) {
        this.callBack_setAdapterProtocol = callBack_setAdapterProtocol;
    }

    public void updateRoom(Room room) {
        ref.child("waitingList").child(room.getRoomId()).child("player2").setValue(room.getPlayer2());
        ref.child("waitingList").child(room.getRoomId()).child("ready").setValue(true);

    }

    public void readReady(Room room) {
        ref.child("waitingList").child(room.getRoomId()).child("ready").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && (boolean)snapshot.getValue() == true){
                    readPlayer2(room);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readPlayer2(Room room) {
        ref.child("waitingList").child(room.getRoomId()).child("player2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                room.setPlayer2(snapshot.getValue(UserDetails.class));
                // initiateTurn(room);
                callBack_hostGameProtocol.hostOpenGame(room);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//
//    private void initiateTurn(Room room) {
//        ref
//                .child("waitingList")
//                .child(room.getRoomId())
//                .child("game")
//                .child("turn")
//                .setValue(0);
//    }
}
