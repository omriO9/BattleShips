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
    public final static String EASY = "Easy";
    public final static String MEDIUM = "Medium";

    private HumanPlayer humanPlayer;
    private PCPlayer pcPlayer;
    private String difficulty;
    private boolean humanPlayerTurn;

    public GameManager(String difficulty){
        this.difficulty=difficulty;
        humanPlayerTurn=true;
    }
    public int[] createPlayers(String humanName,String pcName){

        if(difficulty.equals(EASY)){
            humanPlayer = new HumanPlayer(humanName,EASY_GRID_SIZE,EASY_SHIPS_AMOUNT);
            pcPlayer=new PCPlayer(pcName,EASY_GRID_SIZE,EASY_SHIPS_AMOUNT);
            return new int []{EASY_GRID_SIZE,EASY_SHIPS_AMOUNT};
        }
        else if(difficulty.equals(MEDIUM)){
            humanPlayer = new HumanPlayer(humanName,MEDIUM_GRID_SIZE,MEDIUM_SHIPS_AMOUNT);
            pcPlayer=new PCPlayer(pcName,MEDIUM_GRID_SIZE,MEDIUM_SHIPS_AMOUNT);
            return new int []{MEDIUM_GRID_SIZE,MEDIUM_SHIPS_AMOUNT};
        }
        // hard level
        humanPlayer = new HumanPlayer(humanName,INSANE_GRID_SIZE,INSANE_SHIPS_AMOUNT);
        pcPlayer=new PCPlayer(pcName,INSANE_GRID_SIZE,INSANE_SHIPS_AMOUNT);
            return new int []{INSANE_GRID_SIZE,INSANE_SHIPS_AMOUNT};
        }


    public BattleField.shotState manageGame(Coordinate target){
        BattleField.shotState returnFlag;
        BattleField.shotState status;
        if (humanPlayerTurn){ // a human player pressed on grid same as - if (humanPlayerTurn).
            humanPlayerTurn=false;
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

}
