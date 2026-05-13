
package com.example.soulspire.Util;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;



public class SoundManager {

    private static MediaPlayer music;
    private static double volume = 0.1;
    private static boolean muted = false;
    private static final Map<String, AudioClip> cache = new HashMap<>();

    /**
     * Plays background music in a loop.
     * @param path
     */
    public void playMusic(String path) {
        stopMusic();

        URL url = getClass().getResource(path);
        if (url == null) {
            return;
        }

        Media media = new Media(url.toExternalForm());
        music = new MediaPlayer(media);
        music.setCycleCount(MediaPlayer.INDEFINITE);
        music.setVolume(volume);
        music.play();


    }

    /**
     * Plays one shot sound effect.
     * @param path
     */
    public static void playEffect(String path) {
        if (muted) {
            return;
        }
        AudioClip clip = cache.get(path);
        if (clip == null) {
            URL url = SoundManager.class.getResource(path);
            if (url == null) { System.err.println("Sound not found: " + path); return; }
            clip = new AudioClip(url.toExternalForm());
            cache.put(path, clip);
        }
        clip.play(volume);
    }

    /**
     * Stops music.
     */
    private void stopMusic() {
        if(music != null) {
            music.stop();
            music = null;
        }

    }

    /**
     * Mutes music in the game.
     */
    public static void toggleMute () {
        muted = !muted;
        if (music != null) {
            music.setVolume(muted ? 0 : volume);
        }

    }

    /**
     * Volume 0.0 - 0.1
     * @param volume
     */
    public void setVolume (double volume) {
        volume = Math.max(0, Math.min(1, volume));
        if (music != null && !muted) {
            music.setVolume(volume);
        }
    }

    public double getVolume() {
        return volume;
    }

    public static boolean isMuted() {
        return muted;
    }

}

