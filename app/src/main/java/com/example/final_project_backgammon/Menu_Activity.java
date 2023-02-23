package com.example.final_project_backgammon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Menu_Activity extends AppCompatActivity {

    public final static String KEY_USER = "KEY_USER";
    private AppCompatImageView menu_IMG_background;
    private Button menu_BTN_play;
    private MaterialTextView menu_LBL_numcoins;
    UserDetails theUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        findViews();
        initViews();
        MyImageUtils.getInstance().loadUri("", menu_IMG_background); // TODO add path to jpg
        Intent prevIntent = getIntent();
        if(prevIntent.getExtras() != null) {
            theUser = (UserDetails) prevIntent.getExtras().getSerializable(KEY_USER);
            menu_LBL_numcoins.setText(theUser.getCoins()+"");
        }

    }

    private void initViews() {
        menu_BTN_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWaitingList(theUser);
            }
        });
    }

    private void openWaitingList(UserDetails theUser) {
        Intent intent = new Intent(this, RoomsActivity.class);
        intent.putExtra(KEY_USER, theUser);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        menu_IMG_background = findViewById(R.id.menu_IMG_background);
        menu_BTN_play = findViewById(R.id.menu_BTN_play);
        menu_LBL_numcoins = findViewById(R.id.menu_LBL_numcoins);
    }
}