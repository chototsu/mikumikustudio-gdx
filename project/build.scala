import sbt._
import Keys._
import Defaults._

import sbtandroid.AndroidPlugin._
import sbtrobovm.RobovmPlugin._

import sbtassembly.Plugin._
import AssemblyKeys._

object Settings {
  lazy val scalameter = new TestFramework("org.scalameter.ScalaMeterFramework")

  lazy val common = Defaults.defaultSettings ++ Seq(
    version := "0.1",
    scalaVersion := "2.10.2",
    javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.6", "-target", "1.6"),
    scalacOptions ++= Seq("-Xlint", "-unchecked", "-deprecation", "-feature"),
    resolvers += Resolver.url("mmstestrepo",url("https://raw.github.com/chototsu/testrepo/master/ivy2/")) ( Patterns(false,"[organisation]/[module]/[revision]/[type]s/[artifact].[ext]") ),
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.10.1" % "test",
      "com.github.axel22" %% "scalameter" % "0.3" % "test",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test",
      "info.projectkyoto" % "mmstestdata" % "0.1-SNAPSHOT"
    ),
    parallelExecution in Test := false,
    testFrameworks in Test += scalameter,
    testOptions in Test ++= Seq(
      Tests.Argument(scalameter, "-preJDK7"),
      Tests.Argument(TestFrameworks.ScalaTest, "-o", "-u", "target/test-reports")
    ),
    unmanagedBase <<= baseDirectory(_/"libs"),
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx" % "0.9.9-SNAPSHOT"
    )
  )

  lazy val desktop = common ++ assemblySettings ++ Seq(
    unmanagedResourceDirectories in Compile += file("common/assets"),
    fork in Compile := true,
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % "0.9.9-SNAPSHOT",
      "com.badlogicgames.gdx" % "gdx-platform" % "0.9.9-SNAPSHOT" classifier "natives-desktop"
    )
  )

  lazy val android = common ++ natives ++ Seq(
    versionCode := 0,
    keyalias := "change-me",
    platformName := "android-17",
    mainAssetsPath in Compile := file("common/assets"),
    unmanagedJars in Compile <+= (libraryJarPath) (p => Attributed.blank(p)) map( x=> x),
    proguardOptions <<= (baseDirectory) { (b) => Seq(
      scala.io.Source.fromFile(b/"src/main/proguard.cfg").getLines.map(_.takeWhile(_!='#')).filter(_!="").mkString("\n")
    )},
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-android" % "0.9.9-SNAPSHOT",
      "com.badlogicgames.gdx" % "gdx-platform" % "0.9.9-SNAPSHOT" % "natives" classifier "natives-armeabi",
      "com.badlogicgames.gdx" % "gdx-platform" % "0.9.9-SNAPSHOT" % "natives" classifier "natives-armeabi-v7a"
    ),
    nativeExtractions <<= (baseDirectory) { base => Seq(
      ("natives-armeabi.jar", new ExactFilter("libgdx.so"), base / "lib" / "armeabi"),
      ("natives-armeabi-v7a.jar", new ExactFilter("libgdx.so"), base / "lib" / "armeabi-v7a")
    )}
  )

  lazy val ios = common ++ natives ++ Seq(
    unmanagedResources in Compile <++= (baseDirectory) map { _ =>
      (file("common/assets") ** "*").get
    },
    forceLinkClasses := Seq("com.badlogic.gdx.scenes.scene2d.ui.*", "com.jme3.scene.plugins.*"
      ,"com.jme3.asset.plugins.*"
      ,"com.jme3.texture.plugins.*"
      ,"com.jme3.audio.plugins.*"
      ,"com.jme3.audio.plugins.OGGLoader"
      ,"com.jme3.material.plugins.*"
      ,"com.jme3.font.plugins.*"
      ,"com.jme3.export.binary.*"
      ,"com.jme3.scene.plugins.ogre.*"
      ,"com.jme3.scene.plugins.blender.*"
      ,"com.jme3.shader.plugins.*"
      ,"projectkyoto.jme3.mmd.PMDLoaderGLSLSkinning2"
      ,"projectkyoto.jme3.mmd.VMDLoader"
      ,"java.util.logging.SimpleFormatter"

    ),
    skipPngCrush := true,
    iosInfoPlist <<= (sourceDirectory in Compile){ sd => Some(sd / "Info.plist") },
    frameworks := Seq("UIKit", "OpenGLES", "QuartzCore", "CoreGraphics", "OpenAL", "AudioToolbox", "AVFoundation"),
    nativePath <<= (baseDirectory){ bd => Seq(bd / "lib") },
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-robovm" % "0.9.9-SNAPSHOT",
      "com.badlogicgames.gdx" % "gdx-platform" % "0.9.9-SNAPSHOT" % "natives" classifier "natives-ios"
    ),
    nativeExtractions <<= (baseDirectory) { base => Seq(
      ("natives-ios.jar", new ExactFilter("libgdx.a") | new ExactFilter("libObjectAL.a"), base / "lib")
    )}
  )

  lazy val assemblyOverrides = Seq(
    mainClass in assembly := Some("my.game.pkg.Main"),
    AssemblyKeys.jarName in assembly := "my-game-0.1.jar"
  )

  lazy val nativeExtractions = SettingKey[Seq[(String, NameFilter, File)]]("native-extractions", "(jar name partial, sbt.NameFilter of files to extract, destination directory)")
  lazy val extractNatives = TaskKey[Unit]("extract-natives", "Extracts native files")
  lazy val natives = Seq(
    ivyConfigurations += config("natives"),
    nativeExtractions := Seq.empty,
    extractNatives <<= (nativeExtractions, update) map { (ne, up) =>
      val jars = up.select(configurationFilter("natives"))
      ne foreach { case (jarName, fileFilter, outputPath) =>
        jars find(_.getName.contains(jarName)) map { jar =>
            IO.unzip(jar, outputPath, fileFilter)
        }
      }
    },
    compile in Compile <<= (compile in Compile) dependsOn (extractNatives)
  )
}

object LibgdxBuild extends Build {
  lazy val common = Project(
    "common",
    file("common"),
    settings = Settings.common)

  lazy val desktop = Project(
    "desktop",
    file("desktop"),
    settings = Settings.desktop)
    .dependsOn(common)
    .settings(Settings.assemblyOverrides: _*)

  lazy val android = AndroidProject(
    "android",
    file("android"),
    settings = Settings.android)
    .dependsOn(common)

  lazy val ios = RobovmProject(
    "ios",
    file("ios"),
    settings = Settings.ios)
    .dependsOn(common)

  lazy val all = Project(
    "all-platforms",
    file("."),
    settings = Settings.common
  ) aggregate(common, desktop, android, ios)
}
