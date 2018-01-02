package com.example.omri.battleShip;

import java.io.Serializable;

public class GameManager implements Serializable {

    private static final String TAG = GameManager.class.getSimpleName();
    public final static int EASY_GRID_SIZE = 5;
    public final static int MEDIUM_GRID_SIZE = 7;
    public final static int INSANE_GRID_SIZE = 10;

    public final static int EASY_SHIPS_AMOUNT = 3;
    public final static int MEDIUM_SHIPS_AMOUNT = 4;
    public final static int INSANE_SHIPS_AMOUNT = 5;

    // amount of cells that the ships have for the game level (for score counting)
    public final static int EASY_CELLS = 8;
    public final static int MEDIUM_CELLS = 12;
    public final static int INSANE_CELLS = 17;

    public final static String EASY = "Easy";
    public final static String MEDIUM = "Medium";

    private HumanPlayer humanPlayer;
    private PCPlayer pcPlayer;
    private String difficulty;
    private boolean humanPlayerTurn;
    private int numOfShipCells; // for score calculation

    public GameManager(String difficulty){
        this.difficulty=difficulty;
        humanPlayerTurn=true;
    }
    public int[] createPlayers(String humanName,String pcName){

        if(difficulty.equals(EASY)){
            humanPlayer = new HumanPlayer(humanName,EASY_GRID_SIZE,EASY_SHIPS_AMOUNT);
            pcPlayer=new PCPlayer(pcName,EASY_GRID_SIZE,EASY_SHIPS_AMOUNT);
            numOfShipCells=EASY_CELLS;
            return new int []{EASY_GRID_SIZE,EASY_SHIPS_AMOUNT};
        }
        else if(difficulty.equals(MEDIUM)){
            humanPlayer = new HumanPlayer(humanName,MEDIUM_GRID_SIZE,MEDIUM_SHIPS_AMOUNT);
            pcPlayer=new PCPlayer(pcName,MEDIUM_GRID_SIZE,MEDIUM_SHIPS_AMOUNT);
            numOfShipCells=MEDIUM_CELLS;
            return new int []{MEDIUM_GRID_SIZE,MEDIUM_SHIPS_AMOUNT};
        }
        // hard level
        humanPlayer = new HumanPlayer(humanName,INSANE_GRID_SIZE,INSANE_SHIPS_AMOUNT);
        pcPlayer=new PCPlayer(pcName,INSANE_GRID_SIZE,INSANE_SHIPS_AMOUNT);
        numOfShipCells=INSANE_CELLS;
            return new int []{INSANE_GRID_SIZE,INSANE_SHIPS_AMOUNT};
        }


    public BattleField.shotState manageGame(Coordinate target){
        BattleField.shotState returnFlag;
        BattleField.shotState status;
        if (target!=null){ // a human player pressed on grid same as - if (humanPlayerTurn).
            humanPlayerTurn=false;
            humanPlayer.increaseAttempts();
            status = pcPlayer.receiveFire(target);
            if(status==BattleField.shotState.SUNK)
                returnFlag=BattleField.shotState.SUNK;
            else if (status==BattleField.shotState.HIT)
                returnFlag=BattleField.shotState.HIT;
            else
                returnFlag=BattleField.shotState.MISS;
        }
        else { // a pc player has to randomize hit
            pcPlayer.generateShot();
            status=humanPlayer.receiveFire(pcPlayer.getLastShot());
            if(status==BattleField.shotState.SUNK)
                returnFlag= BattleField.shotState.SUNK;
            else if (status==BattleField.shotState.HIT)
                returnFlag= BattleField.shotState.HIT;
            else
                returnFlag=BattleField.shotState.MISS;
            humanPlayerTurn=true;
        }
        return returnFlag;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }
    public PCPlayer getPcPlayer() {
        return pcPlayer;
    }
    public boolean isHumanPlayerTurn() {
        return humanPlayerTurn;
    }

    public int getNumOfCells(){
        // a func that returns the nubmer of ship's cells
        return numOfShipCells;
    }
}
