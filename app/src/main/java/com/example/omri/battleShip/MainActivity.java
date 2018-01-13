package com.example.omri.battleShip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public final static String defaultDifficulty = "Easy";

    private SharedPreferences sharedPref;

    private String gameDifficulty;
    private SharedPreferences.Editor editor;

    private boolean isSound = true;
    private Switch soundSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB - tryouts
        //shipsOpenHelper dbHelper = new shipsOpenHelper(this);
        //this.deleteDatabase(dbHelper.getDatabaseName());
//        SQLiteDatabase db =dbHelper.getReadableDatabase();
//        db.execSQL("delete from "+ shipsOpenHelper.SCORES_TABLE);
        //        db.beginTransaction();
        //        String selectQuery = "DELETE * FROM "+shipsOpenHelper.SCORES_TABLE;
        //        Cursor cursor = db.rawQuery(selectQuery,null);
        //        db.setTransactionSuccessful();
        //        db.endTransaction();
        //        // End the transaction.
        //        db.close();

        //this.deleteDatabase(dbHelper.getDatabaseName()); // // delete a db
        // finish DB tryouts

        sharedPref=getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        isSound = sharedPref.getBoolean("isSound",true);
        soundSwitch = (Switch) findViewById(R.id.soundSwitch);
        soundSwitch.setChecked(isSound);

        initRadioGroup();
    }
    public void initRadioGroup(){
        RadioGroup radGrp = (RadioGroup) findViewById(R.id.radioGroup);
        gameDifficulty = sharedPref.getString("gameDifficulty","Easy");
        for (int i=0;i<radGrp.getChildCount();i++){
            if (gameDifficulty.equals(((RadioButton)radGrp.getChildAt(i)).getText().toString())) {
                Log.d(TAG, "initRadioGroup: inside if radio setting");
                ((RadioButton) radGrp.getChildAt(i)).setChecked(true);
            }
        }

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

        if(gameDifficulty != null) {
            ArrangeBattleFieldActivity.putExtra("gameDifficulty", gameDifficulty);
            editor.putString("gameDifficulty",gameDifficulty);
        }
        else {
            ArrangeBattleFieldActivity.putExtra("gameDifficulty", defaultDifficulty);
            editor.putString("gameDifficulty",defaultDifficulty);
        }
        editor.commit();
        ArrangeBattleFieldActivity.putExtra("isSound",isSound);
        startActivity(ArrangeBattleFieldActivity);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }



    public void onSwitchClicked(View view) {
        soundSwitch = (Switch) findViewById(R.id.soundSwitch);
        isSound = soundSwitch.isChecked();
        editor.putBoolean("isSound", isSound);
        editor.commit();
        Log.d(TAG, "onSwitchClicked: isSound="+isSound);

    }

    public void start_Leaderboard_Activity(View view) {
        Intent openLeaderBoards = new Intent(this,LeaderBoard_Activity.class);
        Log.d(TAG, "start_Leaderboard_Activity: gameDifficulty="+gameDifficulty);
        openLeaderBoards.putExtra("difficulty",gameDifficulty);
        startActivity(openLeaderBoards);
    }

}

