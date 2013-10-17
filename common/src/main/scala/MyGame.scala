package my.game.pkg

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}
import com.badlogic.gdx.graphics.{FPSLogger, OrthographicCamera}
import com.jme3.system.{AppSettings}
import com.badlogic.gdx.utils.SharedLibraryLoader
import com.jme3.system.gdx.GdxContext

class MyGame extends Game {
  var batch : SpriteBatch = null
  var font : BitmapFont = null
  var camera : OrthographicCamera = null
  var app : TestApp = null
  var context : GdxContext = null

  val handler = new java.util.logging.ConsoleHandler
  var settings : AppSettings = null

  val fpsLogger = new FPSLogger

  override def create() {
    val loader = new SharedLibraryLoader().load("gdx-bullet")

    batch = new SpriteBatch()
    font = new BitmapFont()
    camera = new OrthographicCamera()
    camera.setToOrtho(false, 400, 400)
    settings = new AppSettings(true)
    settings.setWidth(480)
    settings.setHeight(480)

    settings.putString("AssetConfigURL", null)
    app = new TestApp
  }

  override def render() = {
    context.onDrawFrame()
    fpsLogger.log()
  }

  override def resize(width: Int, height: Int) = {
    println("width = "+width+" height = "+height)
    super.resize(width, height)
    settings.setWidth(width)
    settings.setHeight(height)
    app.start()
    context = app.getContext.asInstanceOf[GdxContext]
    context.setSettings(settings)
  }

  override def pause() = {
  }

  override def resume() = {
  }

}
