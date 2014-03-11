package org.sav.devhelper;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import org.sav.sounds.R;

public class MainActivity extends Activity {

    private MediaPlayer player;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    private void play(int res) {
        stopPlayer();
        player = MediaPlayer.create(getApplicationContext(), res);
        player.start();
    }

    public void onNo(View view) {
        play(R.raw.nooo);
    }

    public void onLaughter(View view) {
        play(R.raw.laughter);
    }

    public void onDevil(View view) {
        play(R.raw.devil);
    }

    public void onEralash(View view) {
        play(R.raw.eralash);
    }

    public void onStop(View view) {
        stopPlayer();
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
}
