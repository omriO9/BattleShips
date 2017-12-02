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

    GridLayout myGridLayout;
    GridLayout enemyGridLayout;
    int myGridLayoutWidth;
    PCPlayer pcPlayer;
    GameManager manager;
    private boolean turnHumanPlayer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    protected void onResume() {
        super.onResume();
        myGridLayout = (GridLayout) findViewById(R.id.myGridLayout);
        enemyGridLayout = (GridLayout) findViewById(R.id.enemyGridLayout);
//        human = (HumanPlayer) getIntent().getSerializableExtra("human");
//        int gridSize = (Integer) getIntent().getSerializableExtra("gridSize");
//        int numOfShips = (Integer) getIntent().getSerializableExtra("numOfShips");
        manager = (GameManager) getIntent().getSerializableExtra("GameManager");
        manager.createPC("BlueGene",10,5);
//        initGridLayout(myGridLayout, manager.getHumanPlayer());
//        initGridLayout(enemyGridLayout, manager.getPcPlayer());
//        initPCPlayer(gridSize,numOfShips);
//        //pcPlayer = new PCPlayer("BlueGene",10,5);
//        initGridLayout(myGridLayout, human,gridSize);
//        initGridLayout(enemyGridLayout, pcPlayer,gridSize);
    }


    private void initPCPlayer(int gridSize, int numOfShips) {

        pcPlayer = new PCPlayer("BlueGene",gridSize,numOfShips);
    }

    private void paintGridLayout(GridLayout grid, Player p, int gridSize) {
        Log.d(TAG, "paintMyGridLayout: starting to paint");
        String[][] mat = p.getMyField().getMyShipsLocation();
        for (int row = 0; row < mat.length; row++) {
            for (int col = 0; col < mat.length; col++) {
                View btn = (grid.getChildAt(row + col * gridSize));
                if (mat[row][col] != null) {
                    btn.setBackgroundResource(R.drawable.hit);
                } else
                    btn.setBackgroundResource(R.drawable.cell_border);
            }
        }


    }

    public void initGridLayout(final GridLayout theGrid, Player p, int gridSize) {

        theGrid.setColumnCount(gridSize);
        theGrid.setRowCount(gridSize);

        int squaresCount = theGrid.getColumnCount() * theGrid.getRowCount();
        Log.d(TAG, "initGridLayout: squaresCount=" + squaresCount);
        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton = new GridButton(this);
            gridButton.setOnClickListener(this);
            theGrid.addView(gridButton);
        }



        theGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                theGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                myGridLayoutWidth = theGrid.getWidth(); //height is ready
                int cellSize = myGridLayoutWidth / theGrid.getColumnCount(); // 910
                // Log.d(TAG, "onResume: gridLayoutWidth="+gridLayoutWidth);
                //cellSize -= 3;
                // int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
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
        paintGridLayout(theGrid, p,gridSize);
    }

    @Override
    public void onClick(View v) {
        boolean hitResult; // true = hit , false = miss.
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;
            if (manager.isHumanPlayerTurn()) { // human player's turn
                manager.setHumanPlayerTurn(false);
                Coordinate target = new Coordinate(gridButton.getPositionX(),gridButton.getPositionY());
                hitResult=manager.manageGame(target);
                paintAttack(enemyGridLayout,target,hitResult);
                //attack(gridButton, manager.getHumanPlayer());
                //turnHumanPlayer = false;
                //if (pcPlayer.hasBeenDefeated())
                //   gameOver(pcPlayer);
                // pc's turn now!
                //hitResult=
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean hitResult=manager.manageGame(null);
                        paintAttack(myGridLayout,manager.getPcPlayer().getLastShot(),hitResult);
                        if (manager.getHumanPlayer().hasBeenDefeated())
                            gameOver(manager.getHumanPlayer());
                        //turnHumanPlayer=true;
                    }
                }, 1000);
                //if (manager.isGameOver)
            }
            else {
                Toast.makeText(this, "Please wait for your turn", Toast.LENGTH_SHORT).show();
            }


            //gridButton.setBackgroundResource(R.drawable.hit);
//            v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
//                @Override
//                public void run() {
//                    gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
//
//                }
//            }).start();
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

//    public void attack(View view, Player p) {
//        int gridSize = myGridLayout.getColumnCount();
//        if (p instanceof PCPlayer) {
//            Coordinate target = p.attack();
//            if (human.receiveFire(target)) {
//                ((GridButton) myGridLayout.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(R.drawable.blast);
//            } else
//                ((GridButton) myGridLayout.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(R.drawable.miss);
//        } else {// p instance of HumanPlayer
//            Coordinate target = new Coordinate(((GridButton) view).getPositionX(), ((GridButton) view).getPositionY()); //p.attack();
//            if (pcPlayer.receiveFire(target)) {
//                ((GridButton) enemyGridLayout.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(R.drawable.blast);
//            } else
//                ((GridButton) enemyGridLayout.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(R.drawable.miss);
//        }
//    }
    public void paintAttack( GridLayout theGrid,Coordinate target,boolean isHit) {

                if (isHit)
                    ((GridButton) theGrid.getChildAt(target.getX() + target.getY() * 10)).setBackgroundResource(R.drawable.blast);
                else
                    ((GridButton) theGrid.getChildAt(target.getX() + target.getY() * 10)).setBackgroundResource(R.drawable.miss);
//
//                if (p instanceof PCPlayer) {
//                    Coordinate target = p.attack();
//                    if (manager.getHumanPlayer().receiveFire(target)) {
//                        ((GridButton) myGridLayout.getChildAt(target.getX() + target.getY() * 10)).setBackgroundResource(R.drawable.blast);
//                    } else
//                        ((GridButton) myGridLayout.getChildAt(target.getX() + target.getY() * 10)).setBackgroundResource(R.drawable.miss);
//                }
//                else {// p instance of HumanPlayer
//                    Coordinate target = new Coordinate(((GridButton)view).getPositionX(),((GridButton)view).getPositionY()); //p.attack();
//                    if (pcPlayer.receiveFire(target)) {
//                        ((GridButton) enemyGridLayout.getChildAt(target.getX() + target.getY() * 10)).setBackgroundResource(R.drawable.blast);
//                    } else
//                         ((GridButton) enemyGridLayout.getChildAt(target.getX() + target.getY() * 10)).setBackgroundResource(R.drawable.miss);
//                }
    }
}
