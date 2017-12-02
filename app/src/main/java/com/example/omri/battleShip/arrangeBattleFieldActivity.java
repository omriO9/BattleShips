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
        String gameDifficulty  = (String) getIntent().getSerializableExtra("gameDifficulty");
        manager = new GameManager(gameDifficulty);
        int [] arr = manager.createPlayers("Mark","BlueGene"); //gridSize and numOfShips are decided according to gameDifficulty
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
                    paintLayout(possibleCords,"@drawable/cell_border_available", GridButton.State.POSSIBLE);
                }
                else if (gridButton.checkAvailability()== GridButton.State.POSSIBLE) {//if (manager.getHumanPlayer().getBattleField().getMyShipsLocation()[pos.getX()][pos.getY()]!=null){//gridButton is notAvailable - means we place a ship
                    //gridButton.setAvailability(GridButton.State.INUSE);
                    pos = new Coordinate(((GridButton) v).getPositionX(), ((GridButton) v).getPositionY());
                    //Toast.makeText(this, "bad positioning?", Toast.LENGTH_SHORT).show();
                    List<Coordinate> list2Paint =manager.getHumanPlayer().myField.placeShip(shipPos,pos,name);
                    paintLayout(list2Paint,"@drawable/hit", GridButton.State.INUSE);
                    //possibleCords.remove(pos); - this line doesn't remove somewhy!!!
                    removePossibleCords(possibleCords); // delete the remaining gray cells.
                    gridButton.setAvailability(GridButton.State.INUSE);
                    possibleCords =null; // clean the list for future placing.

                    // Restarting the selection - cannot choose that battleShip again
                    selectedBattleID=0;
                    oldImageBattleShip.setClickable(false);
                    oldImageBattleShip.setBackgroundResource(R.drawable.miss);
                    numberOfPlacedShips++;
                }
                else
                    Toast.makeText(this, "nothing happens", Toast.LENGTH_SHORT).show();
                gridButton.setBackgroundResource(R.drawable.hit);

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
            GridButton btn = (GridButton) gridLayout.getChildAt(c.getX()+c.getY()*gridSize);
                btn.setAvailability(GridButton.State.EMPTY);
                btn.setDefaultDrawable();
        }
    }
    private void paintLayout(List<Coordinate> list2Paint, String uri,GridButton.State state) {
        // uri = the image resource id
        // paint List of Coordinates according to URI given
        GridButton btn;
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        for (Coordinate c : list2Paint){
            int positionOnGrid = c.getY() * gridLayout.getColumnCount() + c.getX();
            btn = (GridButton) gridLayout.getChildAt(positionOnGrid);
            btn.setBackgroundResource(imageResource);
            btn.setAvailability(state);
            //btn.setOnClickListener(null);
        }
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



