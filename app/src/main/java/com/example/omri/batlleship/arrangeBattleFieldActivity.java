package com.example.omri.batlleship;

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
    List<ImageButton> shipList;
    ImageButton ship5, ship4, ship3, ship3_2, ship2;
    //private enum btlShip{ship5,ship4,ship3,ship3_2,ship2};
    ImageButton oldImageBattleShip;
    HumanPlayer human;
    int selectedShip = 0;  // 0-nothing chosen,1-ship5,2-ship4,3-ship3,4-ship3_2,5-ship2
    int selectedBattleID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_battle_field);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Display display = getWindowManager().getDefaultDisplay();
        //Point size = new Point();
        //display.getSize(size);
        //int screenWidth = size.x;
        //int screenHeight = size.y;

        human = new HumanPlayer("Mark");

        initGridLayout();

        initFleet();
    }

    private void initFleet() {

//        shipList.add((ImageButton)findViewById(R.id.ship5));
//        shipList.add((ImageButton)findViewById(R.id.ship4));
//        shipList.add((ImageButton)findViewById(R.id.ship3));
//        shipList.add((ImageButton)findViewById(R.id.ship3_2));
//        shipList.add((ImageButton)findViewById(R.id.ship2));

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
//        shipList.add(ship5);
//        shipList.add(ship4);
//        shipList.add(ship3);
//        shipList.add(ship3_2);
//        shipList.add(ship2);
//        for (ImageButton ib : shipList)
//            ib.setOnClickListener(this);

    }

    public void initGridLayout() {
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
//        int cellSize = gridLayoutWidth / gridLayout.getColumnCount(); // 910
//        Log.d(TAG, "onResume: gridLayoutWidth="+gridLayoutWidth);
//        cellSize -= 3;
        int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton = new GridButton(this);
            gridButton.setOnClickListener(this);
            gridLayout.addView(gridButton);
        }
        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                gridLayoutWidth = gridLayout.getWidth(); //width is ready
                int cellSize = gridLayoutWidth / gridLayout.getColumnCount(); // 910
                //Log.d(TAG, "onResume: gridLayoutWidth="+gridLayoutWidth);
                cellSize -= 3;
                // int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
                for (int i = 0; i < gridLayout.getChildCount(); i++) {
                    GridButton btn = (GridButton) gridLayout.getChildAt(i);
                    btn.setPositionX(i % gridLayout.getColumnCount());
                    btn.setPositionY(i / gridLayout.getColumnCount());
                    //Drawable border = ContextCompat.getDrawable(this, R.drawable.cell_border);
                    //btn.setOnClickListener(this);
                    //Log.d(TAG, "onGlobalLayout: height+size="+cellSize);
                    btn.setBackgroundResource(R.drawable.cell_border);
                    //btn.setHeight(cellSize);
                    // btn.setWidth(cellSize);
                    //btn.setLayoutParams(btn.getLayoutParams());
                    btn.getLayoutParams().height = cellSize;
                    btn.getLayoutParams().width = cellSize;
                    // btn.setLayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
                }
                gridLayout.invalidate();
                gridLayout.requestLayout();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;
            Coordinate pos = new Coordinate(((GridButton) v).getPositionX(), ((GridButton) v).getPositionY());
            Toast.makeText(this, ""+pos.toString(), Toast.LENGTH_SHORT).show();
            if (selectedBattleID != 0) { // there's a ship selected!
                // add code to placeBattleShip() later
                String name = getResources().getResourceEntryName(selectedBattleID);
                //Toast.makeText(this, "shipName="+name, Toast.LENGTH_SHORT).show();
                if (gridButton.isAvailable())
                Log.d(TAG, "onClick: gridButton is Available");
                else
                    Log.d(TAG, "onClick: gridButton is *NOT* Available");
                if (gridButton.isAvailable()) {
                    Log.d(TAG, "onClick: inside the if");
                    showPossiblePosition(pos, name); // shows placeAble positions for current ship.
                }
                else {
                    Log.d(TAG, "onClick: i'm doing something else");
                    if (pos.getX()==gridButton.getPositionX()) { // we have same X - same row
                        for (int i=0;i<(gridButton.getPositionX()-pos.getY());i++){
                            gridLayout.getChildAt(pos.getY()*10+pos.getX()-i).setBackgroundResource(R.drawable.hit);
                        }
                    }
                }
               // human.placeBattleShips(pos); // only OK after logic check!!!
                v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
                        //Drawable border = ContextCompat.getDrawable(this, R.drawable.hit);
                        gridButton.setBackgroundResource(R.drawable.hit);
                    }
                }).start();
            }
        }
        if (v instanceof ImageButton) { // means i clicked on a BattleShip image
            final ImageButton selectedImageButton = (ImageButton) v;
            if (selectedBattleID == 0) {
//                selectedBattleID = selectedImageButton.getId();

                selectedImageButton.setBackgroundResource(R.drawable.selected_battleship);


            } else if (selectedBattleID != selectedImageButton.getId()) {
                oldImageBattleShip.setBackgroundResource(R.drawable.battleship);
                selectedImageButton.setBackgroundResource(R.drawable.selected_battleship);
                // oldImageBattleShip = selectedImageButton;
            }
            v.animate().rotationXBy(360).setDuration(1500).start();
            selectedBattleID = selectedImageButton.getId();
            oldImageBattleShip = selectedImageButton;

//            switch(selectedImageButton.resour){
//                case()
//            }
        }
    }

    private void showPossiblePosition(Coordinate pos, String name) {
        // A method that gets a clicked location + ship's name and sets on the gridLayout
        // the possible movements to place the ship.
        //Toast.makeText(this, "name="+name, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showPossiblePosition: pos={" + pos.getX() + "," + pos.getY() + "}");
        // Log.d(TAG, "showPossiblePosition: buttonIndex="+gridLayout.getChildAt(pos.getX()*10+pos.getY()).toString());
        int positionOnGrid = pos.getY() * 10 + pos.getX();
        GridButton btn;
        for (BattleShip b : human.getBattleShips()) {
            //  Toast.makeText(this, "b[name]="+b.getName(), Toast.LENGTH_SHORT).show();
            //running over player's BS and checking which ship i'm trying to position

            if (b.getName().equals(name)) { // b is my battleship.
                // to the right
                if (pos.getX() + b.getLength() <= gridLayout.getColumnCount()) {
                    btn = (GridButton) gridLayout.getChildAt(positionOnGrid + b.getLength() - 1);
                    btn.setBackgroundResource(R.drawable.cell_border_available);
                    btn.toggleAvailable();
                }
                // to the left
                if (pos.getX() - b.getLength() + 1 >= 0) {
                    btn = (GridButton) gridLayout.getChildAt(positionOnGrid - b.getLength() + 1);
                    btn.setBackgroundResource(R.drawable.cell_border_available);
                    btn.toggleAvailable();
                }
                // to the down
                if (pos.getY() + b.getLength() <= gridLayout.getRowCount()) {
                    btn = (GridButton) gridLayout.getChildAt(positionOnGrid + 10 * (b.getLength() - 1));
                    btn.setBackgroundResource(R.drawable.cell_border_available);
                    btn.toggleAvailable();
                }
                // to the up
                if (pos.getY() - b.getLength()+1 >= 0) {
                    btn = (GridButton) gridLayout.getChildAt(positionOnGrid - 10 * (b.getLength() + 1));
                    btn.setBackgroundResource(R.drawable.cell_border_available);
                    btn.toggleAvailable();

                }
            }
        }
    }



//Coordinate pos = new Coordinate(((GridButton) v).getPositionX(), ((GridButton) v).getPositionY());

    public void GameActivity(View view) {
        Intent GameActivity = new Intent(this, GameActivity.class);
        startActivity(GameActivity);
    }
}



