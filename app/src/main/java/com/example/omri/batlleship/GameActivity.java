package com.example.omri.batlleship;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = GameActivity.class.getSimpleName();

    private GridLayout myGridLayout;
    private GridLayout enemyGridLayout;
    private GameManager manager;
    private int gridSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    protected void onResume() {
        super.onResume();
        myGridLayout = (GridLayout) findViewById(R.id.myGridLayout);
        enemyGridLayout = (GridLayout) findViewById(R.id.enemyGridLayout);
        manager = (GameManager) getIntent().getSerializableExtra("GameManager");
        gridSize = manager.getHumanPlayer().getMyField().getMyShipsLocation().length;
        initGridLayout(myGridLayout, manager.getHumanPlayer());
        initGridLayout(enemyGridLayout, manager.getPcPlayer());
    }

    private void paintGridLayout(GridLayout grid, Player p) {
        Log.d(TAG, "paintMyGridLayout: starting to paint");
        String[][] playerField = p.getMyField().getMyShipsLocation();
        for (int row = 0; row < playerField.length; row++) {
            for (int col = 0; col < playerField.length; col++) {
                View btn = (grid.getChildAt(row + col * gridSize));
                if (playerField[row][col] != null) {
                    btn.setBackgroundResource(R.drawable.hit);
                } else
                    btn.setBackgroundResource(R.drawable.cell_border);
            }
        }
    }

    public void initGridLayout(final GridLayout theGrid, Player p) {

        theGrid.setColumnCount(gridSize);
        theGrid.setRowCount(gridSize);
        if (p instanceof HumanPlayer)
            initGridLayoutButtons(theGrid,false);
        else
            initGridLayoutButtons(theGrid,true);
        theGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                theGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int myGridLayoutWidth = theGrid.getWidth(); //height is ready
                int cellSize = myGridLayoutWidth / theGrid.getColumnCount();
                for (int i = 0; i < theGrid.getChildCount(); i++) {
                    GridButton btn = (GridButton) theGrid.getChildAt(i);
                    btn.setPositionX(i % theGrid.getColumnCount());
                    btn.setPositionY(i / theGrid.getColumnCount());
                    btn.getLayoutParams().height = cellSize;
                    btn.getLayoutParams().width = cellSize;
                }
                theGrid.invalidate();
                theGrid.requestLayout();
            }
        });
        paintGridLayout(theGrid, p);
    }

    private void initGridLayoutButtons(GridLayout theGrid,boolean isClickable) {

        int squaresCount = theGrid.getColumnCount() * theGrid.getRowCount();
        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton = new GridButton(this);
            if (isClickable)
                gridButton.setOnClickListener(this);
            theGrid.addView(gridButton);
        }
    }

    @Override
    public void onClick(View v) {
        boolean hitResult; // true = hit , false = miss.
        boolean legalShot; // true = can attack there, false = I already shot there!
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;
            if (gridButton.checkAvailability()== GridButton.State.INUSE)//not available
                Toast.makeText(this, "Why would you shoot this again?!", Toast.LENGTH_SHORT).show();
            else {
                if (manager.isHumanPlayerTurn()) { // human player's tur
                    gridButton.setAvailability(GridButton.State.INUSE);
                    Coordinate target = new Coordinate(gridButton.getPositionX(), gridButton.getPositionY());
                    hitResult = manager.manageGame(target);
                    paintAttack(enemyGridLayout, target, hitResult);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            boolean hitResult = manager.manageGame(null);
                            paintAttack(myGridLayout, manager.getPcPlayer().getLastShot(), hitResult);
                            if (manager.getHumanPlayer().hasBeenDefeated())
                                gameOver(manager.getHumanPlayer());

                        }
                    }, 1000);
                    //if (manager.isGameOver)
                } else {
                    Toast.makeText(this, "Please wait for your turn", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void gameOver(Player p) {
        new AlertDialog.Builder(this)
                .setMessage(p.getPlayerName()+" has been defeated!!!")
                .setCancelable(false)
                .setPositiveButton("Rematch", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(GameActivity.this, arrangeBattleFieldActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Back to menu", null)
                .show();
    }

    public void onQuitPressed(View view) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit this game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(GameActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void paintAttack( GridLayout theGrid,Coordinate target,boolean isHit) {
                if (isHit)
                    ((GridButton) theGrid.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(R.drawable.blast);
                else
                    ((GridButton) theGrid.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(R.drawable.miss);
    }
}
