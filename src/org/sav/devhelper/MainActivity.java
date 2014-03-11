package org.sav.devhelper;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements SensorEventListener {

    private MediaPlayer player;
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean isWhip;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        isWhip = false;
    }

    private void play(int res) {
        stopPlayer();
        player = MediaPlayer.create(getApplicationContext(), res);
        player.start();
    }

    @SuppressWarnings("unused")
    public void onNo(View view) {
        play(R.raw.nooo);
    }

    @SuppressWarnings("unused")
    public void onLaughter(View view) {
        play(R.raw.laughter);
    }

    @SuppressWarnings("unused")
    public void onDevil(View view) {
        play(R.raw.devil);
    }

    @SuppressWarnings("unused")
    public void onEralash(View view) {
        play(R.raw.eralash);
    }

    @SuppressWarnings("unused")
    public void onStop(View view) {
        stopPlayer();
    }

    public void onWhip(View view) {
        isWhip = ((ToggleButton) view).isChecked();
    }

    private void stopPlayer() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isWhip && (player == null || !player.isPlaying())) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double mod = Math.sqrt((x * x) + (y * y) + (z * z));
            if (mod > 15) {
                play(R.raw.whip);
            }
        }
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
}
