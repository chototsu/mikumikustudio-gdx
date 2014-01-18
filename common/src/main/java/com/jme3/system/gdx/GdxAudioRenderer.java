package com.jme3.system.gdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.jme3.audio.*;
import com.jme3.audio.android.AndroidAudioData;

import java.util.HashMap;

/**
 * Created by kobayasi on 2013/12/28.
 */
public class GdxAudioRenderer implements AudioRenderer{
    private boolean audioDisabled = false;
    private Listener listener;
    private final HashMap<AudioNode, Music> musicMap = new HashMap<AudioNode, Music>();
    @Override
    public void setListener(Listener listener) {
        Gdx.app.log("GdxAudioRenderer", "setListener");
        if (audioDisabled) {
            return;
        }

        if (this.listener != null) {
            // previous listener no longer associated with current
            // renderer
            this.listener.setRenderer(null);
        }

        this.listener = listener;
        this.listener.setRenderer(this);

    }
    @Override
    public void setEnvironment(Environment environment) {
        Gdx.app.log("GdxAudioRenderer", "setEnvironment");
    }

    @Override
    public void playSourceInstance(AudioNode audioNode) {
        Gdx.app.log("GdxAudioRenderer", "playSourceInstance");
        AndroidAudioData audioData;
        audioData = (AndroidAudioData) audioNode.getAudioData();
        Music music = musicMap.get(audioNode);
        if (Gdx.app.getType() == Application.ApplicationType.iOS && audioData.getAssetKey().getName().endsWith(".ogg")) {
            return;
        }
        if (music == null) {
            music = Gdx.audio.newMusic(GdxAssetCache.getFileHandle(audioData.getAssetKey().getName()));
            musicMap.put(audioNode, music);
        }
        music.stop();
        music.play();
        audioNode.setStatus(AudioNode.Status.Playing);
    }

    @Override
    public void playSource(AudioNode audioNode) {
        Gdx.app.log("GdxAudioRenderer", "playSource");
        if (audioNode.getStatus() == AudioNode.Status.Playing) {
            stopSource(audioNode);
            playSourceInstance(audioNode);
        } else if (audioNode.getStatus() == AudioNode.Status.Stopped) {
            playSourceInstance(audioNode);
        }
    }

    @Override
    public void pauseSource(AudioNode src) {
        Gdx.app.log("GdxAudioRenderer", "pauseSource");
        if (src.getStatus() == AudioNode.Status.Playing) {
            if (src.getAudioData() instanceof AndroidAudioData) {
                AndroidAudioData audioData = (AndroidAudioData) src.getAudioData();
                if (audioData.getAssetKey() instanceof AudioKey) {
                    AudioKey assetKey = (AudioKey) audioData.getAssetKey();

                    if (assetKey.isStream()) {
                        Music mp;
                        if (musicMap.containsKey(src)) {
                            mp = musicMap.get(src);
                            mp.pause();
                            src.setStatus(AudioNode.Status.Paused);
                        }
                    } else {
                        assert src.getChannel() != -1;

                        if (src.getChannel() > 0) {
//                            soundPool.pause(src.getChannel());
                            src.setStatus(AudioNode.Status.Paused);
                        }
                    }
                }
            }

        }
    }

    @Override
    public void stopSource(AudioNode src) {
        Gdx.app.log("GdxAudioRenderer", "stopSource");
        if (src.getStatus() != AudioNode.Status.Stopped) {
            if (src.getAudioData() instanceof AndroidAudioData) {
                AndroidAudioData audioData = (AndroidAudioData) src.getAudioData();
                if (audioData.getAssetKey() instanceof AudioKey) {
                    AudioKey assetKey = (AudioKey) audioData.getAssetKey();
                    if (assetKey.isStream()) {
                        Music mp;
                        if (musicMap.containsKey(src)) {
                            mp = musicMap.get(src);
                            mp.stop();
                            src.setStatus(AudioNode.Status.Stopped);
                            src.setChannel(-1);
                        }
                    } else {
                        int chan = src.getChannel();
                        assert chan != -1; // if it's not stopped, must have id

                        if (src.getChannel() > 0) {
//                            soundPool.stop(src.getChannel());
                            src.setChannel(-1);
                        }

                        src.setStatus(AudioNode.Status.Stopped);

                        if (audioData.getId() > 0) {
//                            soundPool.unload(audioData.getId());
                        }
                        audioData.setId(-1);



                    }
                }
            }

        }

    }

    @Override
    public void updateSourceParam(AudioNode audioNode, AudioParam audioParam) {
        Gdx.app.log("GdxAudioRenderer", "updateSourceParam");
    }

    @Override
    public void updateListenerParam(Listener listener, ListenerParam listenerParam) {
        Gdx.app.log("GdxAudioRenderer", "updateListenerParam");

    }

    @Override
    public void deleteFilter(Filter filter) {
        Gdx.app.log("GdxAudioRenderer", "deleteFilter");

    }

    @Override
    public void deleteAudioData(AudioData audioData) {
        Gdx.app.log("GdxAudioRenderer", "deleteAudioData");

    }

    @Override
    public void initialize() {
        Gdx.app.log("GdxAudioRenderer", "initialize");

    }

    @Override
    public void update(float v) {
//        Gdx.app.log("GdxAudioRenderer", "update");
        for(AudioNode src : musicMap.keySet()) {
            Music music = musicMap.get(src);
            if (src.getStatus() == AudioNode.Status.Playing) {
                if (!music.isPlaying()) {
                    Gdx.app.log("GdxAudioRenderer","music Stopped");
                    src.setStatus(AudioNode.Status.Stopped);
                } else {
                }
            }
        }

    }

    @Override
    public void cleanup() {
        Gdx.app.log("GdxAudioRenderer", "cleanup");
        for(Music music : musicMap.values()) {
            music.dispose();
        }
        musicMap.clear();
    }
}
