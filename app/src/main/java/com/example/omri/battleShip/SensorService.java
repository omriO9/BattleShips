package com.example.omri.battleShip;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 31/12/2017.
 */
interface UpdateListener {
    void onUpdate(float[] value);
}
public class SensorService extends Service implements SensorEventListener {

    private static final String TAG = SensorService.class.getSimpleName();

    private SensorManager sensorManager;
    private boolean isListening = false;
    HandlerThread sensorThread;
    private Handler sensorHandler;
    private float[] values; // [x][y][z] of the accelerometer

    private MyBinder myBinder = new MyBinder();

    private final ArrayList<UpdateListener> mListeners
            = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: of sensorserivece");
        values= new float[3];
        sensorThread = new HandlerThread(SensorService.class.getSimpleName());
        sensorThread.start();
        sensorHandler = new Handler(sensorThread.getLooper());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    public MyBinder getBinder(){
        return myBinder;
    }

    public float[] getValues() {
        return values;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    //Toast.makeText(SensorService.this, "onSensorChanged", Toast.LENGTH_SHORT).show();

        /* check sensor type */
    if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
        values[0] = event.values[0];
        values[1] = event.values[1];
        values[2] = event.values[2];
        Log.d(TAG, "onSensorChanged: TYPE_ACCELEROMETER");
        }
        sendUpdate(values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerListener(UpdateListener listener) {
        mListeners.add(listener);
    }

    public void unregisterListener(UpdateListener listener) {
        mListeners.remove(listener);
    }

    private void sendUpdate(float[] values) {
        for (int i=mListeners.size()-1; i>=0; i--) {
            mListeners.get(i).onUpdate(values);
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        myBinder.sensorService=this;
        return myBinder;

    }
    @Override
    public boolean onUnbind(Intent intent) { // perry's code.
        Log.d(TAG, "onUnbind: im here");
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
        }
        stopSelf();
        return super.onUnbind(intent);
    }





    class MyBinder extends Binder { // this is how you bind a service to activity
        static final String START_LISTENING = "Start";
        private SensorService sensorService;

        public SensorService getService() {
            return sensorService;
        }

        void notifyService(String msg) {
            // A.D: "you must provide an interface that clients use to communicate with the service, by returning an IBinder."
            Log.d(TAG, "notifyService: starting");

            if (msg == MyBinder.START_LISTENING && !isListening) { // Why can we
                List<Sensor> sensorList= sensorManager.getSensorList(Sensor.TYPE_ALL);
                Log.d(TAG, "Available sensors: " + sensorList);
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // Sensor.TYPE_GYROSCOPE will be null in Genymotion free edition
                if (sensor == null && sensorList.size() > 0) {
                    // Backup
                    sensor = sensorList.get(0); // for Genymotion sensors (Genymotion Accelerometer in my case)
                }

                if (sensor == null) return;

                sensorManager.registerListener(getService(), sensor, SensorManager.SENSOR_DELAY_UI, sensorHandler);
                isListening = true;
            }
        }



    }
}
