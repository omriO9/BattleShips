package com.example.omri.battleShip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omri.battleShip.Data.shipsOpenHelper;

import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, UpdateListener, LocationListener {

    private static final String TAG = GameActivity.class.getSimpleName();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;


    private GridLayout myGridLayout;
    private GridLayout enemyGridLayout;
    private GameManager manager;
    private int gridSize;
    private boolean isSound = true;

    private double [] LatLong;

    private Location currentLocation = null;
    private LocationManager locationManager;
    private boolean didAlreadyRequestLocationPermission;
    private shipsOpenHelper dbHelper;



    private boolean isServiceConnected;
    public SensorService.MyBinder binder;
    private TextView accelometerText;

    private SensorManager sensorManager;
    HandlerThread sensorThread;
    private Handler sensorHandler;

    private float[] currXYZ;
    private boolean isXYZinitialized;
    private float[] initialXYZ;
    private float max;
    private int posOfMax;
    private boolean isScreenMoved;
    float score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        currXYZ = new float[3];
        isXYZinitialized = false;
        isScreenMoved = false;
        // db testing //
        dbHelper = new shipsOpenHelper(this);
        //this.deleteDatabase(dbHelper.getDatabaseName()); // // delete a db
        //Location flag initialized
        didAlreadyRequestLocationPermission = false;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myGridLayout = (GridLayout) findViewById(R.id.myGridLayout);
        enemyGridLayout = (GridLayout) findViewById(R.id.enemyGridLayout);
        manager = (GameManager) getIntent().getSerializableExtra("GameManager");
        isSound = getIntent().getBooleanExtra("isSound", true);
        gridSize = manager.getHumanPlayer().getMyField().getMyShipsLocation().length;
        initGridLayout(myGridLayout, manager.getHumanPlayer());
        initGridLayout(enemyGridLayout, manager.getPcPlayer());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        beginService();
        accelometerText = (TextView) findViewById(R.id.accelometer_text);
    }

    private void beginService() {
        Log.d(TAG, "beginService: start");
        Intent intent = new Intent(this,
                SensorService.class);
        // intent.putExtra(PATH_KEY, getPathToImage());

        startService(intent); // starts the service...
        if (!isServiceConnected) {
            bindService(intent, boundService, Context.BIND_AUTO_CREATE);
            Log.d(TAG, "beginService: finished binding");
        }
        sensorThread = new HandlerThread(SensorService.class.getSimpleName());
        sensorThread.start();
        sensorHandler = new Handler(sensorThread.getLooper());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    private void paintGridLayout(GridLayout grid, Player p) {
        Log.d(TAG, "paintMyGridLayout: starting to paint");
        CellInfo[][] playerField = p.getMyField().getMyShipsLocation();
        for (int row = 0; row < playerField.length; row++) {
            for (int col = 0; col < playerField.length; col++) {
                View btn = (grid.getChildAt(row + col * gridSize));
                if (playerField[row][col] != null) { //&& (p instanceof HumanPlayer)) {
                    btn.setBackgroundResource(playerField[row][col].getImg());
                } else
                    btn.setBackgroundResource(R.drawable.cell_border);
            }
        }
    }

    public void initGridLayout(final GridLayout theGrid, Player p) {

        theGrid.setColumnCount(gridSize);
        theGrid.setRowCount(gridSize);
        if (p instanceof HumanPlayer)
            initGridLayoutButtons(theGrid, false);
        else
            initGridLayoutButtons(theGrid, true);
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

    private void initGridLayoutButtons(GridLayout theGrid, boolean isClickable) {
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
        if (v instanceof GridButton) {
            final GridButton gridButton = (GridButton) v;
            if (gridButton.checkAvailability() == GridButton.State.INUSE)//not available
                Toast.makeText(this, "Why would you shoot this again?!", Toast.LENGTH_SHORT).show();
            else {
                if (manager.isHumanPlayerTurn()) { // human player's turn
                    gridButton.setAvailability(GridButton.State.INUSE);
                    Coordinate target = new Coordinate(gridButton.getPositionX(), gridButton.getPositionY());
                    BattleField.shotState shotResult = manager.manageGame(target);
                    if ((shotResult == BattleField.shotState.HIT || shotResult == BattleField.shotState.SUNK) && isSound) {
                        MediaPlayer hitSound = MediaPlayer.create(this, R.raw.hit);
                        hitSound.start();
                    }
                    paintAttack(enemyGridLayout, target, shotResult, manager.getPcPlayer());
                    changeArrowImageByTurn(false);// true means its PC's turn
                    if (manager.getPcPlayer().hasBeenDefeated()) {
                        gameOver(manager.getHumanPlayer());
                        return; // to block a case where PC attacks after you shot and won!
                    }
                    // PCPlayer begins to attack :
                    // Decision to make : if the screen was rotated 'too much' - PCPlayer attacks TWICE?
                    int numberOfPCTurns = 1;
                    if (isScreenMoved) { // Human gets Penalty - PC has 2 attacks!
                        Log.d(TAG, "onClick: inside isScreenMoved");
                        numberOfPCTurns = 2;
                        isScreenMoved = false;
                        Toast.makeText(this, "Penalty=Pc has 2 attacks!", Toast.LENGTH_SHORT).show();
                    }
                    Random r = new Random();
                    final Handler handler = new Handler();
                    for (int i = 0; i < numberOfPCTurns; i++) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BattleField.shotState shotResult = manager.manageGame(null);
                                paintAttack(myGridLayout, manager.getPcPlayer().getLastShot(), shotResult, manager.getHumanPlayer());
                                if (manager.getHumanPlayer().hasBeenDefeated())
                                    gameOver(manager.getPcPlayer());
                                changeArrowImageByTurn(true);

                            }
                        }, 50 + r.nextInt(50));

                    }
                    //changeArrowImageByTurn(true);
                } else {
                    Toast.makeText(this, "Please wait for your turn", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void changeArrowImageByTurn(boolean b) {
        ImageView img;
        if (b) {
            img = (ImageView) findViewById(R.id.enemyTurnArrow);
            img.setVisibility(View.INVISIBLE);
            img = (ImageView) findViewById(R.id.myTurnArrow);
            img.setVisibility(View.VISIBLE);
        } else {
            img = (ImageView) findViewById(R.id.myTurnArrow);
            img.setVisibility(View.INVISIBLE);
            img = (ImageView) findViewById(R.id.enemyTurnArrow);
            img.setVisibility(View.VISIBLE);
        }

    }

    public void gameOver(Player p) {
        if (isSound) {
            MediaPlayer gameOverSound;
            if (p instanceof HumanPlayer)
                gameOverSound = MediaPlayer.create(this, R.raw.game_won);
            else
                gameOverSound = MediaPlayer.create(this, R.raw.game_lost);
            gameOverSound.start();
        }
        //String name = p.getPlayerName();

        score = (float) (manager.getHumanPlayer().getNumOfAttempts()) / manager.getNumOfCells();
        String scoreString = String.format("%.2f", score);
        score = Float.parseFloat(scoreString);

        Log.d(TAG, "gameOver: shots=" + manager.getHumanPlayer().getNumOfAttempts() +
                "\nnumOfCells=" + manager.getNumOfCells() +
                "\nscore=" + score);
        //dbHelper.put(name, score, manager.getDifficulty());

        //View viewInflated = LayoutInflater.from(this.inflate(R.layout.text_input_password, (ViewGroup) getView(), false);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        if(!isPermissionForLocationServicesGranted()) {
            Log.d(TAG, "gameOver: inside hereeeeee");
            requestLocationPermissionsIfNeeded(false);
        }
        //LatLong = getLocation();

        new AlertDialog.Builder(this)
                .setView(input)
                .setMessage("Congratulations,\nyou WON the game!!!")
                .setCancelable(false)
                .setPositiveButton("Rematch", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = input.getText() == null ? "John Doe" : input.getText().toString();

                            if (isPermissionForLocationServicesGranted()) {
                                LatLong = getLocation();
                                dbHelper.put(name, score, manager.getDifficulty(), LatLong[0], LatLong[1]);
                            } else {
                                Toast.makeText(getApplicationContext(), "You will not be added to our database :(", Toast.LENGTH_SHORT).show();
                            }

                            Intent arrangeBattleFieldActivity = new Intent(GameActivity.this, arrangeBattleFieldActivity.class);
                            arrangeBattleFieldActivity.putExtra("gameDifficulty", manager.getDifficulty());
                            arrangeBattleFieldActivity.putExtra("isSound", isSound);
                            GameActivity.super.onBackPressed();
                            startActivity(arrangeBattleFieldActivity);

                    }
                })
                .setNegativeButton("Back to menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String name= input.getText() == null ? "John Doe" : input.getText().toString();
                        if (isPermissionForLocationServicesGranted()) {
                            LatLong = getLocation();
                            dbHelper.put(name, score, manager.getDifficulty(), LatLong[0], LatLong[1]);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "You will not be added to our database :(", Toast.LENGTH_SHORT).show();
                        }
                        arrangeBattleFieldActivity.shouldDie = true;
                        GameActivity.super.onBackPressed();
                    }
                })
                .show();
        //Log.d(TAG, "gameOver: input text="+input.toString());
    }

    private boolean isPermissionForLocationServicesGranted() {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (!(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED));

    }

    @SuppressLint("MissingPermission")
    public double[] getLocation() {
        Location location;
        double latitude = -1, longitude = -1;


        // checking if user have the necessary permissions , if not we are asking him to allow those permissions
        if (isPermissionForLocationServicesGranted()) {
            if (currentLocation == null) {
                // get current location
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            float metersToUpdate = 1;
            long intervalMilliseconds = 1000;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, intervalMilliseconds, metersToUpdate, this);
        }
            if (currentLocation != null) {
                latitude = currentLocation.getLatitude();
                Log.d(TAG, "getLocation: latitude: " + latitude);
                longitude = currentLocation.getLongitude();
                Log.d(TAG, "getLocation: longitude: " + longitude);
            }

        return new double[]{latitude,longitude};
    }

    private boolean requestLocationPermissionsIfNeeded(boolean byUserAction) {
        boolean isAccessGranted;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
            String coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION;
            isAccessGranted = getApplicationContext().checkSelfPermission(fineLocationPermission) == PackageManager.PERMISSION_GRANTED &&
                    getApplicationContext().checkSelfPermission(coarseLocationPermission) == PackageManager.PERMISSION_GRANTED;
            if (!isAccessGranted) { // The user blocked the location services of THIS app / not yet approved

                if (!didAlreadyRequestLocationPermission || byUserAction) {
                    didAlreadyRequestLocationPermission = true;
                    String[] permissionsToAsk = new String[]{fineLocationPermission, coarseLocationPermission};
                    // IllegalArgumentException: Can only use lower 16 bits for requestCode
                    ActivityCompat.requestPermissions(this, permissionsToAsk, LOCATION_PERMISSION_REQUEST_CODE);
                }
            }
        } else {
            // Because the user's permissions started only from Android M and on...
            isAccessGranted = true;
        }
        return isAccessGranted;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                break;
        }
    }


    public void paintAttack(GridLayout theGrid, Coordinate target, BattleField.shotState hitStatus, Player player) {
        if (hitStatus == BattleField.shotState.SUNK) {
            List<Coordinate> cords = player.getMyField().getSunkShipCords(target);
            if (cords != null) { // i received a list.
                for (Coordinate cord : cords) {
                    int img2draw = player.getMyField().getMyShipsLocation()[cord.getX()][cord.getY()].getImgExplosionResourceID();
                    GridButton btn2Paint = ((GridButton) theGrid.getChildAt(cord.getX() + cord.getY() * gridSize));
                    btn2Paint.setBackgroundResource(img2draw);
                }
            }
        } else if (hitStatus == BattleField.shotState.HIT) {
            if (player instanceof HumanPlayer)
                ((GridButton) theGrid.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(player.getMyField().getMyShipsLocation()[target.getX()][target.getY()].getImgExplosionResourceID());
            else
                ((GridButton) theGrid.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(R.drawable.blast);
        } else
            ((GridButton) theGrid.getChildAt(target.getX() + target.getY() * gridSize)).setBackgroundResource(R.drawable.miss);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //finish();
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit this game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        arrangeBattleFieldActivity.shouldDie = true;
                        GameActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
        return true;
    }


    private ServiceConnection boundService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: start");
            isServiceConnected = true;
            binder = (SensorService.MyBinder) service;
            notifyBoundService(SensorService.MyBinder.START_LISTENING);
            //.registerListener(this);
            ((SensorService.MyBinder) service).getService().registerListener(GameActivity.this);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnected = false;
            boundService = null; // gotta make sure it's a right line!!!
        }
    };

    void notifyBoundService(String massageFromActivity) {
        Log.d(TAG, "notifyBoundService: before if");
        if (isServiceConnected && binder != null) {
            Log.d(TAG, "notifyBoundService: inside if");
            binder.notifyService(massageFromActivity);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: activity destroyed");
        if (boundService != null)
            unbindService(boundService);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // gotta make sure it's a right code...
        unbindService(boundService);
        isServiceConnected = false;
        boundService = null;
    }

    @Override
    public void onUpdate(float[] value) {
        //Log.d(TAG, "onUpdate: inside");
        if (!isXYZinitialized) {
            isXYZinitialized = true;
            initialXYZ = new float[3];
            initialXYZ[0] = value[0];
            initialXYZ[1] = value[1];
            initialXYZ[2] = value[2];
            setMaxXYZ();
        }
        currXYZ[0] = value[0];
        currXYZ[1] = value[1];
        currXYZ[2] = value[2];
        checkIfMoved();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: before setting text");
                accelometerText.setText("[X,Y,Z]= [" + String.format("%.2f", currXYZ[0]) + "," + String.format("%.2f", currXYZ[1]) + "," + String.format("%.2f", currXYZ[2]) + "]");
            }
        });



    }

    public void checkIfMoved() {
        float curMax;
        int curIndex;

        if (currXYZ[0] > currXYZ[1] && currXYZ[0] > currXYZ[2]) {
            curMax = currXYZ[0];
            curIndex = 0;
        } else if (currXYZ[1] > currXYZ[2] && currXYZ[1] > currXYZ[0]) {
            curMax = currXYZ[1];
            curIndex = 1;
        } else {// [2] is max
            curMax = currXYZ[2];
            curIndex = 2;
        }
        Log.d(TAG, "checkIfMoved: curMax="+curMax+",Index="+curIndex+", Max="+max+" posOfMax="+posOfMax);
        if (curIndex!=posOfMax && curMax>max){
            Log.d(TAG, "checkIfMoved: screen moved ");
            isScreenMoved=true;
    }
 
       

        
    }
    public void setMaxXYZ(){

        Log.d(TAG, "setMaxXYZ: start");
        if (initialXYZ[0]>initialXYZ[1] && initialXYZ[0]>initialXYZ[2]){
            max=initialXYZ[0];
            posOfMax=0;
        }
        else if (initialXYZ[1]>initialXYZ[2] && initialXYZ[1]>initialXYZ[0]){
            max=initialXYZ[1];
            posOfMax=1;
        }
        else {
            max=initialXYZ[2];
            posOfMax=2;
        }
        Log.d(TAG, "setMaxXYZ: initial[x,y,z]=["+initialXYZ[0]+","+initialXYZ[1]+","+initialXYZ[2]+"]");
        Log.d(TAG, "setMaxXYZ: max="+max+" posOfMax="+posOfMax);
    }
}
