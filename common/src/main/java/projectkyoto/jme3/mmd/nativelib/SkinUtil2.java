/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectkyoto.jme3.mmd.nativelib;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author kobayasi
 */
public class SkinUtil2 {
    public static void copy(Buffer src, Buffer dist, int size) {

    }
    public static void setSkin(FloatBuffer buf, ShortBuffer indexBuf, FloatBuffer skinBuf, float weight) {

    }
    public static void copyBoneMatrix(FloatBuffer src, FloatBuffer dist, ShortBuffer indexBuffer){
        dist.position(0);
        for(int i=0;i<indexBuffer.limit();i++) {
            int index = indexBuffer.get(i);
            src.position(index * 16);
            for(int j=0;j<16;j++) {
                dist.put(src.get());
            }
        }
    }
    public static void clear(Buffer buf) {

    }
}
