package com.example.omri.battleShip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LeaderBoard_Activity extends AppCompatActivity {

    private String difficulty ;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();


        difficulty=getIntent().getStringExtra("difficulty");
        RadioGroup radGrp = (RadioGroup) findViewById(R.id.radioGroup);
        for (int i=0;i<radGrp.getChildCount();i++){
            if (difficulty.equals(((RadioButton)radGrp.getChildAt(i)).getText().toString())) {
                ((RadioButton) radGrp.getChildAt(i)).setChecked(true);
            }
        }

        Toast.makeText(this, "difficulty="+difficulty, Toast.LENGTH_SHORT).show();

        FragmentTransaction ft = fragmentManager.beginTransaction();

        Fragment tableFrag= new TableFragment();
        Bundle args = new Bundle();
        args.putString("difficulty",difficulty);
        tableFrag.setArguments(args);
        ft.add(R.id.tableFrame,tableFrag,"frag1");



        Fragment mapFrag= new MapFragment();
        Bundle args2 = new Bundle();
        args2.putString("difficulty",difficulty);
        mapFrag.setArguments(args);
        ft.add(R.id.mapFrame, mapFrag,"frag2");

        ft.commit();



    }

}
