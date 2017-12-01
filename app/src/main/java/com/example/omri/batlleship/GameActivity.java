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

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = GameActivity.class.getSimpleName();

    GridLayout myGridLayout;
    GridLayout enemyGridLayout;
    int myGridLayoutWidth;
    PCPlayer pcPlayer;
    HumanPlayer human;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        myGridLayout = (GridLayout) findViewById(R.id.myGridLayout);
        enemyGridLayout = (GridLayout) findViewById(R.id.enemyGridLayout);
        human = (HumanPlayer)getIntent().getSerializableExtra("human");
        initGridLayout(myGridLayout);
        initGridLayout(enemyGridLayout);
       // paintMyGridLayout();
    }

    protected void onResume() {
        super.onResume();
        myGridLayout = (GridLayout) findViewById(R.id.myGridLayout);
        enemyGridLayout = (GridLayout) findViewById(R.id.enemyGridLayout);
        human = (HumanPlayer)getIntent().getSerializableExtra("human");
        pcPlayer = new PCPlayer(10);
        initGridLayout(myGridLayout);
        initGridLayout(enemyGridLayout);
       // paintMyGridLayout();

    }

    private void paintMyGridLayout(GridLayout grid,Player p) {
        Log.d(TAG, "paintMyGridLayout: starting to paint");
        String[][] mat = p.getMyField().getMyShipsLocation();
        for (int row=0;row<mat.length;row++){
            for (int col=0;col<mat.length;col++) {
                if (mat[row][col]!=null){
                    Log.d(TAG, "paintMyGridLayout: found something to paint");
                    View btn = (grid.getChildAt(row+col*10));
                    Log.d(TAG, "paintMyGridLayout: Btn="+btn.toString());
                    btn.setBackgroundResource(R.drawable.hit);
                    Log.d(TAG, "paintMyGridLayout: supposed to be view changed!!!");
                }
                //Button quit = (Button)findViewById(R.id.quitButton);
                //quit.setBackgroundColor(col*row);
            }
        }



    }

    public void initGridLayout(final GridLayout theGrid) {

//        int cellSize = gridLayoutWidth / gridLayout.getColumnCount(); // 910
//        Log.d(TAG, "onResume: gridLayoutWidth="+gridLayoutWidth);
//        cellSize -= 3;
        int squaresCount = theGrid.getColumnCount() * theGrid.getRowCount();
        Log.d(TAG, "initGridLayout: squaresCount="+squaresCount);
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
                    btn.setBackgroundResource(R.drawable.cell_border);
                    btn.getLayoutParams().height = cellSize;
                    btn.getLayoutParams().width = cellSize;
                }
                theGrid.invalidate();
                theGrid.requestLayout();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;
            gridButton.setBackgroundResource(R.drawable.hit);
//            v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
//                @Override
//                public void run() {
//                    gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
//
//                }
//            }).start();
        }



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

    public void startGame(View view) {
        paintMyGridLayout(myGridLayout,human);
        paintMyGridLayout(enemyGridLayout,pcPlayer);
    }
}
