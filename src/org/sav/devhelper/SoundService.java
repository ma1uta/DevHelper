package org.sav.devhelper;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author tolya
 * @since 15.03.14.
 */
public class SoundService extends Service implements SensorEventListener {

    public final int NOTIFICATION_ID = 1;
    private boolean isRunning = false;
    private SensorManager sensorManager;
    private volatile float[] previousVector = new float[3];
    private volatile float[] currentVector = new float[3];
    private volatile float[] gravityVector = new float[3];
    private volatile boolean timerRunning;
    private volatile boolean skip;
    private static final String TAG = "DEV_HELPER";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isRunning) {
            Toast.makeText(this, "Service already started", Toast.LENGTH_SHORT).show();
        } else {
            isRunning = true;
        }
        skip = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        flushVectors();
        Notification.Builder notificationBuilder =
                new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle("title")
                        .setContentText("text").setAutoCancel(false);
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);
        Notification notification = notificationBuilder.build();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);

        startForeground(NOTIFICATION_ID, notification);
        super.onCreate();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isRunning) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_GRAVITY:
                    System.arraycopy(event.values, 0, gravityVector, 0, 3);
//                    Log.d(TAG, "" + mod(gravityVector));
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    if (!(JokeSoundsPlayer.INSTANCE.isPlaying() || emptyVector(event.values))) {
                        if (emptyVector(previousVector)) {
                            if (skip) {
                                skip = false;
                            } else {
                                System.arraycopy(event.values, 0, previousVector, 0, 3);
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "prev: " + cos(gravityVector, previousVector));
                                        Log.d(TAG, "curr: " + cos(gravityVector, currentVector));
                                        Log.d(TAG, "merge: " + cos(previousVector, currentVector));
                                        flushVectors();
                                        skip = true;
                                        timerRunning = false;
                                    }
                                }, 500);
                            }
                        } else {
                            System.arraycopy(event.values, 0, currentVector, 0, 3);
                        }
//                        System.arraycopy(currentVector, 0, previousVector, 0, 3);
//                        System.arraycopy(event.values, 0, currentVector, 0, 3);
//                        Log.d(TAG, "" + degree(previousVector, currentVector));
//                        Log.d(TAG, "" + cos(previousVector, currentVector));
                        break;
                    }
/*
                    if (timerRunning) {
                        System.arraycopy(event.values, 0, currentVector, 0, 3);
                        timerRunning = false;
                    } else {
                        timerRunning = true;
                        System.arraycopy(event.values, 0, previousVector, 0, 3);
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Log.d(TAG, "prev: " + cos(gravityVector, previousVector) + " curr: " +
                                           cos(gravityVector, currentVector) + " merge: " +
                                           cos(previousVector, currentVector));
                                if (cos(gravityVector, previousVector) < -0.8f &&
                                    cos(gravityVector, currentVector) < -0.8f &&
                                    cos(previousVector, currentVector) > 0.8f) {
                                    JokeSoundsPlayer.INSTANCE.play(getApplicationContext(), R.raw.nooo);
                                } else if (cos(gravityVector, previousVector) > 0.8f &&
                                           cos(gravityVector, currentVector) > 0.8f &&
                                           cos(previousVector, currentVector) > 0.8f) {
                                    JokeSoundsPlayer.INSTANCE.play(getApplicationContext(), R.raw.laughter);
                                }
                                flushVectors();
                                timerRunning = false;
                            }
                        }, 1000);

                    }
*/

            }
        }
    }

    private double mod(float[] vector) {
        return Math.sqrt((vector[0] * vector[0]) + (vector[1] * vector[1]) + (vector[2] * vector[2]));
    }

    private double cos(float[] v1, float[] v2) {
        double scalar = (v1[0] * v2[0]) + (v1[1] * v2[1]) + (v1[2] * v2[2]);
        return scalar / (mod(v1) * mod(v2));
    }

    private double degree(float[] v1, float[] v2) {
        return Math.toDegrees(Math.acos(cos(v1, v2)));
    }

    private boolean emptyVector(float[] vector) {
        return mod(vector) < 15.0f;
    }

    private void flushVectors() {
        Log.d(TAG, "flush vectors");
        Arrays.fill(currentVector, 0f);
        Arrays.fill(previousVector, 0f);
        Arrays.fill(gravityVector, 0f);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }
}
