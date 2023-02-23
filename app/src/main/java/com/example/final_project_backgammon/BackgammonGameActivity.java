package com.example.final_project_backgammon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Random;

public class BackgammonGameActivity extends AppCompatActivity {

    private int roundCounter =0;
    private boolean wasRepeated = false;
    private boolean areEqual = false;

    public final static String KEY_ROOM = "KEY_ROOM";

    CallBack_updateChipsProtocol callBack_updateChipsProtocol = new CallBack_updateChipsProtocol() {
        @Override
        public void updateChips(Room room) {
            if(backgammonGameManager.getCurrentUser().getChipColor() == UserDetails.CHIP.BROWN) {
                updateOff(backgammonGameManager
                        .getChipsRows().get(room.getGame().getTurnOff().getX()).getBrowns(), room);
                updateOn(backgammonGameManager
                        .getChipsRows().get(room.getGame().getTurnOn().getX()).getBrowns(), room);
                if(room.getGame().getEaten() != null) {
                    backgammonGameManager.handleEaten(room.getGame().getEaten(), ChipRow.COLOR.BROWN);
                }
            }
            else {
                updateOff(backgammonGameManager
                        .getChipsRows().get(room.getGame().getTurnOff().getX()).getWhites(), room);
                updateOn(backgammonGameManager
                        .getChipsRows().get(room.getGame().getTurnOn().getX()).getBrowns(), room);
                if(room.getGame().getEaten() != null) {
                    backgammonGameManager.handleEaten(room.getGame().getEaten(), ChipRow.COLOR.WHITE);
                }
            }
            adapter_chip.notifyDataSetChanged();
            gameDBManager.readTurnFromDB(backgammonGameManager.getRoom(), backgammonGameManager.getCurrentUser());
        }

    };

    private void updateOff(ArrayList<ChipRow> chips, Room room) {
        backgammonGameManager
                .getChipsRows()
                .get(room.getGame().getTurnOff().getX())
                .getBrowns()
                .get(room.getGame().getTurnOff().getY()).setIsVisible(false);

        if(room.getGame().getTurnOff().getX() < 15) {
            backgammonGameManager.updateUpperOff(room.getGame().getTurnOff());
        }
        else {
            backgammonGameManager.updateBottomOff(room.getGame().getTurnOff());
        }
    }

    private void updateOn(ArrayList<ChipRow> chips, Room room) {
        backgammonGameManager
                .getChipsRows()
                .get(room.getGame().getTurnOn().getX())
                .getBrowns()
                .get(room.getGame().getTurnOn().getY()).setIsVisible(true);

        if(room.getGame().getTurnOn().getX() < 15) {
            backgammonGameManager.updateUpperOn(room.getGame().getTurnOn());
        }
        else {
            backgammonGameManager.updateBottomOn(room.getGame().getTurnOn());
        }
    }
    CallBack_waitTurnProtocol callBack_waitTurnProtocol = new CallBack_waitTurnProtocol() {
        @Override
        public void waitTurn(UserDetails current) {
            backgammongame_BTN_rolldice.setVisibility(View.INVISIBLE);
            backgammongame_IMG_dice1.setVisibility(View.INVISIBLE);
            backgammongame_IMG_dice2.setVisibility(View.INVISIBLE);
           // MySignal.getInstance().toast("Wait for your turn");
            gameDBManager.readUpdatedBoard(backgammonGameManager.getRoom());
            //gameDBManager.readTurnFromDB(backgammonGameManager.getRoom(), current);
        }
    };
    CallBack_playTurnProtocol callBack_playTurnProtocol= new CallBack_playTurnProtocol() {
        @Override
        public void playTurn(UserDetails currentUser) {
            backgammongame_BTN_rolldice.setVisibility(View.VISIBLE);
            backgammongame_IMG_dice1.setVisibility(View.VISIBLE);
            backgammongame_IMG_dice2.setVisibility(View.VISIBLE);
           // MySignal.getInstance().toast("play your turn");
        }
    };
    CallBack_chipProtocol callBack_chipProtocol = new CallBack_chipProtocol() {
        @Override
        public void chip(ChipRow chipRow, boolean direction) {
            if(endOfGame == true) {
                MySignal.getInstance().toast("The game is over!");
                return;
            }
            if(backgammonGameManager.getCurrentDice() == 0){
                MySignal.getInstance().toast("First role dices!");
                return;
            }
            ChipRow tempChipRow = chipRow;
            if(backgammonGameManager.checkMovement(tempChipRow, direction)) { // check if chip movement is valid
                gameDBManager.writeBoardChangesToDB(backgammonGameManager.getRoom());
                adapter_chip.notifyDataSetChanged();
                roundCounter++;
                if(roundCounter == 1){
                    if(backgammonGameManager.getCurrentDice() == backgammonGameManager.getOtherDice() && !wasRepeated){
                        areEqual = true;
                        roundCounter--;
                        wasRepeated = true;
                    }else{
                        if(backgammongame_IMG_yellowback1.getVisibility() == View.VISIBLE) {
                            backgammongame_IMG_yellowback1.setVisibility(View.INVISIBLE);
                            backgammongame_IMG_yellowback2.setVisibility(View.VISIBLE);
                        }
                        else if(backgammongame_IMG_yellowback2.getVisibility() == View.VISIBLE) {
                            backgammongame_IMG_yellowback2.setVisibility(View.INVISIBLE);
                            backgammongame_IMG_yellowback1.setVisibility(View.VISIBLE);
                        }
                        backgammonGameManager.setCurrentDice(backgammonGameManager.getOtherDice());
                        wasRepeated = false;
                    }
                }
                if(roundCounter == 2){
                    if(areEqual && !wasRepeated){
                        roundCounter--;
                        wasRepeated = true;
                        areEqual = false;
                    }
                    else{
                        hasRolled = false;
                        backgammonGameManager.setCurrentDice(0);
                        backgammongame_IMG_yellowback1.setVisibility(View.INVISIBLE);
                        backgammongame_IMG_yellowback2.setVisibility(View.INVISIBLE);
                        wasRepeated = false;
                        if(backgammonGameManager.getCurrentUser().getChipColor() == UserDetails.CHIP.BROWN)
                            gameDBManager.updateTurn(backgammonGameManager.getRoom(), 1);
                        else
                            gameDBManager.updateTurn(backgammonGameManager.getRoom(), 0);
                    }
                }
            }
            else { // if not valid, the chip won't move, a toast will tell the user his step is not valid
                MySignal.getInstance().toast("This step is not valid");
            }
        }
    };

    // for recycler view
    private RecyclerView backgammongame_RCV_recyclerView;
    private Adapter_Chip adapter_chip;


    // for xml file
    private Button backgammongame_BTN_rolldice;
    private ImageView backgammongame_IMG_dice1;
    private ImageView backgammongame_IMG_dice2;
    private ImageView backgammongame_IMG_yellowback1;
    private ImageView backgammongame_IMG_yellowback2;

    // for determineStartingPlayer function
    private int currentPlayer;

    // for rollDice function
    private int rollAnimations = 6;
    private boolean hasRolled = false;
    private int delayTime = 50;
    private Random random = new Random();
    private int[] diceImages = {
            R.drawable.img_dice1,
            R.drawable.img_dice2,
            R.drawable.img_dice3,
            R.drawable.img_dice4,
            R.drawable.img_dice5,
            R.drawable.img_dice6
    };
    private int dicesSumForRollFunc = 0;

    // initialize manager in onCreate function
    private BackgammonGameManager backgammonGameManager;

    private CountDownTimer gameTimer;

    private boolean endOfGame = false;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    GameDBManager gameDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backgammon_game);
        backgammonGameManager = new BackgammonGameManager();
        Intent previuosIntent = getIntent();
        Room room = (Room)previuosIntent.getExtras().getSerializable(KEY_ROOM);
        backgammonGameManager.setRoom(room);
        gameDBManager = new GameDBManager();

        gameDBManager.setCallBack_updateChipsProtocol(callBack_updateChipsProtocol);
        gameDBManager.setCallBack_waitTurnProtocol(callBack_waitTurnProtocol);
        gameDBManager.setCallBack_playTurnProtocol(callBack_playTurnProtocol);

        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
        if(current.getUid().equals(backgammonGameManager.getRoom().getRoomId()))
            backgammonGameManager.setCurrentUser(backgammonGameManager.getRoom().getHost());
        else
            backgammonGameManager.setCurrentUser(backgammonGameManager.getRoom().getPlayer2());
        gameTimer = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the UI with the remaining time
                TextView textView = findViewById(R.id.backgammongame_LBL_timer);
                textView.setText("Time left: " + millisUntilFinished / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                // Perform actions when the timer is finished
                TextView textView = findViewById(R.id.backgammongame_LBL_timer);
                textView.setText("Game Over");
                endOfGame = true;
                hasRolled = true;
                updateUI();
            }
        };

        findViews();

        backgammongame_BTN_rolldice.setVisibility(View.INVISIBLE);
        backgammongame_IMG_dice1.setVisibility(View.INVISIBLE);
        backgammongame_IMG_dice2.setVisibility(View.INVISIBLE);

        backgammonGameManager.getRoom().getGame().setTurn(0);
        gameDBManager.initiateTurn(backgammonGameManager.getRoom());
        gameDBManager.readTurnFromDB(room, backgammonGameManager.getCurrentUser());
        backgammonGameManager.init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        backgammongame_RCV_recyclerView.setLayoutManager(layoutManager);
        adapter_chip = new Adapter_Chip(backgammonGameManager.getChipsRows());
        backgammongame_RCV_recyclerView.setAdapter(adapter_chip);

        if(currentUser.getUid().equals(backgammonGameManager.getRoom().getHost().getId()))
            adapter_chip.setTheUser(backgammonGameManager.getRoom().getHost());
        else
            adapter_chip.setTheUser(backgammonGameManager.getRoom().getPlayer2());

        adapter_chip.setCallBack_chipProtocol(callBack_chipProtocol);

        initButtonView();
        gameTimer.start();
        MySignal.init(this);
    }

    private void updateUI() {
        MySignal.getInstance().toast("The game is over!");
        if(backgammonGameManager.getEatenBrowns() > backgammonGameManager.getEatenWhites())
            MySignal.getInstance().toast("The winner is the brown's player!");
        else if(backgammonGameManager.getEatenBrowns() < backgammonGameManager.getEatenWhites())
            MySignal.getInstance().toast("The winner is the white's player!");
        else if(backgammonGameManager.getEatenBrowns() == backgammonGameManager.getEatenWhites())
            MySignal.getInstance().toast("It's a tie!");
    }
    private void findViews() {
        backgammongame_RCV_recyclerView = findViewById(R.id.backgammongame_RCV_recyclerView);

        backgammongame_IMG_dice1 = findViewById(R.id.backgammongame_IMG_dice1);
        backgammongame_IMG_dice2 = findViewById(R.id.backgammongame_IMG_dice2);

        backgammongame_BTN_rolldice = findViewById(R.id.backgammongame_BTN_rolldice);

        backgammongame_IMG_yellowback1  = findViewById(R.id.backgammongame_IMG_yellowback1);
        backgammongame_IMG_yellowback2 = findViewById(R.id.backgammongame_IMG_yellowback2);
    }

    private void initButtonView() {
        backgammongame_BTN_rolldice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();

                final Handler handler = new Handler();
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // Code to run after delay
                        MySignal.getInstance().toast("Click on the chip you want to move");
                    }
                };
                handler.postDelayed(myRunnable, 1000); // Delay in milliseconds
            }
        });
    }

    private int rollDice() {
        if (hasRolled) {
            return dicesSumForRollFunc;
        }
        hasRolled = true;

        final Handler handler = new Handler();
        final int[] dic1 = {0};
        final int[] dic2 = {0};
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < rollAnimations; i++) {
                    dic1[0] = random.nextInt(6) + 1;  //dic1 and dic2 are arrays with one element, which are set to the random values inside the loop. This way, then can be accessed from within the inner class and don't need to be declared as final.
                    dic2[0] = random.nextInt(6) + 1; //dic1 and dic2 are arrays with one element, which are set to the random values inside the loop. This way, then can be accessed from within the inner class and don't need to be declared as final.
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            backgammongame_IMG_dice1.setImageResource(diceImages[dic1[0] - 1]);
                            backgammongame_IMG_dice2.setImageResource(diceImages[dic2[0] - 1]);

                            if (dic1[0] > dic2[0] || dic1[0] == dic2[0]) {
                                backgammongame_IMG_yellowback1.setVisibility(View.VISIBLE);
                                backgammongame_IMG_yellowback2.setVisibility(View.INVISIBLE);
                                backgammonGameManager.setCurrentDice(dic1[0]);
                                backgammonGameManager.setOtherDice(dic2[0]);
                                roundCounter =0;
                            }
                            if (dic1[0] < dic2[0]) {
                                backgammongame_IMG_yellowback2.setVisibility(View.VISIBLE);
                                backgammongame_IMG_yellowback1.setVisibility(View.INVISIBLE);
                                backgammonGameManager.setCurrentDice(dic2[0]);
                                backgammonGameManager.setOtherDice(dic1[0]);
                                roundCounter =0;
                            }

                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                dicesSumForRollFunc = dic1[0] + dic2[0];
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return dicesSumForRollFunc;
    }
}