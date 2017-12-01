package com.example.omri.batlleship;

import java.io.Serializable;

/**
 * Created by Mark on 01/12/2017.
 */

public class GameManager implements Serializable {



    HumanPlayer humanPlayer;
    PCPlayer pcPlayer;
    boolean isGameOver;
    private boolean humanPlayerTurn;

    public GameManager(){
        isGameOver=false;
        humanPlayerTurn=true;
    }
    public void createHuman(String name,int sizeOfMap,int numOfShips){
        this.humanPlayer=new HumanPlayer(name,sizeOfMap,numOfShips);
    }
    public void createPC(String name,int sizeOfMap,int numOfShips){
        this.pcPlayer=new PCPlayer(name,sizeOfMap,numOfShips);
    }
    public boolean manageGame(Coordinate target){
        boolean returnFlag;
        if (target!=null){ // a human player pressed on grid same as - if (humanPlayerTurn).
            //humanPlayer.attack(target)
            if(pcPlayer.receiveFire(target))
                returnFlag=true;
            else
                returnFlag=false;
            humanPlayerTurn=false;
        }
        else { // a pc player has to randomize hit
            pcPlayer.generateShot();
            if(humanPlayer.receiveFire(pcPlayer.getLastShot()))
                returnFlag= true;
            else
                returnFlag= false;
            humanPlayerTurn=true;
        }
        return returnFlag;
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

    public void setHumanPlayerTurn(boolean humanPlayerTurn) {
        this.humanPlayerTurn = humanPlayerTurn;
    }
//    public void toggleHumanPlayerTurn() {
//        this.humanPlayerTurn = !this.humanPlayerTurn;
//    }

    public boolean isGameOver(Player p){
        return p.hasBeenDefeated();
    }
}
