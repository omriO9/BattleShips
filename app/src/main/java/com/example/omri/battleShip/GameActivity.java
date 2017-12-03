package com.example.omri.battleShip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

//import com.example.omri.batlleship.GameManager;
//import com.example.omri.batlleship.GridButton;
//import com.example.omri.batlleship.HumanPlayer;
//import com.example.omri.batlleship.MainActivity;
//import com.example.omri.batlleship.Player;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = GameActivity.class.getSimpleName();

    private GridLayout myGridLayout;
    private GridLayout enemyGridLayout;
    private GameManager manager;
    private int gridSize;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        CellInfo[][] playerField = p.getMyField().getMyShipsLocation();

        for (int row = 0; row < playerField.length; row++) {
            for (int col = 0; col < playerField.length; col++) {
                View btn = (grid.getChildAt(row + col * gridSize));
                if (playerField[row][col] != null && (p instanceof HumanPlayer)) {
                    btn.setBackgroundResource(playerField[row][col].getImg()); // need int
                    //playerField[row][col].getImg() - Drawable
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
                int myGridLayoutHeight = theGrid.getHeight(); //height is ready
                int cellSize = myGridLayoutHeight / theGrid.getColumnCount();
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
                    BattleField.shotState shotResult = manager.manageGame(target);
                    if(shotResult==BattleField.shotState.HIT || shotResult==BattleField.shotState.SUNK) {
                        MediaPlayer hitSound = MediaPlayer.create(this, R.raw.hit);
                        hitSound.start();
                    }
                    paintAttack(enemyGridLayout, target, shotResult,manager.getPcPlayer());
                    changeArrowImageByTurn(false);// true means its PC's turn
                    if (manager.getPcPlayer().hasBeenDefeated())
                        gameOver(manager.getHumanPlayer());
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BattleField.shotState shotResult = manager.manageGame(null);
                            paintAttack(myGridLayout, manager.getPcPlayer().getLastShot(), shotResult,manager.getHumanPlayer());
                            if (manager.getHumanPlayer().hasBeenDefeated())
                                gameOver(manager.getPcPlayer());
                            changeArrowImageByTurn(true);

                        }
                    }, 1000);
                    //if (manager.isGameOver)
                } else {
                    Toast.makeText(this, "Please wait for your turn", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void changeArrowImageByTurn(boolean b) {

        if(b) {
            img = (ImageView) findViewById(R.id.enemyTurnArrow);
            img.setVisibility(View.INVISIBLE);
            img = (ImageView) findViewById(R.id.myTurnArrow);
            img.setVisibility(View.VISIBLE);
        }
        else{
            img = (ImageView) findViewById(R.id.myTurnArrow);
            img.setVisibility(View.INVISIBLE);
            img = (ImageView) findViewById(R.id.enemyTurnArrow);
            img.setVisibility(View.VISIBLE);
        }

    }

    public void gameOver(Player p) {
        MediaPlayer gameOverSound;
        if (p instanceof HumanPlayer)
            gameOverSound = MediaPlayer.create(this,R.raw.game_won);
        else
            gameOverSound = MediaPlayer.create(this,R.raw.game_lost);
        gameOverSound.start();
        new AlertDialog.Builder(this)
                .setMessage(p.getPlayerName()+" WON the game!!!")
                .setCancelable(false)
                .setPositiveButton("Rematch", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent arrangeBattleFieldActivity = new Intent(GameActivity.this, arrangeBattleFieldActivity.class);
                        arrangeBattleFieldActivity.putExtra("gameDifficulty", manager.getDifficulty());
                        startActivity(arrangeBattleFieldActivity);
                    }
                })
                .setNegativeButton("Back to menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent MainActivity = new Intent(GameActivity.this, MainActivity.class);
                        startActivity(MainActivity);
                    }
                })
                .show();
    }

    public void paintAttack( GridLayout theGrid,Coordinate target,BattleField.shotState hitStatus,Player player) {

                if (hitStatus==BattleField.shotState.SUNK){
                    List<Coordinate> cords = player.getBattleField().getSunkShipCords(target);
                    if (cords!=null) { // i received a list.
                        Log.d(TAG, "paintAttack: cords.size="+cords.size());
                        for (Coordinate cord : cords){
                            //gridLayout.getChildAt(0).setBackgroundResource(manager.getHumanPlayer().getBattleField().getMyShipsLocation()[list2Paint.get(i).getX()][list2Paint.get(i).getY()].getImgExplosioResourceID());
                            if (player.getBattleField().getMyShipsLocation()[cord.getX()][cord.getY()]!=null){
                                if (player.getBattleField().getMyShipsLocation()[cord.getX()][cord.getY()].getImg()!=0)
                                    Log.d(TAG, "paintAttack: there is a ship here");
                            }

                            int img2draw = player.getBattleField().getMyShipsLocation()[cord.getX()][cord.getY()].getImg();
                            // img2draw isn't the right one!!!
                            GridButton btn2Paint = ((GridButton) theGrid.getChildAt(cord.getX() + cord.getY() * gridSize));
                            btn2Paint.setBackgroundResource(img2draw);
                        }
                    }
                }
                else if (hitStatus== BattleField.shotState.HIT) {
                    ((GridButton) theGrid.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(R.drawable.blast);
                }
                else
                    ((GridButton) theGrid.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(R.drawable.miss);


    }


    @Override
    public boolean onSupportNavigateUp(){
        //finish();
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
        return true;
    }

}
