package my.game.pkg

import android.os.Bundle
import com.badlogic.gdx.backends.android._
import info.projectkyoto.mms.bullet.BulletUtil

class Main extends AndroidApplication {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    BulletUtil.init()
    val config = new AndroidApplicationConfiguration()
    config.useAccelerometer = false
    config.useCompass = false
    config.useWakelock = true
    config.hideStatusBar = true
    config.useGL20 = true
    initialize(new MyGame(), config)
  }
}
