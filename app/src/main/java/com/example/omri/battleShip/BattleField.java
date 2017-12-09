package com.example.omri.battleShip;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mark on 30/11/2017.
 */

public class BattleField implements Serializable {

    public enum shotState {MISS,HIT,SUNK};
    private static final String TAG = BattleField.class.getSimpleName();
    private final static int MEDIUM_SHIPS_AMOUNT = 4;
    private final static int INSANE_SHIPS_AMOUNT = 5;
    private CellInfo[][] myShipsLocation;
    private HashMap<String,BattleShip> shipMap;


public BattleField(int size,int numOfShips){
    // size = number of rows/cols
    myShipsLocation= new CellInfo[size][size];
    initMyShipsLocation();
    shipMap=new HashMap<>();
    initShipMap(numOfShips);
}

    private void initMyShipsLocation() {
        for (int i=0;i<myShipsLocation.length;i++){
            for (int j=0;j<myShipsLocation.length;j++){
                myShipsLocation[i][j]=null;//new CellInfo(null,null);
            }
        }
    }

    private void initShipMap(int numOfShips) {

        shipMap.put("ship2",new BattleShip("ship2",2));
        shipMap.put("ship3",new BattleShip("ship3",3));
        shipMap.put("ship3_2",new BattleShip("ship3_2",3));

        if(numOfShips>= MEDIUM_SHIPS_AMOUNT){
            shipMap.put("ship4",new BattleShip("ship4",4));
            if(numOfShips ==INSANE_SHIPS_AMOUNT )
                shipMap.put("ship5",new BattleShip("ship5",5));
        }
    }
    public boolean isShipInXY(int x, int y){

        if(myShipsLocation[x][y]==null)
            return true;
        return false;

    }

    public List<Coordinate> getSunkShipCords(Coordinate target) {

        List<Coordinate> list2return = new ArrayList<>();
        if (myShipsLocation[target.getX()][target.getY()]!=null) {
            String shipName = myShipsLocation[target.getX()][target.getY()].getShipName();
            for (int i = 0; i < myShipsLocation.length; i++) {
                for (int j = 0; j < myShipsLocation.length; j++) {
                    if (myShipsLocation[i][j] != null) { // there's a ship here
                        if (myShipsLocation[i][j].getShipName().equals(shipName)) { // found the specific ship
                            Coordinate c = new Coordinate(i, j);
                            list2return.add(c);
                        }
                    }
                }
            }
        }
        else {
            Log.d(TAG, "getSunkShipCords: trying to reach null ship...");
        }
        return list2return;
    }

    public List<Coordinate> showPossiblePositions(Coordinate shipPos, String name) {
        // A method that gets a clicked location + ship's name and returns a list of possible positions
        // to place the ship.
        List<Coordinate> listOfPossibilities = new ArrayList<>();
        boolean isBlocked;

            //running over player's battleShip and checking which ship i'm trying to position
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
                    Log.d(TAG, "showPossiblePositions: shipPos="+shipPos+" desiredPos="+desiredPos+"isblocked="+isBlocked);
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
            int front,center,rear,frontEx,centerEx,rearEx;
            boolean isVertical=false;
            if (shipStartingPos.getX() == requestedPosition.getX())
                isVertical = true;
            if (isVertical) {
                Log.d(TAG, "placeShip: isVertical=true");
                front = R.drawable.front_vertical;
                center = R.drawable.center_vertical;
                rear = R.drawable.rear_vertical;
                frontEx = R.drawable.front_vertical_ex;
                centerEx = R.drawable.center_vertical_ex;
                rearEx = R.drawable.rear_vertical_ex;
            } else {
                front = R.drawable.front;
                center = R.drawable.center;
                rear = R.drawable.rear;
                frontEx = R.drawable.front_ex;
                centerEx = R.drawable.center_ex;
                rearEx = R.drawable.rear_ex;
            }
        List<Coordinate> cordsOfShip = createCordList(shipStartingPos,requestedPosition);
            for (int i=0;i<cordsOfShip.size();i++){
                Log.d(TAG, "initBattleShipsRandomly: Cord="+cordsOfShip.get(i));
                Coordinate currCoord = cordsOfShip.get(i);
                int x = currCoord.getX() , y = currCoord.getY();
                if (i==0) // rear
                    myShipsLocation[x][y] = new CellInfo(shipName,front,frontEx);
                else if (i==cordsOfShip.size()-1) //front
                    myShipsLocation[x][y] = new CellInfo(shipName,rear,rearEx);
                else //center
                    myShipsLocation[x][y] = new CellInfo(shipName,center,centerEx);
            }
        return cordsOfShip;
    }
    public List<Coordinate> createCordList(Coordinate start,Coordinate end){

        List<Coordinate> cordList = new ArrayList<>();
        if (start.getX()==end.getX()) { // Vertical running
            if (start.getY() < end.getY()) { // fill it down
                for (int i = start.getY(); i <= end.getY(); i++) {
                    cordList.add(new Coordinate(start.getX(),i));
                }
            }
            else {
                for (int i = end.getY(); i <= start.getY(); i++) {
                    cordList.add(new Coordinate(start.getX(),i));
                }
            }
        }
        else {
            if(start.getX()< end.getX()) { // fill it right
                for (int i = start.getX(); i <=end.getX(); i++) {
                    cordList.add(new Coordinate(i,start.getY()));
                }
            }
            else if(start.getX()> end.getX()){ //fill it left
                for (int i = end.getX(); i <= start.getX(); i++) {
                    cordList.add(new Coordinate(i,start.getY()));
                }
            }
        }
        Log.d(TAG, "createCordList: printing cordList before return= "+cordList.toString());
        return cordList;
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

    public BattleField.shotState shipWasHit(String name){
        // receives a ship's name that was hit - updates it's number of hits +1 , if it was sunk then return
        // true , else just updates and returns false.
        BattleShip b= shipMap.get(name);
        int newNumOfHits = b.getNumberOfHits()+1;
        Log.d(TAG, "shipWasHit: newNumOfHits="+newNumOfHits);
        b.setNumberOfHits(newNumOfHits);
        if (b.isSunk()){
            b.setSunk();
            return shotState.SUNK;
        }
        return shotState.HIT;
    }
        /* a func for debugging logic only
    public void printMat() {
        for (int i = 0; i < myShipsLocation.length; i++) {
            for (int j = 0; j < myShipsLocation.length; j++) {
                if (myShipsLocation[i][j] != null) {
                    Log.d(TAG, "printMat: shipName="+myShipsLocation[i][j].getShipName()+"[" + i + "," + j + "]=" + myShipsLocation[i][j]);
                }
            }
        }
    }*/
}

