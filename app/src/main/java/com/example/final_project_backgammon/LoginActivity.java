package com.example.final_project_backgammon;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private AppCompatImageView login_IMG_background;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    UserDetails theUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        login_IMG_background = findViewById(R.id.login_IMG_background);

        MyImageUtils.getInstance().loadUri("", login_IMG_background); // TODO add path to jpg

        mAuth = FirebaseAuth.getInstance();

        firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser == null){
            login();
            //saveUserToDB(firebaseUser);
        }

        else { // user logged in
            readUserFromDB();
        }
    }

    private void login(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if(result.getResultCode() == RESULT_OK){
            firebaseUser= mAuth.getCurrentUser();
        }
        theUser = new UserDetails().
                setId(firebaseUser.getUid()).
                setName(firebaseUser.getDisplayName());
        saveUserToDB(theUser);
        openMenu(theUser);
    }

    private void saveUserToDB(UserDetails theUser){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref= db.getReference("accounts");
        ref.child("users").child(theUser.getId()).setValue(theUser);
    }

    private void readUserFromDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("accounts");
        ref.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    theUser = new UserDetails().
                            setId(firebaseUser.getUid()).
                            setName(firebaseUser.getDisplayName());
                    saveUserToDB(theUser);
                }
                else{
                    theUser = (UserDetails) snapshot.getValue(UserDetails.class);
                }
                openMenu(theUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });
    }

    private void openMenu(UserDetails theUser) {
        Intent intent = new Intent(this, Menu_Activity.class);
        intent.putExtra(Menu_Activity.KEY_USER, theUser);
        startActivity(intent);
        finish();
    }
}