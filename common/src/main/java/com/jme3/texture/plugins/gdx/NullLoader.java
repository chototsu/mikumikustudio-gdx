package com.jme3.texture.plugins.gdx;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.texture.Image;
import com.jme3.util.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: kobayasi
 * Date: 13/10/08
 * Time: 3:12
 * To change this template use File | Settings | File Templates.
 */
public class NullLoader implements AssetLoader {
    public NullLoader() {

    }
    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        if (true) {
            throw new RuntimeException();
        }
        Image image = new Image();
        image.setFormat(Image.Format.RGBA8);
        image.setWidth(2);
        image.setHeight(2);
        ByteBuffer bb = BufferUtils.createByteBuffer(4*4);
        for(int i=0;i<2*2*4;i++) {
            bb.put((byte)0xff);
        }
        bb.position(0);
        image.setData(bb);
        return image;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
