package org.sav.devhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    @SuppressWarnings("unused")
    public void onNo(View view) {
        JokeSoundsPlayer.INSTANCE.play(getApplicationContext(), R.raw.nooo);
    }

    @SuppressWarnings("unused")
    public void onLaughter(View view) {
        JokeSoundsPlayer.INSTANCE.play(getApplicationContext(), R.raw.laughter);
    }

    @SuppressWarnings("unused")
    public void onDevil(View view) {
        JokeSoundsPlayer.INSTANCE.play(getApplicationContext(), R.raw.devil);
    }

    @SuppressWarnings("unused")
    public void onEralash(View view) {
        JokeSoundsPlayer.INSTANCE.play(getApplicationContext(), R.raw.eralash);
    }

    @SuppressWarnings("unused")
    public void onStop(View view) {
        JokeSoundsPlayer.INSTANCE.stopPlayer();
    }

    @SuppressWarnings("unused")
    public void onWhip(View view) {
        JokeSoundsPlayer.INSTANCE.play(getApplicationContext(), R.raw.whip);
//        isWhip = ((ToggleButton) view).isChecked();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if (isWhip && (!JokeSoundsPlayer.INSTANCE.isPlaying())) {
//            float x = event.values[0];
//            float y = event.values[1];
//            float z = event.values[2];
//            double mod = Math.sqrt((x * x) + (y * y) + (z * z));
//            if (mod > 15) {
//                JokeSoundsPlayer.INSTANCE.play(getApplicationContext(), R.raw.whip);
//            }
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onStartService(View view) {
        Intent intent = new Intent(this, SoundService.class);
        startService(intent);
    }

    public void onStopService(View view) {
        Intent intent = new Intent(this, SoundService.class);
        stopService(intent);
    }
}
