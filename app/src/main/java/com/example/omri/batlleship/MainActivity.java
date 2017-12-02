package com.example.omri.batlleship;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    SharedPreferences sharedPref;
    HumanPlayer playerH;
    String gameDifficulty;
    String defaultDifficulty = "Easy";

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
        RadioGroup radGrp = (RadioGroup) findViewById(R.id.radioGroup);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.easyRadio:
                        gameDifficulty = ((RadioButton)(findViewById(R.id.easyRadio))).getText().toString();
                        break;
                    case R.id.mediumRadio:
                        gameDifficulty = ((RadioButton)(findViewById(R.id.mediumRadio))).getText().toString();
                        break;
                    case R.id.insaneRadio:
                        gameDifficulty = ((RadioButton)(findViewById(R.id.insaneRadio))).getText().toString();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void startRulesActivity(View view) {
        Intent openRulesActivity = new Intent(this,RulesActivity.class);
        startActivity(openRulesActivity);
    }

    public void startArrangeBattleFieldActivity(View view) {
        Intent ArrangeBattleFieldActivity = new Intent(this,arrangeBattleFieldActivity.class);
        if(gameDifficulty != null)
            ArrangeBattleFieldActivity.putExtra("gameDifficulty",gameDifficulty);
        else
            ArrangeBattleFieldActivity.putExtra("gameDifficulty",defaultDifficulty);
        startActivity(ArrangeBattleFieldActivity);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void saveUserName(View view) {

        String userName= userNameEditText.getText().toString();
        playerH = new HumanPlayer(userName,10,5);
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

