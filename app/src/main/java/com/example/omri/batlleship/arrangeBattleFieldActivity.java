package com.example.omri.batlleship;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;

public class arrangeBattleFieldActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = arrangeBattleFieldActivity.class.getSimpleName();
    GridLayout gridLayout;
    int gridLayoutWidth;

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
        initGridLayout();

    }

    public void initGridLayout(){
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
//        int cellSize = gridLayoutWidth / gridLayout.getColumnCount(); // 910
//        Log.d(TAG, "onResume: gridLayoutWidth="+gridLayoutWidth);
//        cellSize -= 3;
        int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
        for (int i = 0; i < squaresCount; i++) {
            GridButton gridButton= new GridButton(this);
            gridButton.setOnClickListener(this);
            gridLayout.addView(gridButton);
        }
        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                gridLayoutWidth=gridLayout.getWidth(); //height is ready
                int cellSize = gridLayoutWidth / gridLayout.getColumnCount(); // 910
                Log.d(TAG, "onResume: gridLayoutWidth="+gridLayoutWidth);
                cellSize -= 3;
               // int squaresCount = gridLayout.getColumnCount() * gridLayout.getRowCount();
                for (int i = 0; i < gridLayout.getChildCount(); i++) {
                    GridButton btn = (GridButton) gridLayout.getChildAt(i);
                    btn.setPositionX(i % gridLayout.getColumnCount());
                    btn.setPositionY(i / gridLayout.getColumnCount());
                    //Drawable border = ContextCompat.getDrawable(this, R.drawable.cell_border);
                    //btn.setOnClickListener(this);
                    Log.d(TAG, "onGlobalLayout: height+size="+cellSize);
                    btn.setBackgroundResource(R.drawable.cell_border);
                    //btn.setHeight(cellSize);
                   // btn.setWidth(cellSize);
                    //btn.setLayoutParams(btn.getLayoutParams());
                    btn.getLayoutParams().height=cellSize;
                    btn.getLayoutParams().width=cellSize;
                   // btn.setLayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
                }
                gridLayout.invalidate();
                gridLayout.requestLayout();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v instanceof  GridButton) {
            final GridButton gridButton = (GridButton) v;
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
//    @Override
//    public void onClick(View v) {
//        if (v instanceof GridButton) {
//            final GridButton gridButton = (GridButton) v;
//            gameLogic.onPlayerClicked(gridButton.getPositionX(),+ gridButton.getPositionY());
//            v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
//                @Override
//                public void run() {
//                    gridButton.animate().scaleY(1).scaleX(1).setDuration(200).start();
//                }
//            }).start();
//
//        }
//    }
}
