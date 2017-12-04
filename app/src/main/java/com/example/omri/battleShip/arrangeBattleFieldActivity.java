package com.example.omri.battleShip;

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
    int gridLayoutHeight;
    Coordinate shipPos;
    private int numOfShips;
    private int gridSize;
    ImageButton ship5, ship4, ship3, ship3_2, ship2;
    ImageButton oldImageBattleShip;
    int selectedBattleID = 0; // the ID is from the resource file.
    int numberOfPlacedShips=0;
    GameManager manager;
    List<Coordinate> possibleCords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_battle_field);
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        String gameDifficulty  = getIntent().getStringExtra("gameDifficulty");
        String userName = getIntent().getStringExtra("userName");
        Log.d(TAG, "nullwongame?: name="+userName);
        manager = new GameManager(gameDifficulty);
        int [] arr = manager.createPlayers(userName,"BlueGene"); //gridSize and numOfShips are decided according to gameDifficulty
        gridSize = arr[0];
        numOfShips = arr[1];
        initGridLayout(gridSize);
        initFleet(numOfShips);

    }

    private void initFleet(int shipAmount) {

        ship3 = (ImageButton) findViewById(R.id.ship3);
        ship3_2 = (ImageButton) findViewById(R.id.ship3_2);
        ship2 = (ImageButton) findViewById(R.id.ship2);
        ship3.setOnClickListener(this);
        ship3_2.setOnClickListener(this);
        ship2.setOnClickListener(this);

        if(shipAmount >= GameManager.MEDIUM_SHIPS_AMOUNT) {
            ship4 = (ImageButton) findViewById(R.id.ship4);
            ship4.setVisibility(View.VISIBLE);
            ship4.setOnClickListener(this);
            if(shipAmount == GameManager.INSANE_SHIPS_AMOUNT){
                ship5= (ImageButton) findViewById(R.id.ship5);
                ship5.setVisibility(View.VISIBLE);
                ship5.setOnClickListener(this);
            }
        }
    }

    public void initGridLayout(int gridSize) {
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        Log.d(TAG, "initGridLayout: "+gridSize);
        gridLayout.setColumnCount(gridSize);
        gridLayout.setRowCount(gridSize);
        initGridLayoutButtons();
        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                gridLayoutHeight = gridLayout.getHeight(); //width is ready
                int cellSize = gridLayoutHeight / gridLayout.getColumnCount();
                //cellSize -= 3;
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
            Coordinate pos;
            if (selectedBattleID != 0) { // there's a ship selected!
                String name = getResources().getResourceEntryName(selectedBattleID);

                if (gridButton.checkAvailability()== GridButton.State.EMPTY) {
                    
                    pos = new Coordinate(((GridButton) v).getPositionX(), ((GridButton) v).getPositionY());
                    if (possibleCords!=null) {
                        Log.d(TAG, "onClick: possibleCords is not null!!!");
                        removePossibleCords(possibleCords); // delete old gray cells
                        // set old ship location to default cell view.
                        ((GridButton)gridLayout.getChildAt(shipPos.getX()+shipPos.getY()*gridSize)).setDefaultDrawable();
                    }
                    shipPos = pos;
                    possibleCords=manager.getHumanPlayer().myField.showPossiblePositions(pos, name); // shows placeAble positions for current ship.
                    paintLayout(possibleCords, GridButton.State.POSSIBLE);
                    gridButton.setBackgroundResource(R.drawable.hit);
                }
                else if (gridButton.checkAvailability()== GridButton.State.POSSIBLE) {//if (manager.getHumanPlayer().getBattleField().getMyShipsLocation()[pos.getX()][pos.getY()]!=null){//gridButton is notAvailable - means we place a ship
                    //gridButton.setAvailability(GridButton.State.INUSE);
                    pos = new Coordinate(((GridButton) v).getPositionX(), ((GridButton) v).getPositionY());
                    //Toast.makeText(this, "bad positioning?", Toast.LENGTH_SHORT).show();
                    List<Coordinate> list2Paint =manager.getHumanPlayer().myField.placeShip(shipPos,pos,name);
                    removePossibleCords(possibleCords); // delete the remaining gray cells.
                    gridButton.setAvailability(GridButton.State.INUSE);
                    paintLayout(list2Paint, GridButton.State.INUSE);
                    //possibleCords.remove(pos); - this line doesn't remove somewhy!!!
                    possibleCords =null; // clean the list for future placing.

                    // Restarting the selection - cannot choose that battleShip again
                    selectedBattleID=0;
                    oldImageBattleShip.setClickable(false);
                    oldImageBattleShip.setBackgroundResource(R.drawable.miss);
                    numberOfPlacedShips++;
                }
                else
                    Toast.makeText(this, "nothing happens", Toast.LENGTH_SHORT).show();


            }
            else{ // didn't choose a ship / already placed one.
                Toast.makeText(this, "Please select a ship first", Toast.LENGTH_SHORT).show();
            }
        }
        if (v instanceof ImageButton) { // means i clicked on a BattleShip image
            final ImageButton selectedImageButton = (ImageButton) v;
            if (selectedBattleID == 0) {
                selectedImageButton.setAlpha(0.5f);
            } else if (selectedBattleID != selectedImageButton.getId()) {
                oldImageBattleShip.setAlpha(1.0f);
                selectedImageButton.setAlpha(0.5f);
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
            GridButton btn = (GridButton) gridLayout.getChildAt(c.getX()+c.getY()*gridSize);
                btn.setAvailability(GridButton.State.EMPTY);
                btn.setDefaultDrawable();
        }
    }

    private void paintLayout(List<Coordinate> list2Paint,GridButton.State state) {
        // the func receives a flag - true - it paints ships / false - it paints gray available cells
        // it paints on the list of Coordinates it receives .
        GridButton btn;
//        boolean isVertical=false;
//        int front=-1,center=-1,rear=-1;
//        int frontEx=-1,centerEx=-1,rearEx=-1;
//        if (state==GridButton.State.INUSE) {
//
//            if (list2Paint.get(0).getX() == list2Paint.get(1).getX())
//                isVertical = true;
//            if (isVertical) {
//                front = R.drawable.front_vertical;
//                center = R.drawable.center_vertical;
//                rear = R.drawable.rear_vertical;
//                frontEx = R.drawable.front_vertical_ex;
//                centerEx = R.drawable.center_vertical_ex;
//                rearEx = R.drawable.rear_vertical_ex;
//                Log.d(TAG, "paintLayout: frontEx="+frontEx);
//
//            } else {
//                front = R.drawable.front;
//                center = R.drawable.center;
//                rear = R.drawable.rear;
//                frontEx = R.drawable.front_ex;
//                centerEx = R.drawable.center_ex;
//                rearEx = R.drawable.rear_ex;
//            }
//        }
       // int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        for (int i=0;i<list2Paint.size();i++){
            Log.d(TAG, "paintLayout: list2Paint.size="+list2Paint.size());
            int positionOnGrid = list2Paint.get(i).getY() * gridLayout.getColumnCount() + list2Paint.get(i).getX();
            btn = (GridButton) gridLayout.getChildAt(positionOnGrid);

            if (state== GridButton.State.POSSIBLE) { // we want gray cells
                btn.setBackgroundResource(R.drawable.cell_border_available);
            }
            else { // we are placing a ship!
                btn.setBackgroundResource(manager.getHumanPlayer().getBattleField().getMyShipsLocation()[list2Paint.get(i).getX()][list2Paint.get(i).getY()].getImg());

//                if (i==0) {
//                   btn.setBackgroundResource(front);
//                   manager.getHumanPlayer().getBattleField().getMyShipsLocation()[list2Paint.get(i).getX()][list2Paint.get(i).getY()].setImgResourceID(front,frontEx);
//                   // gridLayout.getChildAt(0).setBackgroundResource(manager.getHumanPlayer().getBattleField().getMyShipsLocation()[list2Paint.get(i).getX()][list2Paint.get(i).getY()].getImgExplosioResourceID());
//                }
//                else if (i==list2Paint.size()-1) {
//                    btn.setBackgroundResource(rear);
//                   manager.getHumanPlayer().getBattleField().getMyShipsLocation()[list2Paint.get(i).getX()][list2Paint.get(i).getY()].setImgResourceID(rear,rearEx);
//                   // gridLayout.getChildAt(1).setBackgroundResource(manager.getHumanPlayer().getBattleField().getMyShipsLocation()[list2Paint.get(i).getX()][list2Paint.get(i).getY()].getImgExplosioResourceID());
//                    Log.d(TAG, "paintLayout: list2Paint inside rear?");
//                }
//                else {
//                    btn.setBackgroundResource(center);
//                   manager.getHumanPlayer().getBattleField().getMyShipsLocation()[list2Paint.get(i).getX()][list2Paint.get(i).getY()].setImgResourceID(center,centerEx);
//                   // gridLayout.getChildAt(3).setBackgroundResource(manager.getHumanPlayer().getBattleField().getMyShipsLocation()[list2Paint.get(i).getX()][list2Paint.get(i).getY()].getImgExplosioResourceID());
//                }
            }
            btn.setAvailability(state);
        }
//        for (Coordinate c : list2Paint){
//            int positionOnGrid = c.getY() * gridLayout.getColumnCount() + c.getX();
//            btn = (GridButton) gridLayout.getChildAt(positionOnGrid);
//            btn.setBackgroundResource(imageResource);
//            btn.setAvailability(state);
//            //btn.setOnClickListener(null);
//        }
    }
    public void startGameActivity(View view) {

        if (numberOfPlacedShips!=manager.getHumanPlayer().numOfShips){
            Toast.makeText(this, "Please place all ships before proceeding.", Toast.LENGTH_SHORT).show();
        }
        else {
        Intent GameActivity = new Intent(this, GameActivity.class);
        GameActivity.putExtra("GameManager", manager);
        startActivity(GameActivity);
        }
    }


    @Override
    public boolean onSupportNavigateUp(){
        //finish();
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
        return true;
    }
}



