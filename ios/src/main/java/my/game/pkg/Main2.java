package my.game.pkg;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.robovm.cocoatouch.coregraphics.*;
import org.robovm.cocoatouch.foundation.*;
import org.robovm.cocoatouch.glkit.*;
import org.robovm.cocoatouch.opengles.*;
import org.robovm.cocoatouch.uikit.*;

public class Main2 extends UIApplicationDelegate.Adapter implements GLKViewDelegate {

    private UIWindow window = null;
    private boolean increasing = false;
    private float curRed = 0.0f;
    private GLKViewControllerDelegate vcd;

    @Override
    public boolean didFinishLaunching(UIApplication application,
                                      NSDictionary launchOptions) {

        window = new UIWindow(UIScreen.getMainScreen().getBounds());
        EAGLContext context = new EAGLContext(EAGLRenderingAPI.OpenGLES2);
        GLKView view = new GLKView(UIScreen.getMainScreen().getBounds(), context);
        view.setDelegate(this);

        GLKViewController viewController = new GLKViewController();
        viewController.setView(view);

        vcd = new GLKViewControllerDelegate.Adapter() {
            @Override
            public void update(GLKViewController controller) {
                Main2.this.update(controller);
            }
        };

        viewController.setDelegate(vcd);
        viewController.setPreferredFramesPerSecond(60);

        window.setRootViewController(viewController);
        window.setBackgroundColor(UIColor.whiteColor());
        window.makeKeyAndVisible();

        return true;
    }

    public void update(GLKViewController controller) {
        if (increasing) {
            curRed += 1.0f * controller.getTimeSinceLastUpdate();
        } else {
            curRed -= 1.0f * controller.getTimeSinceLastUpdate();
        }
        if (curRed >= 1.0f) {
            curRed = 1.0f;
            increasing = false;
        }
        if (curRed <= 0.0f) {
            curRed = 0.0f;
            increasing = true;
        }
        System.err.println("update "+controller.getTimeSinceLastUpdate());
    }

    @Override
    public void draw(GLKView view, CGRect rect) {
       GL.glClearColor(curRed, 0.0f, 0.0f, 1.0f);
       GL.glClear(GL.GL_COLOR_BUFFER_BIT);

    }

    public static void main2(String[] args) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(args, null, Main.class);
        pool.drain();
    }


}
