package my.game.pkg;

import com.badlogic.gdx.Gdx;
import org.robovm.cocoatouch.uikit.UIButton;
import org.robovm.cocoatouch.uikit.UILabel;
import org.robovm.cocoatouch.uikit.UIViewController;
import org.robovm.objc.Selector;
import org.robovm.objc.annotation.BindSelector;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.TypeEncoding;
import org.robovm.rt.bro.annotation.Callback;

/**
 * Created by kobayasi on 2014/02/22.
 */
@CustomClass("TestAppViewController")
public class TestAppViewController extends UIViewController {
    UILabel label1;
    public TestAppViewController() {
        super("TestAppViewController", null);
        Gdx.app.debug("TestAppViewController", "TestAppViewController()");
        System.err.println("TestAppViewController::TestAppViewController");
    }
    @SuppressWarnings("unused")
    @Callback
    @BindSelector("setLabel1:")
    @TypeEncoding("v@:@")
    private static void setLabel1(TestAppViewController self, Selector sel, UILabel label) {
        self.label1 = label;
    }
    @SuppressWarnings("unused")
    @Callback
    @BindSelector("label1")
    private static UILabel getLabel1(TestAppViewController self, Selector sel) {
        return self.label1;
    }
    @SuppressWarnings("unused")
    @Callback
    @BindSelector("buttonAction:")
    private static void buttonAction(TestAppViewController self, Selector sel, UIButton button) {
        self.buttonAction();
    }

    private void buttonAction() {
        label1.setText("Hello!!");
    }
}
