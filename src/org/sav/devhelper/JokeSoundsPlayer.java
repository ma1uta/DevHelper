package org.sav.devhelper;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * @author tolya
 * @since 15.03.14.
 *
 * Media player.
 */
public enum JokeSoundsPlayer {

    INSTANCE;

    private MediaPlayer player;

    public void play(Context context, int resource) {
        stopPlayer();
        player = MediaPlayer.create(context, resource);
        player.start();
    }

    public void stopPlayer() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }
}
