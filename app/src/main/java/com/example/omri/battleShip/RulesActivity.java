package com.example.omri.battleShip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public boolean onSupportNavigateUp(){

        Intent intent = new Intent(RulesActivity.this, MainActivity.class);
        startActivity(intent);

        return true;
    }

}
