
package com.CalistaAqilaRamadhani.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

public final class AudioSystem {

    private static final AudioSystem instance = new AudioSystem();

    private Music bgm;
    private Sound shootSound;
    private Sound deathSound;
    private Sound collectSound;

    private boolean initialized = false;

    private AudioSystem() {
    }

    public static AudioSystem getInstance() {
        return instance;
    }

    private FileHandle getAudioFile(String fileName) {

        String[] paths = {
            "audio/" + fileName,
            "assets/audio/" + fileName,
            "../assets/audio/" + fileName
        };

        for (String path : paths) {
            FileHandle file = Gdx.files.internal(path);

            if (file.exists()) {
                System.out.println(
                    "Audio found: " + file.file().getAbsolutePath()
                );
                return file;
            }
        }

        throw new GdxRuntimeException(
            "Audio file not found: " + fileName +
                "\nWorking Directory: " +
                System.getProperty("user.dir") +
                "\nCheck your assets/audio folder."
        );
    }

    public void init() {
        if (initialized) {
            return;
        }

        System.out.println("Initializing Audio System...");
        System.out.println(
            "Working Directory: " + System.getProperty("user.dir")
        );

        FileHandle bgmFile = getAudioFile("touhou_bgm.ogg");
        FileHandle shootFile = getAudioFile("shoot.wav");
        FileHandle deathFile = getAudioFile("enemy_death.wav");
        FileHandle collectFile = getAudioFile("item_collect.wav");

        bgm = Gdx.audio.newMusic(bgmFile);
        shootSound = Gdx.audio.newSound(shootFile);
        deathSound = Gdx.audio.newSound(deathFile);
        collectSound = Gdx.audio.newSound(collectFile);

        bgm.setLooping(true);
        bgm.setVolume(0.4f);

        initialized = true;

        System.out.println("Audio System initialized successfully!");
    }

    public void playBgm() {
        if (initialized && !bgm.isPlaying()) {
            bgm.play();
        }
    }

    public void stopBgm() {
        if (initialized && bgm.isPlaying()) {
            bgm.stop();
        }
    }

    public void pauseBgm() {
        if (initialized && bgm.isPlaying()) {
            bgm.pause();
        }
    }

    public void playShoot() {
        if (initialized) {
            shootSound.play(0.5f);
        }
    }

    public void playEnemyDeath() {
        if (initialized) {
            deathSound.play(0.7f);
        }
    }

    public void playItemCollect() {
        if (initialized) {
            collectSound.play(0.7f);
        }
    }

    public void setBgmVolume(float volume) {
        if (initialized) {
            bgm.setVolume(Math.max(0f, Math.min(1f, volume)));
        }
    }

    public void dispose() {
        if (bgm != null) {
            bgm.dispose();
        }

        if (shootSound != null) {
            shootSound.dispose();
        }

        if (deathSound != null) {
            deathSound.dispose();
        }

        if (collectSound != null) {
            collectSound.dispose();
        }

        bgm = null;
        shootSound = null;
        deathSound = null;
        collectSound = null;

        initialized = false;

        System.out.println("Audio System disposed.");
    }
}
