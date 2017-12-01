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

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = GameActivity.class.getSimpleName();

    GridLayout myGridLayout;
    GridLayout enemyGridLayout;
    int myGridLayoutWidth;
    PCPlayer pcPlayer;
    HumanPlayer human;
    private boolean turnHumanPlayer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
//        myGridLayout = (GridLayout) findViewById(R.id.myGridLayout);
//        enemyGridLayout = (GridLayout) findViewById(R.id.enemyGridLayout);
//        human = (HumanPlayer)getIntent().getSerializableExtra("human");
//        initGridLayout(myGridLayout);
//        initGridLayout(enemyGridLayout);
//       // paintMyGridLayout();
    }

    protected void onResume() {
        super.onResume();
        myGridLayout = (GridLayout) findViewById(R.id.myGridLayout);
        enemyGridLayout = (GridLayout) findViewById(R.id.enemyGridLayout);
        human = (HumanPlayer) getIntent().getSerializableExtra("human");
        pcPlayer = new PCPlayer("BlueGene",10,5);
        initGridLayout(myGridLayout, human);
        initGridLayout(enemyGridLayout, pcPlayer);


    }

    private void paintGridLayout(GridLayout grid, Player p) {
        Log.d(TAG, "paintMyGridLayout: starting to paint");
        String[][] mat = p.getMyField().getMyShipsLocation();
        for (int row = 0; row < mat.length; row++) {
            for (int col = 0; col < mat.length; col++) {
                View btn = (grid.getChildAt(row + col * 10));
                if (mat[row][col] != null) {
                    btn.setBackgroundResource(R.drawable.hit);
                } else
                    btn.setBackgroundResource(R.drawable.cell_border);
            }
        }


    }

    public void initGridLayout(final GridLayout theGrid, Player p) {
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
                    //Drawable border = ContextCompat.getDrawable(this, R.drawable.cell_border);
                    //btn.setOnClickListener(this);
                    //   Log.d(TAG, "onGlobalLayout: height+size="+cellSize);
                    //btn.setBackgroundResource(R.drawable.cell_border);
                    btn.getLayoutParams().height = cellSize;
                    btn.getLayoutParams().width = cellSize;
                }
                theGrid.invalidate();
                theGrid.requestLayout();
            }
        });
        paintGridLayout(theGrid, p);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;
            if (turnHumanPlayer) { // human player's turn
                attack(v, human);
                turnHumanPlayer = false;
                if (pcPlayer.hasBeenDefeated())
                    playerDefeated(pcPlayer);
            }
            // pc's turn now!
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    attack(null, pcPlayer);
                    turnHumanPlayer=true;
                    if (human.hasBeenDefeated())
                        playerDefeated(human);
                }
            }, 500);

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

    public void playerDefeated(Player p) {
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

    public void attack(View view, Player p) {
        if (p instanceof PCPlayer) {
            Coordinate target = p.attack();
            if (human.receiveFire(target)) {
                ((GridButton) myGridLayout.getChildAt(target.getX() + target.getY() * 10)).setBackgroundResource(R.drawable.blast);
            } else
                ((GridButton) myGridLayout.getChildAt(target.getX() + target.getY() * 10)).setBackgroundResource(R.drawable.miss);
        } else {// p instance of HumanPlayer
            Coordinate target = new Coordinate(((GridButton)view).getPositionX(),((GridButton)view).getPositionY()); //p.attack();
            if (pcPlayer.receiveFire(target)) {
                ((GridButton) enemyGridLayout.getChildAt(target.getX() + target.getY() * 10)).setBackgroundResource(R.drawable.blast);
            } else
                ((GridButton) enemyGridLayout.getChildAt(target.getX() + target.getY() * 10)).setBackgroundResource(R.drawable.miss);
        }
    }
}
