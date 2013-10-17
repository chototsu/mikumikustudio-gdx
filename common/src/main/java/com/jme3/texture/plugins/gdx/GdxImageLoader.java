package com.jme3.texture.plugins.gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.math.FastMath;
import com.jme3.texture.Image;
import com.jme3.util.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: kobayasi
 * Date: 13/10/10
 * Time: 23:54
 * To change this template use File | Settings | File Templates.
 */
public class GdxImageLoader implements AssetLoader{
    public GdxImageLoader() {
    }

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        InputStream is = assetInfo.openStream();
        byte[] buf = new byte[is.available()];
        System.err.println(assetInfo.getKey()+" buf length = "+buf.length);
        int i = 0;
        while ( i < buf.length) {
            int len = is.read(buf, i, buf.length - i);
            if (len < 0) {
                throw new IOException();
            }
            i += len;
        }
        is.close();
        Pixmap pixmap = new Pixmap(buf, 0, buf.length);
        Image.Format format;
        ByteBuffer bb  = BufferUtils.clone(pixmap.getPixels());
        int height = pixmap.getHeight();
        int width = pixmap.getWidth();
        switch(pixmap.getGLFormat()) {
            case GL20.GL_RGBA:
                format = Image.Format.RGBA8;
                ByteBuffer bb2 = pixmap.getPixels();
                for(int y = 0;y<height;y++) {
                    bb.position((y * width)*4);
                    bb2.position(((height - y - 1) * width)*4);
                    for(int i2=0;i2<width * 4;i2++) {
                        bb.put(bb2.get());
                    }
                }
                break;
            case GL20.GL_RGB:
                format = Image.Format.RGB8;
                bb2 = pixmap.getPixels();
                for(int y = 0;y<height;y++) {
                    bb.position((y * width)*3);
                    bb2.position(((height - y-1) * width)*3);
                    for(int i2=0;i2<width * 3;i2++) {
                        bb.put(bb2.get());
                    }
                }
                break;
            default:
                throw new IOException("Invalid format "+pixmap.getGLFormat());
        }
        if (pixmap.getWidth() != FastMath.nearestPowerOfTwo(pixmap.getWidth())
                || pixmap.getHeight() != FastMath.nearestPowerOfTwo(pixmap.getHeight())) {
            throw new IOException("size error "+pixmap.getWidth()+" "+pixmap.getHeight());
        }
        Image image = new Image(format, pixmap.getWidth(), pixmap.getHeight(), bb, null);
        return image;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
