package com.jme3.system;

import com.jme3.app.Application;
import com.jme3.asset.*;
import com.jme3.asset.gdx.GdxAssetManager;
import com.jme3.audio.*;
import com.jme3.system.gdx.GdxAudioRenderer;
import com.jme3.system.gdx.GdxContext;
import com.jme3.util.AndroidLogHandler;
//import com.jme3.audio.DummyAudioRenderer;
import com.jme3.system.JmeContext.Type;
import com.jme3.util.JmeFormatter;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.URL;

public class JmeSystem {

    private static final Logger logger = Logger.getLogger(JmeSystem.class.getName());
    private static boolean initialized = false;
    private static boolean lowPermissions = false;
    //private static Resources res;
    //private static Context activity;
    private static ThreadLocal<Application> app = new ThreadLocal<Application>();

    public static void initialize(AppSettings settings) {
        if (initialized) {
            return;
        }

        initialized = true;
        try {
            JmeFormatter formatter = new JmeFormatter();

            Handler consoleHandler = new AndroidLogHandler();
            consoleHandler.setFormatter(formatter);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, "Security error in creating log file", ex);
        }
        logger.log(Level.INFO, "Running on {0}", getFullName());
    }

    public static String getFullName() {
        return "MikuMikuStudio gdx";
    }

    public static void setLowPermissions(boolean lowPerm) {
        lowPermissions = lowPerm;
    }

    public static boolean isLowPermissions() {
        return lowPermissions;
    }

    public static JmeContext newContext(AppSettings settings, Type contextType) {
        initialize(settings);
        if (settings.getRenderer().startsWith("LiveWallpaper")) {
            
        }
        return new GdxContext();
    }

    // TODO
    public static AudioRenderer newAudioRenderer(AppSettings settings) {
        return new GdxAudioRenderer();
    }

//    public static void setResources(Resources res) {
//        JmeSystem.res = res;
//    }

//    public static Resources getResources() {
//        return res;
//    }

//    public static void setActivity(Context activity) {
//        JmeSystem.activity = activity;
//
//    }
    public static void setApplication(Application app) {
        JmeSystem.app.set(app);
    }
    public static Application getApplication() {
        return app.get();
    }

//    public static Context getActivity() {
//        return activity;
//    }

    public static AssetManager newAssetManager() {
        logger.log(Level.INFO, "newAssetManager()");
        AssetManager am = new GdxAssetManager();

        return am;
    }

    public static AssetManager newAssetManager(URL url) {
        logger.log(Level.INFO, "newAssetManager({0})", url);
        AssetManager am = new GdxAssetManager();

        return am;
    }

    public static boolean showSettingsDialog(AppSettings settings, boolean loadSettings) {
        return true;
    }

    public static Platform getPlatform() {
        String arch = System.getProperty("os.arch").toLowerCase();
        if (arch.contains("arm")){
            if (arch.contains("v5")){
                return Platform.Android_ARM5;
            }else if (arch.contains("v6")){
                return Platform.Android_ARM6;
            }else if (arch.contains("v7")){
                return Platform.Android_ARM7;
            }else{
                return Platform.Android_ARM5; // unknown ARM
            }
        }else{
            throw new UnsupportedOperationException("Unsupported Android Platform");
        }
    }

}
