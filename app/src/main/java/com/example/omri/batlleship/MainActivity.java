package com.example.omri.batlleship;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    HumanPlayer playerH;

    private EditText userNameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();
        sharedPref=getPreferences(Context.MODE_PRIVATE);
        userNameEditText= (EditText)findViewById(R.id.userNameEditText);
        String json = sharedPref.getString("playerH", null);
        if (json!=null) {
            HumanPlayer playerH = gson.fromJson(json, HumanPlayer.class);
            userNameEditText.setText("Welcome back, "+playerH.getPlayerName());
           // playerH.setAmountOfLogins(playerH.getAmountOfLogins()+1);
           // Toast.makeText(this, "you have logged in "+playerH.getAmountOfLogins()+" times!", Toast.LENGTH_SHORT).show();
        }
    }


    public void showRulesActivity(View view) {
        Intent openRulesActivity = new Intent(this,RulesActivity.class);
        startActivity(openRulesActivity);
    }

    public void ArrangeBattleFieldActivity(View view) {
        Intent ArrangeBattleFieldActivity = new Intent(this,arrangeBattleFieldActivity.class);
        startActivity(ArrangeBattleFieldActivity);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void saveUserName(View view) {

        String userName= userNameEditText.getText().toString();
        playerH = new HumanPlayer(userName);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(playerH);
        editor.putString("playerH", json);
        //sharedPref.edit().putString()
        //editor.putInt(getString(R.string.saved_high_score), newHighScore);
        editor.commit();
        Toast.makeText(this, ""+userName+" saved!", Toast.LENGTH_SHORT).show();
    }
}

