package com.example.omri.battleShip;

import java.io.Serializable;

/**
 * Created by Mark on 02/12/2017.
 */

public class CellInfo implements Serializable {



    private String shipName;
    private int imgResourceID;

    public CellInfo(String name,int id){
        this.shipName=name;
        this.imgResourceID=id; // -1 - default value means not initilized yet.
    }
    public String getShipName() {
        return shipName;
    }

    public int getImg() {
        return imgResourceID;
    }
    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public void setImgResourceID(int id) {
        this.imgResourceID = id;
    }


}
