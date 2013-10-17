package my.game.pkg;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import projectkyoto.jme3.mmd.CartoonEdgeProcessor;
import projectkyoto.jme3.mmd.PMDNode;
import projectkyoto.jme3.mmd.UpdateControl;
import projectkyoto.jme3.mmd.vmd.VMDControl;
import projectkyoto.mmd.file.VMDFile;

/**
 * test
 * @author normenhansen
 */
public class TestApp extends SimpleApplication {
    PMDNode model;
    VMDControl vmdControl;

    public TestApp() {
        System.err.println("new TestApp()");
    }

    @Override
    public void simpleInitApp() {
        System.err.println("simpleInitApp()");

        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        viewPort.setBackgroundColor(ColorRGBA.Blue);

        Quaternion q = new Quaternion();
        q.fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);
        cam.setAxes(q);
        cam.update();
        Vector3f v = cam.getDirection();
        System.out.println("direction = " + v.toString());
        setDisplayFps(true);
        setDisplayStatView(true);

        model = (PMDNode)assetManager.loadModel("/Model/sora/sora_act2.5.pmd");
        model.move(0, -10, -20);
        model.move(1, 0, 0);

        vmdControl = new VMDControl(model, (VMDFile)assetManager.loadAsset("motion/koshihuri.vmd"));
        model.addControl(vmdControl);
        vmdControl.setPause(false);
        model.addControl(new UpdateControl(model));

        rootNode.attachChild(model);

        //model.rotate(0,FastMath.PI * 0.7f,0);
        //ライトの設定
        DirectionalLight dl = new DirectionalLight();//方向ライト
        dl.setDirection(new Vector3f(1, 0, -5).normalizeLocal());
        dl.setColor(ColorRGBA.White.mult(0.8f));
        rootNode.addLight(dl);
        AmbientLight al = new AmbientLight();//アンビエントライト
        al.setColor(ColorRGBA.White.mult(0.5f));
        rootNode.addLight(al);

        viewPort.addProcessor(new CartoonEdgeProcessor());
    }
}
