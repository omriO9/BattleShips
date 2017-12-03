package com.example.omri.battleShip;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mark on 30/11/2017.
 */

public class BattleField implements Serializable {

    public enum shotState {MISS,HIT,SUNK};

    private static final String TAG = BattleField.class.getSimpleName();
    private final static int MEDIUM_SHIPS_AMOUNT = 4;
    private final static int INSANE_SHIPS_AMOUNT = 5;
    private shotState status;
    protected CellInfo[][] myShipsLocation;
    protected HashMap<String,BattleShip> shipMap;


public BattleField(int size,int numOfShips){
    // size = number of rows/cols
    myShipsLocation= new CellInfo[size][size];
    initmyShipsLocation();
    shipMap=new HashMap<>();
    initShipMap(numOfShips);
}

    private void initmyShipsLocation() {
        for (int i=0;i<myShipsLocation.length;i++){
            for (int j=0;j<myShipsLocation.length;j++){
                myShipsLocation[i][j]=null;//new CellInfo(null,null);
            }
        }
    }

    public void initShipMap(int numOfShips) {

        shipMap.put("ship2",new BattleShip("ship2",2,true,new Coordinate(-1,-1)));
        shipMap.put("ship3",new BattleShip("ship3",3,true,new Coordinate(-1,-1)));
        shipMap.put("ship3_2",new BattleShip("ship3_2",3,true,new Coordinate(-1,-1)));

        if(numOfShips>= MEDIUM_SHIPS_AMOUNT){
            shipMap.put("ship4",new BattleShip("ship4",4,true,new Coordinate(-1,-1)));
            if(numOfShips ==INSANE_SHIPS_AMOUNT )
                shipMap.put("ship5",new BattleShip("ship5",5,true,new Coordinate(-1,-1)));
        }
    }
    public boolean isShipInXY(int x, int y){

        if(myShipsLocation[x][y]==null)
            return true;
        return false;

    }

    public List<Coordinate> showPossiblePositions(Coordinate shipPos, String name) {
        // A method that gets a clicked location + ship's name and returns a list of possible positions
        // to place the ship.
        List<Coordinate> listOfPossibilities = new ArrayList<>();
        boolean isBlocked = false;

            //running over player's BS and checking which ship i'm trying to position
            BattleShip ship=shipMap.get(name);

                if (shipPos.getX() + ship.getLength() <= myShipsLocation.length) {// to the right
                    Coordinate desiredPos = new Coordinate(shipPos.getX() + ship.getLength()-1,shipPos.getY());
                    isBlocked= checkIfBlockedByShip(shipPos,desiredPos);
                    // we can take this position
                    if(!isBlocked)
                        listOfPossibilities.add(desiredPos);
                }

                if (shipPos.getX() - ship.getLength() + 1 >= 0) {// to the left
                    Coordinate desiredPos = new Coordinate(shipPos.getX() - ship.getLength() + 1,shipPos.getY());
                    isBlocked= checkIfBlockedByShip(shipPos,desiredPos);
                    Log.d(TAG, "mrk: shipPos="+shipPos+" desiredPos="+desiredPos+"isblocked="+isBlocked);
                    if(!isBlocked)
                        listOfPossibilities.add(desiredPos);
                }

                if (shipPos.getY() + ship.getLength() <= myShipsLocation.length) {// to the down
                    Coordinate desiredPos = new Coordinate(shipPos.getX(),shipPos.getY() + ship.getLength()-1);
                    isBlocked= checkIfBlockedByShip(shipPos,desiredPos);
                    if(!isBlocked)
                        listOfPossibilities.add(desiredPos);
                }

                if (shipPos.getY() - ship.getLength()+1 >= 0) {// to the up
                    Coordinate desiredPos = new Coordinate(shipPos.getX(),shipPos.getY() - ship.getLength()+1);
                    isBlocked= checkIfBlockedByShip(shipPos,desiredPos);
                    if(!isBlocked)
                        listOfPossibilities.add(desiredPos);
                }
                return listOfPossibilities;
            }

    public List<Coordinate> placeShip(Coordinate shipStartingPos, Coordinate requestedPosition,String shipName) {
        // func gets starting ship position and desired position to place ship
        // returns list of coordinates that the ship is placed in to paint on grid
        List<Coordinate> CordsToPaint = new ArrayList<>();
        if (shipStartingPos.getX()==requestedPosition.getX()) { // we have same X - same column
            if (shipStartingPos.getY() < requestedPosition.getY()) { // fill it down
                for (int i = shipStartingPos.getY(); i <= requestedPosition.getY(); i++) {
                    myShipsLocation[shipStartingPos.getX()][i]=new CellInfo(shipName,-1);
                    CordsToPaint.add(new Coordinate(shipStartingPos.getX(),i));
                }
            } else if (shipStartingPos.getY() > requestedPosition.getY()) { //fill it up
                for (int i = requestedPosition.getY(); i <= shipStartingPos.getY(); i++) {
                    myShipsLocation[shipStartingPos.getX()][i]=new CellInfo(shipName,-1);
                    CordsToPaint.add(new Coordinate(shipStartingPos.getX(),i));
                }
            }
        }
        else {//if(pos.getY()==gridButton.getPositionY()){
            if(shipStartingPos.getX()< requestedPosition.getX()) { // fill it right
                for (int i = shipStartingPos.getX(); i <=requestedPosition.getX(); i++) {
                    myShipsLocation[i][shipStartingPos.getY()]=new CellInfo(shipName,-1);
                    CordsToPaint.add(new Coordinate(i,shipStartingPos.getY()));
                }
            }
            else if(shipStartingPos.getX()> requestedPosition.getX()){ //fill it left
                for (int i = requestedPosition.getX(); i <= shipStartingPos.getX(); i++) {
                    myShipsLocation[i][shipStartingPos.getY()]=new CellInfo(shipName,-1);
                    CordsToPaint.add(new Coordinate(i,shipStartingPos.getY()));
                }
            }
        }
        return CordsToPaint;
    }

    public void printMat() {
        for (int i = 0; i < myShipsLocation.length; i++) {
            for (int j = 0; j < myShipsLocation.length; j++) {
                if (myShipsLocation[i][j] != null) {
                    Log.d(TAG, "printMat: [" + i + "," + j + "]=" + myShipsLocation[i][j]);
                }
            }
        }
    }

    public List<Coordinate> getSunkShipCords(Coordinate target){
        List<Coordinate> CordsToPaint = new ArrayList<>();
        String shipName = myShipsLocation[target.getX()][target.getY()].getShipName();
        for (int i=0;i<myShipsLocation.length;i++){
            for (int j=0;j<myShipsLocation.length;j++){
                if(myShipsLocation[i][j].getShipName().equals(shipName)){
                    CordsToPaint.add(new Coordinate(i,j));
                }
            }
        }
        return CordsToPaint;
    }


    public boolean checkIfBlockedByShip(Coordinate shipPos, Coordinate desiredPos){
            // pos = ship's starting (pressed) location ,
            // desiredPos = ship's suggested location by showPossiblePositions function.

            if (shipPos.getX()==desiredPos.getX()) { // we have same X - same column
                if (shipPos.getY() < desiredPos.getY()) { // fill it down
                    for (int i = shipPos.getY(); i <= desiredPos.getY(); i++) {
                        if(myShipsLocation[shipPos.getX()][i] != null)
                            return true;
                    }
                } else if (shipPos.getY() > desiredPos.getY()) { //fill it up
                    for (int i = desiredPos.getY(); i <= shipPos.getY(); i++) {
                        if(myShipsLocation[shipPos.getX()][i] != null)
                            return true;
                    }
                }
            }
            else {//if(pos.getY()==gridButton.getPositionY()){
                if(shipPos.getX()< desiredPos.getX()) { // fill it right
                    for (int i = shipPos.getX(); i <=desiredPos.getX(); i++) {
                       if(myShipsLocation[i][shipPos.getY()] !=null)
                           return true;
                    }
                }
                else if(shipPos.getX()> desiredPos.getX()){ //fill it left
                    for (int i = desiredPos.getX(); i <= shipPos.getX(); i++) {
                        if(myShipsLocation[i][shipPos.getY()] !=null)
                            return true;
                    }
                }
            }
            return false;
        }
    public CellInfo[][] getMyShipsLocation() {
        return myShipsLocation;
    }
    public HashMap<String, BattleShip> getShipMap() {
        return shipMap;
    }

    public shotState shipWasHit(String name){
        // receives a ship's name that was hit - updates it's number of hits +1 , if it was sunk then return
        // true , else just updates and returns false.
        BattleShip b= shipMap.get(name);
        //Log.d(TAG, "shipWasHit: setting to ship="+b.getName()+" new hit number = "+b.getNumberOfHits()+1);
        int newNumOfHits = b.getNumberOfHits()+1;
        Log.d(TAG, "shipWasHit: newNumOfHits="+newNumOfHits);
        b.setNumberOfHits(newNumOfHits);
        Log.d(TAG, "shipWasHit: b has numOfhits="+b.getNumberOfHits());
        if (b.getNumberOfHits()==b.getLength()){
            b.setSunk(true);
            return status.SUNK;
        }
        return status.HIT;
    }

    public boolean isAllShipsSunk(){
        for(Map.Entry<String,BattleShip> entry : shipMap.entrySet()){
            if (!entry.getValue().isSunk())
                return false; // there's still atleast 1 ship not sunk.
        }
        return true;
    }
}

