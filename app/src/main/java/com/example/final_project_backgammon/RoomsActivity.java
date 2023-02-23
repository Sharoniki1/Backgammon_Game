package com.example.final_project_backgammon;

import static com.example.final_project_backgammon.BackgammonGameActivity.KEY_ROOM;
import static com.example.final_project_backgammon.Menu_Activity.KEY_USER;
import static com.example.final_project_backgammon.UserDetails.CHIP.WHITE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RoomsActivity extends AppCompatActivity {

    private AppCompatImageView rooms_IMG_background;
    private RecyclerView rooms_LST_waitingrooms;
    private Button rooms_BTN_newroom;
    private Button rooms_BTN_menu;

    private ArrayList<Room> waitingList;

    private Adapter_Game adapter_game;

    UserDetails theUser;

    RoomsDBManager roomsDBManager;

    CallBack_startGameProtocol callBack_startGameProtocol = new CallBack_startGameProtocol() {
        @Override
        public void game(Room room) {
            theUser.setChipColor(WHITE);
            room.setPlayer2(theUser);
            roomsDBManager.updateRoom(room);
            Intent intent = new Intent(RoomsActivity.this, BackgammonGameActivity.class);
            intent.putExtra(KEY_ROOM, room);
            startActivity(intent);
            finish();
        }
    };

    CallBack_hostGameProtocol callBack_hostGameProtocol = new CallBack_hostGameProtocol() {
        @Override
        public void hostOpenGame(Room room) {
            Intent intent = new Intent(RoomsActivity.this, BackgammonGameActivity.class);
            intent.putExtra(KEY_ROOM, room);
            startActivity(intent);
            finish();
        }
    };


    CallBack_setAdapterProtocol callBack_setAdapterProtocol = new CallBack_setAdapterProtocol() {
        @Override
        public void setAdapter(ArrayList<Room> rooms) {
            waitingList = rooms;
            adapter_game = new Adapter_Game(RoomsActivity.this, waitingList);
            adapter_game.setCallBack_startGameProtocol(callBack_startGameProtocol);
            rooms_LST_waitingrooms.setLayoutManager(new LinearLayoutManager(RoomsActivity.this));
            rooms_LST_waitingrooms.setAdapter(adapter_game);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        findViews();
        initViews();
        MyImageUtils.getInstance().loadUri("", rooms_IMG_background); // TODO add path to jpg

        if(adapter_game != null)
            adapter_game.setCallBack_startGameProtocol(callBack_startGameProtocol);

        Intent prevIntent = getIntent();
        theUser = (UserDetails) prevIntent.getExtras().getSerializable(KEY_USER);

        roomsDBManager = new RoomsDBManager();
        roomsDBManager.setTheUser(theUser);
        roomsDBManager.setCallBack_setAdapterProtocol(callBack_setAdapterProtocol);
        roomsDBManager.setCallBack_hostGameProtocol(callBack_hostGameProtocol);

        roomsDBManager.readRoomsFromDB();
    }

    private void startNewRoom(){
        theUser.setChipColor(UserDetails.CHIP.BROWN);
        Room room = new Room().setRoomId(theUser.getId()).setHost(theUser);
        waitingList.add(room);
        roomsDBManager.saveRoomToDB(room);
        Log.d("after save room", "after save");
        //TODO  wait for other player with lottie
        roomsDBManager.readReady(room);

    }

    private void initViews() {
        rooms_BTN_newroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewRoom();
            }
        });
        rooms_BTN_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomsActivity.this, Menu_Activity.class);
                intent.putExtra(KEY_USER, theUser);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findViews() {
        rooms_IMG_background = findViewById(R.id.rooms_IMG_background);
        rooms_LST_waitingrooms = findViewById(R.id.rooms_LST_waitingrooms);
        rooms_BTN_newroom = findViewById(R.id.rooms_BTN_newroom);
        rooms_BTN_menu = findViewById(R.id.rooms_BTN_menu);
    }
}