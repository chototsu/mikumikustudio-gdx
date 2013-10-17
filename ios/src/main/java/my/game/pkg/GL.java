package my.game.pkg;

import org.robovm.rt.bro.*;
import org.robovm.rt.bro.annotation.*;

import java.nio.NioUtils;

@Library("OpenGLES")
public class GL {
    static {
        Bro.bind( GL.class);
    }

    public static final int GL_DEPTH_BUFFER_BIT = 0x00000100;
    public static final int GL_STENCIL_BUFFER_BIT = 0x00000400;
    public static final int GL_COLOR_BUFFER_BIT = 0x00004000;

    @Bridge
    public static native void glClearColor(float red,
                                           float green, float blue, float alpha);

    @Bridge
    public static native void glClear(int mask);
}
