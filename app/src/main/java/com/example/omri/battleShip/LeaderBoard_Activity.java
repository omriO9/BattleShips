package com.example.omri.battleShip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class LeaderBoard_Activity extends AppCompatActivity {

    private String difficulty ;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board_);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();
        difficulty=getIntent().getStringExtra("difficulty");
        Toast.makeText(this, "difficulty="+difficulty, Toast.LENGTH_SHORT).show();

//        RelativeLayout layout = (RelativeLayout)findViewById(R.id.relativeLayout_Leader);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
//                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup);

        FragmentTransaction ft = fragmentManager.beginTransaction();

        Fragment tableFrag= new TableFragment();
        Bundle args = new Bundle();
        args.putString("difficulty",difficulty);
        tableFrag.setArguments(args);
        ft.add(R.id.tableFrame,tableFrag,"frag1");



//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.linerLayout_Leader, tableFrag,"frag1")
//                .commit();

        Fragment tableFrag2= new TableFragment();
        Bundle args2 = new Bundle();
        args2.putString("difficulty",difficulty);
        tableFrag2.setArguments(args);
        ft.add(R.id.mapFrame, tableFrag2,"frag2");

        ft.commit();
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.linerLayout_Leader, tableFrag2,"frag2")
//                .commit();
//


    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
