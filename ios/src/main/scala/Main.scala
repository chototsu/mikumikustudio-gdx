package my.game.pkg

import org.robovm.cocoatouch.foundation.{NSObject, NSDictionary, NSAutoreleasePool}
import org.robovm.cocoatouch.uikit.{UIButton, UIApplication}

import com.badlogic.gdx.backends.iosrobovm.IOSApplication
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import view.TestAppViewController

class Main extends IOSApplication.Delegate {
  var app : IOSApplication = null
  var testAppViewController : TestAppViewController = null
  override protected def createApplication(): IOSApplication = {
    System.err.println("Main::createApplication")

    val config = new IOSApplicationConfiguration()
    app = new IOSApplication(new MyGame(), config)
    app
  }


  override def didFinishLaunching(application: UIApplication, launchOptions: NSDictionary[_ <: NSObject, _ <: NSObject]): Boolean = {
    super.didFinishLaunching(application, launchOptions)
    System.err.println("Main::didFinishLaunching")
    testAppViewController = new TestAppViewControllerImpl
    app.getUIViewController.getView.addSubview(testAppViewController.getView)
    true
  }

}

object Main {
  def main(args: Array[String]) {
    System.err.println("Main::main")
      val pool = new NSAutoreleasePool()
      UIApplication.main(args, null, classOf[Main])
      pool.drain()
  }
}
class TestAppViewControllerImpl extends TestAppViewController {
  protected override def buttonAction(sender: UIButton): Unit = {
    label1.setText("Hello!!")
  }
}
