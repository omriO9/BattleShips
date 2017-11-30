package com.example.omri.batlleship;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class arrangeBattleFieldActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = arrangeBattleFieldActivity.class.getSimpleName();
    GridLayout gridLayout;
    int gridLayoutWidth;
    Coordinate shipPos;
    List<ImageButton> shipList;
    ImageButton ship5, ship4, ship3, ship3_2, ship2;
    ImageButton oldImageBattleShip;
    HumanPlayer human;
    //int selectedShip = 0;  // 0-nothing chosen,1-ship5,2-ship4,3-ship3,4-ship3_2,5-ship2
    int selectedBattleID = 0; // the ID is from the resource file.

    List<Coordinate> possibleCords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_battle_field);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // we need to receive from mainActivity the size of map (level 1/2/3);
        human = new HumanPlayer("Mark",10);
        initGridLayout();
        initFleet();
    }

    private void initFleet() {

        ship5 = (ImageButton) findViewById(R.id.ship5);
        ship4 = (ImageButton) findViewById(R.id.ship4);
        ship3 = (ImageButton) findViewById(R.id.ship3);
        ship3_2 = (ImageButton) findViewById(R.id.ship3_2);
        ship2 = (ImageButton) findViewById(R.id.ship2);
        ship5.setOnClickListener(this);
        ship4.setOnClickListener(this);
        ship3.setOnClickListener(this);
        ship3_2.setOnClickListener(this);
        ship2.setOnClickListener(this);


    }

    public void initGridLayout() {
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        initGridLayoutButtons();

        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                gridLayoutWidth = gridLayout.getWidth(); //width is ready
                int cellSize = gridLayoutWidth / gridLayout.getColumnCount();
                cellSize -= 3;
                for (int i = 0; i < gridLayout.getChildCount(); i++) {
                    GridButton btn = (GridButton) gridLayout.getChildAt(i);
                    btn.setPositionX(i % gridLayout.getColumnCount());
                    btn.setPositionY(i / gridLayout.getColumnCount());
                    btn.setBackgroundResource(R.drawable.cell_border);
                    btn.getLayoutParams().height = cellSize;
                    btn.getLayoutParams().width = cellSize;
                }
                gridLayout.invalidate();
                gridLayout.requestLayout();
            }
        });

    }

    private void initGridLayoutButtons() {

        int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton = new GridButton(this);
            gridButton.setOnClickListener(this);
            gridLayout.addView(gridButton);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;
            Coordinate pos = new Coordinate(((GridButton) v).getPositionX(), ((GridButton) v).getPositionY());
            Toast.makeText(this, ""+pos.toString(), Toast.LENGTH_SHORT).show();
            if (selectedBattleID != 0) { // there's a ship selected!
                String name = getResources().getResourceEntryName(selectedBattleID);
                Toast.makeText(this, "shipName="+name, Toast.LENGTH_SHORT).show();

                if (gridButton.isAvailable()) {

                    if (possibleCords!=null) {
                        for (int i=0;i<possibleCords.size();i++){
                    }
                        removePossibleCords(possibleCords); // deletes old gray cells
                        ((GridButton)gridLayout.getChildAt(shipPos.getX()+shipPos.getY()*10)).setDefaultDrawable();
                    }
                    shipPos = pos;
                    possibleCords=human.myField.showPossiblePositions(pos, name); // shows placeAble positions for current ship.
                    paintLayout(possibleCords,"@drawable/cell_border_available");
                }
                else {//gridButton is notAvailable - means we place a ship
                    List<Coordinate> list2Paint =human.myField.placeShip(shipPos,pos,name);
                    human.myField.printMat();

                    paintLayout(list2Paint,"@drawable/hit");
                    boolean test=possibleCords.remove(pos);
                    Log.d(TAG, "onClick3: test="+test);
                    removePossibleCords(possibleCords);
                    possibleCords =null;

                    // Restarting the selection - cannot choose that battleShip again
                    selectedBattleID=0;
                    oldImageBattleShip.setClickable(false);
                    oldImageBattleShip.setBackgroundResource(R.drawable.miss);
                }
                gridButton.setBackgroundResource(R.drawable.hit);
               // human.placeBattleShips(pos); // only OK after logic check!!!
//                v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
//                    @Override
//                    public void run() {
//                        gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
//                        //Drawable border = ContextCompat.getDrawable(this, R.drawable.hit);
//
//                    }
//                }).start();
            }
            else{ // didn't choose a ship / already placed one.
                Toast.makeText(this, "Please select a ship first", Toast.LENGTH_SHORT).show();
            }
        }
        if (v instanceof ImageButton) { // means i clicked on a BattleShip image
            final ImageButton selectedImageButton = (ImageButton) v;
            if (selectedBattleID == 0) {
                selectedImageButton.setBackgroundResource(R.drawable.selected_battleship);
            } else if (selectedBattleID != selectedImageButton.getId()) {
                oldImageBattleShip.setBackgroundResource(R.drawable.battleship);
                selectedImageButton.setBackgroundResource(R.drawable.selected_battleship);
            }
            v.animate().rotationXBy(360).setDuration(1500).start();
            selectedBattleID = selectedImageButton.getId();
            oldImageBattleShip = selectedImageButton;
        }
    }

    private void removePossibleCords(List<Coordinate> possibleCords) {
        // func that receives a list of "old" possible cords - not relevant anymore since
        // ship was placed - and removes them = sets button Drawable to correct one.
        for(Coordinate c : possibleCords){
            GridButton btn = (GridButton) gridLayout.getChildAt(c.getX()+c.getY()*10);
                btn.toggleAvailable();
                btn.setDefaultDrawable();
        }
    }

    private void paintLayout(List<Coordinate> list2Paint, String uri) {
        // uri = the image resource id
        // paint List of Coordinates according to URI given
        GridButton btn;
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        for (Coordinate c : list2Paint){
            int positionOnGrid = c.getY() * 10 + c.getX();
            btn = (GridButton) gridLayout.getChildAt(positionOnGrid);
            btn.setBackgroundResource(imageResource);
            btn.toggleAvailable();
        }
    }

    public void showGameActivity(View view) {
        Intent GameActivity = new Intent(this, GameActivity.class);
        startActivity(GameActivity);
    }

    public void onBackBtnPressed(View view) {

        new AlertDialog.Builder(this)
                .setMessage("Your positioning will be reseted,\n are you sure you want exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(arrangeBattleFieldActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}



