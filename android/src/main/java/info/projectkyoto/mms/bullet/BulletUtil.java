/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.projectkyoto.mms.bullet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kobayasi
 */
public class BulletUtil {
    private static final Logger logger = Logger.getLogger(BulletUtil.class.getName());
    static {
        try {
            if (hasNeon()) {
                System.loadLibrary("bulletjmeneon");
            } else {
                System.loadLibrary("bulletjme");
            }
        }catch(Exception ex) {
            logger.log(Level.SEVERE, "init failed.", ex);
        }
        
    }
    static boolean hasNeon() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/cpuinfo"), "utf-8"));
        try {
            for(;;) {
                String s = r.readLine();
                if (s == null) {
                    break;
                }
                if (s.indexOf("neon") >=0) {
                    logger.info("hasNeon = true");
                    return true;
                }
            }
        } finally {
            r.close();
        }
        logger.info("hasNeon = false");
        return false;
    }
    public static void init() {
        
    }
}
