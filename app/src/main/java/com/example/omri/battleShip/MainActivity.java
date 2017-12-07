package com.example.omri.battleShip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    SharedPreferences sharedPref;
    String userName;
    String gameDifficulty;
    String defaultDifficulty = "Easy";
    SharedPreferences.Editor editor;
    private EditText userNameEditText;
    Button nameBtn;
    TextView registeredName;
    private boolean isSound = true;
    ImageButton editPencil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //editor.clear().apply();
        //Gson gson = new Gson();
        sharedPref=getPreferences(Context.MODE_PRIVATE);
        userNameEditText= (EditText)findViewById(R.id.userNameEditText);
        nameBtn = (Button)findViewById(R.id.nameBtn);
        registeredName = (TextView)findViewById(R.id.registeredName);
        editPencil = (ImageButton)findViewById(R.id.edit_Pencil);
        //String json = sharedPref.getString("playerH", null);
        //if (json!=null) {
        //    HumanPlayer playerH = gson.fromJson(json, HumanPlayer.class);
        userName = sharedPref.getString("userName","unknown");
        if (!userName.equals("unknown")){
            userNameEditText.setVisibility(View.INVISIBLE);
            nameBtn.setVisibility(View.INVISIBLE);
            registeredName.setVisibility(View.VISIBLE);
            registeredName.setText("Hello, "+userName+"!");
            editPencil.setVisibility(View.VISIBLE);
        }

           // playerH.setAmountOfLogins(playerH.getAmountOfLogins()+1);
           // Toast.makeText(this, "you have logged in "+playerH.getAmountOfLogins()+" times!", Toast.LENGTH_SHORT).show();
        //}
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
        //userName = registeredName.getText().toString();
        Intent ArrangeBattleFieldActivity = new Intent(this,arrangeBattleFieldActivity.class);
        ArrangeBattleFieldActivity.putExtra("userName",userName);
        if(gameDifficulty != null) {
            ArrangeBattleFieldActivity.putExtra("gameDifficulty", gameDifficulty);
        }
        else
            ArrangeBattleFieldActivity.putExtra("gameDifficulty",defaultDifficulty);

        ArrangeBattleFieldActivity.putExtra("isSound",isSound);
        startActivity(ArrangeBattleFieldActivity);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void saveUserName(View view) {

        userName= userNameEditText.getText().toString();
        editor = sharedPref.edit();
        //Gson gson = new Gson();
        //String json = gson.toJson(userName);
        editor.putString("userName", userName);
        //sharedPref.edit().putString()
        //editor.putInt(getString(R.string.saved_high_score), newHighScore);
        editor.commit();
        Toast.makeText(this, "Username saved!", Toast.LENGTH_SHORT).show();
        userNameEditText.setVisibility(View.INVISIBLE);
        view.setVisibility(View.INVISIBLE);
        editPencil.setVisibility(View.VISIBLE);
        registeredName.setVisibility(View.VISIBLE);
        registeredName.setText("Hello, "+userName+"!");
    }

    public void editUserName(View view) {
        editPencil.setVisibility(View.INVISIBLE);
        registeredName.setVisibility(View.INVISIBLE);
        nameBtn.setVisibility(View.VISIBLE);
        userNameEditText.setVisibility(View.VISIBLE);

    }

    public void onSwitchClicked(View view) {
        Switch soundSwitch = (Switch) findViewById(R.id.soundSwitch);
        isSound = soundSwitch.isChecked();
    }
}

