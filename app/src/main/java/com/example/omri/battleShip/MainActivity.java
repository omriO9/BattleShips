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
    //private String userName;
    private String gameDifficulty;
    private SharedPreferences.Editor editor;
    //private EditText userNameEditText;
    //private Button nameBtn;
    //private TextView registeredName;
    private boolean isSound = true;
    private Switch soundSwitch;
    //private ImageButton editPencil;

    //private shipsOpenHelper dbHelper;
    //private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB - tryouts
        //dbHelper= new shipsOpenHelper(this);
        //this.deleteDatabase(dbHelper.getDatabaseName()); // // delete a db
        // finish DB tryouts

        sharedPref=getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        //userNameEditText= (EditText)findViewById(R.id.userNameEditText);
        //nameBtn = (Button)findViewById(R.id.nameBtn);
        //registeredName = (TextView)findViewById(R.id.registeredName);
        //editPencil = (ImageButton)findViewById(R.id.edit_Pencil);
        //userName = sharedPref.getString("userName","unknown");
        isSound = sharedPref.getBoolean("isSound",true);
        soundSwitch = (Switch) findViewById(R.id.soundSwitch);
        soundSwitch.setChecked(isSound);
//        if (!userName.equals("unknown")){
//            userNameEditText.setVisibility(View.INVISIBLE);
//            nameBtn.setVisibility(View.INVISIBLE);
//            registeredName.setVisibility(View.VISIBLE);
//            String registeredNameTxt = "Hello, "+userName+"!";
//            registeredName.setText(registeredNameTxt);
//            editPencil.setVisibility(View.VISIBLE);
//        }
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
        //ArrangeBattleFieldActivity.putExtra("userName",userName);
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

//    public void saveUserName(View view) {
//
//        userName= userNameEditText.getText().toString();
//        editor.putString("userName", userName);
//        editor.commit();
//        Toast.makeText(this, "Username saved!", Toast.LENGTH_SHORT).show();
//        userNameEditText.setVisibility(View.INVISIBLE);
//        view.setVisibility(View.INVISIBLE);
//        editPencil.setVisibility(View.VISIBLE);
//        registeredName.setVisibility(View.VISIBLE);
//        String registeredNameText = "Hello, "+userName+"!";
//        registeredName.setText(registeredNameText);
//    }

//    public void editUserName(View view) {
//        editPencil.setVisibility(View.INVISIBLE);
//        registeredName.setVisibility(View.INVISIBLE);
//        nameBtn.setVisibility(View.VISIBLE);
//        userNameEditText.setVisibility(View.VISIBLE);
//
//    }

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

