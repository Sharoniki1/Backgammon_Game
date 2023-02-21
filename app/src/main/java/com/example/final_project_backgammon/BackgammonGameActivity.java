package com.example.final_project_backgammon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class BackgammonGameActivity extends AppCompatActivity {

    private int roundCounter =0;
    private boolean wasRepeated = false;
    private boolean areEqual = false;

    CallBack_chipProtocol callBack_chipProtocol = new CallBack_chipProtocol() {
        @Override
        public void chip(ChipRow chipRow, boolean direction) {

            if(backgammonGameManager.getCurrentDice() == 0){
                MySignal.getInstance().toast("First role dices!");
                return;
            }
            ChipRow tempChipRow = chipRow;
            if(backgammonGameManager.checkMovement(tempChipRow, direction)) { // check if chip movement is valid
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backgammon_game);

        backgammonGameManager = new BackgammonGameManager();

        findViews();

        backgammonGameManager.init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        backgammongame_RCV_recyclerView.setLayoutManager(layoutManager);
        adapter_chip = new Adapter_Chip(backgammonGameManager.getChipsRows());
        backgammongame_RCV_recyclerView.setAdapter(adapter_chip);

        adapter_chip.setCallBack_chipProtocol(callBack_chipProtocol); // this one I added

        initButtonView();
        MySignal.init(this);
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
//        determineStartingPlayer();
    }

    private void determineStartingPlayer() {
        backgammongame_BTN_rolldice.setVisibility(View.VISIBLE);
        backgammongame_IMG_dice1.setVisibility(View.VISIBLE);
        backgammongame_IMG_dice2.setVisibility(View.VISIBLE);
        int player1Roll = rollDice();
        int player2Roll = rollDice();
        if (player1Roll > player2Roll) {
            currentPlayer = 1;
            Log.d("current is 1", "Player 1 starts");
        } else if (player2Roll > player1Roll) {
            currentPlayer = 2;
            Log.d("current is 2", "Player 2 starts");
        } else {
            determineStartingPlayer();
        }
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