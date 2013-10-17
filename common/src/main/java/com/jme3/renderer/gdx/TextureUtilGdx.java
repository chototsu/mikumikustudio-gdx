package com.jme3.renderer.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.jme3.app.AndroidHarness;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.math.FastMath;
import com.jme3.system.JmeSystem;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class TextureUtilGdx {
    public static boolean ENABLE_COMPRESSION = true;
    private static boolean NPOT = false;
    private static boolean ETC1support = false;
    private static boolean DXT1 = false;
    private static boolean DEPTH24_STENCIL8 = false;
    private static boolean DEPTH_TEXTURE = true;
    private static boolean RGBA8 = false;
    
    // Same constant used by both GL_ARM_rgba8 and GL_OES_rgb8_rgba8.
    private static final int GL_RGBA8 = 0x8058;
    
    private static final int GL_DXT1 = 0x83F0;
    private static final int GL_DXT1A = 0x83F1;
    
    private static final int GL_DEPTH_STENCIL_OES = 0x84F9;
    private static final int GL_UNSIGNED_INT_24_8_OES = 0x84FA;
    private static final int GL_DEPTH24_STENCIL8_OES = 0x88F0;

    public static int convertTextureFormat(Format fmt){
        switch (fmt){
            case Alpha16:
            case Alpha8:
                return GL10.GL_ALPHA;
            case Luminance8Alpha8:
            case Luminance16Alpha16:
                return GL10.GL_LUMINANCE_ALPHA;
            case Luminance8:
            case Luminance16:
                return GL10.GL_LUMINANCE;
            case RGB10:
            case RGB16:
            case BGR8:
            case RGB8:
            case RGB565:
                return GL10.GL_RGB;
            case RGB5A1:
            case RGBA16:
            case RGBA8:
                return GL10.GL_RGBA;
                
            case Depth:
                return Gdx.gl20.GL_DEPTH_COMPONENT;
            case Depth16:
                return Gdx.gl20.GL_DEPTH_COMPONENT16;
            case Depth24:
            case Depth32:
            case Depth32F:
                throw new UnsupportedOperationException("Unsupported depth format: " + fmt);   
                
            case DXT1A:
                throw new UnsupportedOperationException("Unsupported format: " + fmt);
            default:
                throw new UnsupportedOperationException("Unrecognized format: " + fmt);
        }
    }
   // TODO
//    private static void buildMipmap(Bitmap bitmap) {
//        int level = 0;
//        int height = bitmap.getHeight();
//        int width = bitmap.getWidth();
//
//        while (height >= 1 || width >= 1) {
//            //First of all, generate the texture from our bitmap and set it to the according level
//
//            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
////checkGLError();
//            if (height == 1 || width == 1) {
//                break;
//            }
//
//            //Increase the mipmap level
//            level++;
//
//            height /= 2;
//            width /= 2;
//            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
//
//            bitmap.recycle();
//            bitmap = bitmap2;
//        }
//    }bitmap2
//    // TODO
//    /**
//     * <code>uploadTextureBitmap</code> uploads a native android bitmap
//     * @param target
//     * @param bitmap
//     * @param generateMips
//     * @param powerOf2
//     */
//    public static void uploadTextureBitmap(final int target, Bitmap bitmap, boolean generateMips, boolean powerOf2) {
//        int MAX_RETRY_COUNT = 1;
//        for(int retryCount = 0;retryCount<MAX_RETRY_COUNT;retryCount++) {
//            try {
//                uploadTextureBitmap2(target, bitmap, generateMips, powerOf2);
//            }catch(OutOfMemoryError ex) {
//                if (!(retryCount < MAX_RETRY_COUNT)){
//                    throw ex;
//                }
//                DesktopAssetManager assetManager =
//                        (DesktopAssetManager)((AndroidHarness)JmeSystem.getActivity())
//                        .getJmeApplication().getAssetManager();
//                assetManager.clearCache();
//                System.gc();
//                System.runFinalization();
//            }
//        }
//    }
//    /**
//     * <code>uploadTextureBitmap</code> uploads a native android bitmap
//     * @param target
//     * @param bitmap
//     * @param generateMips
//     * @param powerOf2
//     */
//    private static void uploadTextureBitmap2(final int target, Bitmap bitmap, boolean generateMips, boolean powerOf2)
//    {
//        if (bitmap.isRecycled()) {
//            throw new RuntimeException("bitmap is recycled.");
//        }
//        if (!powerOf2)
//        {
//            int width = bitmap.getWidth();
//            int height = bitmap.getHeight();
//            if (!FastMath.isPowerOfTwo(width) || !FastMath.isPowerOfTwo(height)
//                    /*|| width >= 512 || height >= 512*/)
//            {
//                // scale to power of two
//                width = FastMath.nearestPowerOfTwo(width);
//                height = FastMath.nearestPowerOfTwo(height);
////                while(width >= 512) {
////                    width = width / 2;
////                }
////                while(height >= 512) {
////                    height = height / 2;
////                }
//            Logger.getLogger(TextureUtil.class.getName()).warning("texture size changed.");
//                Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
//                bitmap.recycle();
//                bitmap = bitmap2;
//            }
//        }
//
//        if (generateMips)
//        {
//            buildMipmap(bitmap);
//        }
//        else
//        {
//            // TODO
//            GLUtils.texImage2D(target, 0, bitmap, 0);
////checkGLError();
////bitmap.recycle();
//        }
//    }
    private static void unsupportedFormat(Format fmt) {
        throw new UnsupportedOperationException("The image format '" + fmt + "' is unsupported by the video hardware.");
    }

    public static AndroidGLImageFormat getImageFormat(Format fmt) throws UnsupportedOperationException {
        AndroidGLImageFormat imageFormat = new AndroidGLImageFormat();
        switch (fmt) {
            case RGBA16:
            case RGB16:
            case RGB10:
            case Luminance16:
            case Luminance16Alpha16:
            case Alpha16:
            case Depth32:
            case Depth32F:
                throw new UnsupportedOperationException("The image format '"
                        + fmt + "' is not supported by OpenGL ES 2.0 specification.");
            case Alpha8:
                imageFormat.format = GL20.GL_ALPHA;
                imageFormat.dataType = GL20.GL_UNSIGNED_BYTE;
                if (RGBA8) {
                    imageFormat.renderBufferStorageFormat = GL_RGBA8;
                } else {
                    // Highest precision alpha supported by vanilla OGLES2
                    imageFormat.renderBufferStorageFormat = GL20.GL_RGBA4;
                }
                break;
            case Luminance8:
                imageFormat.format = GL20.GL_LUMINANCE;
                imageFormat.dataType = GL20.GL_UNSIGNED_BYTE;
                if (RGBA8) {
                    imageFormat.renderBufferStorageFormat = GL_RGBA8;
                } else {
                    // Highest precision luminance supported by vanilla OGLES2
                    imageFormat.renderBufferStorageFormat = GL20.GL_RGB565;
                }
                break;
            case Luminance8Alpha8:
                imageFormat.format = GL20.GL_LUMINANCE_ALPHA;
                imageFormat.dataType = GL20.GL_UNSIGNED_BYTE;
                if (RGBA8) {
                    imageFormat.renderBufferStorageFormat = GL_RGBA8;
                } else {
                    imageFormat.renderBufferStorageFormat = GL20.GL_RGBA4;
                }
                break;
            case RGB565:
                imageFormat.format = GL20.GL_RGB;
                imageFormat.dataType = GL20.GL_UNSIGNED_SHORT_5_6_5;
                imageFormat.renderBufferStorageFormat = GL20.GL_RGB565;
                break;
            case ARGB4444:
                imageFormat.format = GL20.GL_RGBA4;
                imageFormat.dataType = GL20.GL_UNSIGNED_SHORT_4_4_4_4;
                imageFormat.renderBufferStorageFormat = GL20.GL_RGBA4;
                break;
            case RGB5A1:
                imageFormat.format = GL20.GL_RGBA;
                imageFormat.dataType = GL20.GL_UNSIGNED_SHORT_5_5_5_1;
                imageFormat.renderBufferStorageFormat = GL20.GL_RGB5_A1;
                break;
            case RGB8:
                imageFormat.format = GL20.GL_RGB;
                imageFormat.dataType = GL20.GL_UNSIGNED_BYTE;
                if (RGBA8) {
                    imageFormat.renderBufferStorageFormat = GL_RGBA8;
                } else {
                    // Fallback: Use RGB565 if RGBA8 is not available.
                    imageFormat.renderBufferStorageFormat = GL20.GL_RGB565;
                }
                break;
            case BGR8:
                imageFormat.format = GL20.GL_RGB;
                imageFormat.dataType = GL20.GL_UNSIGNED_BYTE;
                if (RGBA8) {
                    imageFormat.renderBufferStorageFormat = GL_RGBA8;
                } else {
                    imageFormat.renderBufferStorageFormat = GL20.GL_RGB565;
                }
                break;
            case RGBA8:
                imageFormat.format = GL20.GL_RGBA;
                imageFormat.dataType = GL20.GL_UNSIGNED_BYTE;
                if (RGBA8) {
                    imageFormat.renderBufferStorageFormat = GL_RGBA8;
                } else {
                    imageFormat.renderBufferStorageFormat = GL20.GL_RGBA4;
                }
                break;
            case Depth:
            case Depth16:
                if (!DEPTH_TEXTURE) {
                    unsupportedFormat(fmt);
                }
                imageFormat.format = GL20.GL_DEPTH_COMPONENT;
                imageFormat.dataType = GL20.GL_UNSIGNED_SHORT;
                imageFormat.renderBufferStorageFormat = GL20.GL_DEPTH_COMPONENT16;
                break;
            case Depth24:
//            case Depth24Stencil8:
//                if (!DEPTH_TEXTURE) {
//                    unsupportedFormat(fmt);
//                }
//                if (DEPTH24_STENCIL8) {
//                    // NEW: True Depth24 + Stencil8 format.
//                    imageFormat.format = GL_DEPTH_STENCIL_OES;
//                    imageFormat.dataType = GL_UNSIGNED_INT_24_8_OES;
//                    imageFormat.renderBufferStorageFormat = GL_DEPTH24_STENCIL8_OES;
//                } else {
//                    // Vanilla OGLES2, only Depth16 available.
//                    imageFormat.format = GLES20.GL_DEPTH_COMPONENT;
//                    imageFormat.dataType = GLES20.GL_UNSIGNED_SHORT;
//                    imageFormat.renderBufferStorageFormat = GLES20.GL_DEPTH_COMPONENT16;
//                }
//                break;
            case DXT1:
                if (!DXT1) {
                    unsupportedFormat(fmt);
                }
                imageFormat.format = GL_DXT1;
                imageFormat.dataType = GL20.GL_UNSIGNED_BYTE;
                imageFormat.compress = true;
                break;
            case DXT1A:
                if (!DXT1) {
                    unsupportedFormat(fmt);
                }
                imageFormat.format = GL_DXT1A;
                imageFormat.dataType = GL20.GL_UNSIGNED_BYTE;
                imageFormat.compress = true;
                break;
            default:
                throw new UnsupportedOperationException("Unrecognized format: " + fmt);
        }
        return imageFormat;
    }
    public static class AndroidGLImageFormat {

        boolean compress = false;
        int format = -1;
        int renderBufferStorageFormat = -1;
        int dataType = -1;
    }
    public static void uploadTexture(
                                     Image img,
                                     int target,
                                     int index,
                                     int border,
                                     boolean tdc,
                                     boolean generateMips,
                                     boolean powerOf2){
        // TODO
//        if (img.getEfficentData() instanceof Bitmap){
//            Bitmap bitmap = (Bitmap) img.getEfficentData();
//            uploadTextureBitmap(target, bitmap, generateMips, powerOf2);
//            return;
//        }

        Image.Format fmt = img.getFormat();
        ByteBuffer data;
        if (index >= 0 || img.getData() != null && img.getData().size() > 0){
            data = img.getData(index);
        }else{
            data = null;
        }

        int width = img.getWidth();
        int height = img.getHeight();
        int depth = img.getDepth();

        boolean compress = false;
        int internalFormat = -1;
        int format = -1;
        int dataType = -1;

        switch (fmt){
            case Alpha16:
            case Alpha8:
                format = GL20.GL_ALPHA;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case Luminance8:
                format = GL20.GL_LUMINANCE;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case Luminance8Alpha8:
                format = GL20.GL_LUMINANCE_ALPHA;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case Luminance16Alpha16:
                format = GL20.GL_LUMINANCE_ALPHA;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case Luminance16:
                format = GL20.GL_LUMINANCE;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case RGB565:
                format = GL20.GL_RGB;
                internalFormat = GL20.GL_RGB565;
                dataType = GL20.GL_UNSIGNED_SHORT_5_6_5;
                break;
            case ARGB4444:
                format = GL20.GL_RGBA;
                dataType = GL20.GL_UNSIGNED_SHORT_4_4_4_4;
                break;
            case RGB10:
                format = GL20.GL_RGB;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case RGB16:
                format = GL20.GL_RGB;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case RGB5A1:
                format = GL20.GL_RGBA;
                internalFormat = GL20.GL_RGB5_A1;
                dataType = GL20.GL_UNSIGNED_SHORT_5_5_5_1;
                break;
            case RGB8:
                format = GL20.GL_RGB;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case BGR8:
                format = GL20.GL_RGB;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case RGBA16:
                format = GL20.GL_RGBA;
                internalFormat = GL20.GL_RGBA4;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case RGBA8:
                format = GL20.GL_RGBA;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case DXT1A:
                format = GL20.GL_COMPRESSED_TEXTURE_FORMATS;
                dataType = GL20.GL_UNSIGNED_BYTE;
            case Depth:
                format = GL20.GL_DEPTH_COMPONENT;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case Depth16:
                format = GL20.GL_DEPTH_COMPONENT;
                internalFormat = GL20.GL_DEPTH_COMPONENT16;
                dataType = GL20.GL_UNSIGNED_BYTE;
                break;
            case Depth24:
            case Depth32:
            case Depth32F:
                throw new UnsupportedOperationException("Unsupported depth format: " + fmt);                
            default:
                throw new UnsupportedOperationException("Unrecognized format: " + fmt);
        }
        
        if (internalFormat == -1)
        {
            internalFormat = format;
        }

        if (data != null)
            Gdx.gl20.glPixelStorei(GL20.GL_UNPACK_ALIGNMENT, 1);

        int[] mipSizes = img.getMipMapSizes();
        int pos = 0;
        if (mipSizes == null){
            if (data != null)
                mipSizes = new int[]{ data.capacity() };
            else
                mipSizes = new int[]{ width * height * fmt.getBitsPerPixel() / 8 };
        }

        // XXX: might want to change that when support
        // of more than paletted compressions is added..
        if (compress){
            data.clear();
            Gdx.gl20.glCompressedTexImage2D(GL20.GL_TEXTURE_2D,
                                      1 - mipSizes.length,
                                      format,
                                      width,
                                      height,
                                      0,
                                      data.capacity(),
                                      data);
//checkGLError();
return;
        }

        for (int i = 0; i < mipSizes.length; i++){
            int mipWidth =  Math.max(1, width  >> i);
            int mipHeight = Math.max(1, height >> i);
            int mipDepth =  Math.max(1, depth  >> i);

            if (data != null){
                data.position(pos);
                data.limit(pos + mipSizes[i]);
            }

            if (compress && data != null){
                Gdx.gl20.glCompressedTexImage2D(GL20.GL_TEXTURE_2D,
                                          i,
                                          format,
                                          mipWidth,
                                          mipHeight,
                                          0,
                                          data.remaining(),
                                          data);
//checkGLError();
            }else{
                Gdx.gl20.glTexImage2D(GL20.GL_TEXTURE_2D,
                                i,
                                internalFormat,
                                mipWidth,
                                mipHeight,
                                0,
                                format,
                                dataType,
                                data);
            }
//checkGLError();
            pos += mipSizes[i];
        }
    }
//    private static void checkGLError() {
//        int error;
//        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
//            	throw new RuntimeException("glError " + error);
//        }
//    }
}
