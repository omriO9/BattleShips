package com.example.omri.batlleship;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void showRulesActivity(View view) {
        Intent openRulesActivity = new Intent(this,RulesActivity.class);
        startActivity(openRulesActivity);
    }

    public void ArrangeBattleFieldActivity(View view) {
        Intent ArrangeBattleFieldActivity = new Intent(this,ArrangeBattleFieldActivity.class);
        startActivity(ArrangeBattleFieldActivity);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}

