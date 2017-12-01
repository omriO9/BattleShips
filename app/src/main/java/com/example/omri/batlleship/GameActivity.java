package com.example.omri.batlleship;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    GridLayout myGridLayout;
    GridLayout enemyGridLayout;
    int myGridLayoutWidth;
    PCPlayer pcPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    protected void onResume() {
        super.onResume();
        pcPlayer = new PCPlayer(10);
        myGridLayout = (GridLayout) findViewById(R.id.myGridLayout);
        enemyGridLayout = (GridLayout) findViewById(R.id.enemyGridLayout);
        initGridLayout(myGridLayout);
        initGridLayout(enemyGridLayout);


    }

    public void initGridLayout(final GridLayout theGrid) {

        int squaresCount = theGrid.getColumnCount() * myGridLayout.getRowCount();
        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton = new GridButton(this);
            gridButton.setOnClickListener(this);
            theGrid.addView(gridButton);
        }
        theGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                theGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                myGridLayoutWidth = theGrid.getWidth();
                int cellSize = myGridLayoutWidth / theGrid.getColumnCount();
                cellSize -= 3;
                for (int i = 0; i < theGrid.getChildCount(); i++) {
                    GridButton btn = (GridButton) theGrid.getChildAt(i);
                    btn.setPositionX(i % theGrid.getColumnCount());
                    btn.setPositionY(i / theGrid.getColumnCount());

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
}
