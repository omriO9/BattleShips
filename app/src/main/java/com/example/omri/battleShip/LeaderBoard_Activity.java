package com.example.omri.battleShip;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;

public class LeaderBoard_Activity extends AppCompatActivity implements FragmentListener {

    private static final String TAG = LeaderBoard_Activity.class.getSimpleName();
    private String difficulty ;
    private FragmentManager fragmentManager;
    TableFragment tableFrag;
    FragmentTransaction fragmentTransaction;

    RadioGroup radGrp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board_);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();


        difficulty=getIntent().getStringExtra("difficulty");
        //Toast.makeText(this, "difficulty="+difficulty, Toast.LENGTH_SHORT).show();
        initRadioGroup();
        RadioGroup radGrp = (RadioGroup) findViewById(R.id.radioGroup);
        for (int i=0;i<radGrp.getChildCount();i++){
            if (difficulty.equals(((RadioButton)radGrp.getChildAt(i)).getText().toString())) {
                ((RadioButton) radGrp.getChildAt(i)).setChecked(true);
            }
        }

        Toast.makeText(this, "difficulty="+difficulty, Toast.LENGTH_SHORT).show();

        fragmentTransaction = fragmentManager.beginTransaction();

        tableFrag= new TableFragment();
        Bundle args = new Bundle();
        args.putString("difficulty",difficulty);
        tableFrag.setArguments(args);
        fragmentManager.beginTransaction()
                .add(R.id.tableFrame,tableFrag,"frag1")
                .commit();
        ((TableFragment)tableFrag).registerListener(LeaderBoard_Activity.this);

        Fragment mapFrag= new MapFragment();
        Bundle args2 = new Bundle();
        args2.putString("difficulty",difficulty);
        mapFrag.setArguments(args);
        fragmentTransaction.add(R.id.mapFrame, mapFrag,"frag2");
        fragmentTransaction.commit();
    }

    private void initRadioGroup() {
        radGrp = (RadioGroup) findViewById(R.id.radioGroup);
        Log.d(TAG, "initRadioGroup: ");


        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                Log.d(TAG, "onCheckedChanged: starting, difficulty="+difficulty);
                switch (id) {
                    case R.id.easyRadio:
                        difficulty = ((RadioButton)(findViewById(R.id.easyRadio))).getText().toString();
                        break;
                    case R.id.mediumRadio:
                        difficulty = ((RadioButton)(findViewById(R.id.mediumRadio))).getText().toString();
                        break;
                    case R.id.insaneRadio:
                        difficulty = ((RadioButton)(findViewById(R.id.insaneRadio))).getText().toString();
                        break;
                    default:
                        break;
                }
                Log.d(TAG, "onCheckedChanged: finishing, difficulty= "+difficulty);

                tableFrag.getArguments().putString("difficulty",difficulty);
                fragmentTransaction =fragmentManager.beginTransaction();
               // fragmentTransaction.remove(tableFrag).replace(R.id.tableFrame,frag).commit();
                fragmentTransaction.detach(tableFrag).attach(tableFrag).commit();

            }
        });

            }

    @Override
    public void sendUpdate(int id,boolean isFromTableFrag) {

        if (isFromTableFrag){
            // the id is from table , need to send update to map
            Log.d(TAG, "Activity receives message : "+id);
        }
        else {
            // the id is from map , need to send update to table

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
