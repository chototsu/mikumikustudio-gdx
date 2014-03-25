import sbt._
import Keys._

object XcodeSettings {
  val xcodebuild = TaskKey[Unit]("xcodebuild", "run xcodebuild")
  val createXibViewClass = TaskKey[Unit]("create-xib-view-class", "create view classes from xib files.")
  val xcodeProjectRoot = SettingKey[File]("xcode-project-root", "the root directory of xcode project.")

  def run(cmd: Seq[String], dir: File): Unit = {
    val result = Process(cmd, dir).!
    if (result != 0) {
      throw new RuntimeException("xcodebuild failed. " + result)
    }
  }

  private def getElementById(node : scala.xml.Node, id : String) : Option[scala.xml.Node] = {
    if ((node \ "@id").text.equals(id) ) {
      return Some(node)
    } else {
      for(node2 <- node.child) {
         val result = getElementById(node2, id)
         if (result.isDefined) {
            return result
         }
      }
    }
    None
  }
  private def makeProperty(sb : StringBuilder, selfClass : String, clazz : String, name : String) : Unit = {
    val clazz2 = "UI"+clazz.charAt(0).toUpper+clazz.substring(1)
    val name2 = name.charAt(0).toUpper+name.substring(1)
    sb.append("\tprotected " + clazz2+" "+name+";\n")
    sb.append("\t@SuppressWarnings(\"unused\")\n")
    sb.append("\t@Callback\n")
    sb.append("\t@BindSelector(\"set"+name2+":\")\n")
    sb.append("\t@TypeEncoding(\"v@:@\")\n")
    sb.append("\tprivate static void set"+name2+"("+selfClass+" self, Selector selector, "+clazz2+" "+name+") {\n")
    sb.append("\t\tself."+name +"="+name+";\n")
    sb.append("\t}\n")

    sb.append("\t@SuppressWarnings(\"unused\")\n")
    sb.append("\t@Callback\n")
    sb.append("\t@BindSelector(\"get"+name2+"\")\n")
    sb.append("\t@TypeEncoding(\"v@:@\")\n")
    sb.append("\tprivate static "+clazz2+" get"+name2+"("+selfClass+" self, Selector selector) {\n")
    sb.append("\t\treturn self."+name+";\n")
    sb.append("\t}\n")
  }
  private def makeButtonAction(sb:StringBuilder, selfClass : String, clazz : String, actionName : String) : Unit = {
    val clazz2 = "UI"+clazz.charAt(0).toUpper+clazz.substring((1))
    sb.append("\t@SuppressWarnings(\"unused\")\n")
    sb.append("\t@Callback\n")
    sb.append("\t@BindSelector(\""+actionName+"\")\n")
    sb.append("\t@TypeEncoding(\"v@:@\")\n")
    sb.append("\tprivate static void "+(actionName.substring(0, actionName.length-1))+"("+selfClass+" self, Selector selector, "+clazz2+" sender) {\n")
    sb.append("\t\tself."+(actionName.substring(0, actionName.length-1))+"(sender);\n")
    sb.append("\t}\n")
    sb.append("\tabstract protected void "+(actionName.substring(0, actionName.length-1))+"("+clazz2+" sender);\n")
  }
  def makeClass(xibFile: File, distDir: File): Unit = {
    val xml = scala.xml.XML.loadFile(xibFile)
    val sb = new StringBuilder
    val placeholders = xml \\ "placeholder"
    sb.append(
      """package view;
        |import org.robovm.cocoatouch.uikit.*;
        |import org.robovm.objc.Selector;
        |import org.robovm.objc.annotation.BindSelector;
        |import org.robovm.objc.annotation.CustomClass;
        |import org.robovm.objc.annotation.TypeEncoding;
        |import org.robovm.rt.bro.annotation.Callback;
        |""".stripMargin)

    val result = (placeholders filter (p => {
      (p \ "@userLabel").text.equals("File's Owner")
    }) map {
      e =>
        val customClass = (e \ "@customClass").text
        sb.append("@CustomClass(\""+customClass+"\")\n")
        sb.append("public abstract class "+customClass+" extends UIViewController {\n")
        sb.append("\tpublic "+customClass+"() {\n")
        sb.append("\t\tsuper(\""+customClass+"\", null);\n")
        sb.append("\t}")
        val outlets = e \\ "outlet" map {
          o =>
            val property = (o \ "@property").text
            val destination = (o \ "@destination").text
            val e2 = getElementById(xml, destination).get
            if (property != "view")makeProperty(sb, customClass, e2.label, property)
            (property, e2.label)
        } filter (_._1 != "view")

        xml \\ "button" \"connections" \ "action" foreach(n => {
          val clazz = "button"
          val selector = (n \ "@selector").text
          makeButtonAction(sb, customClass, clazz, selector)
        }
          )


        sb.append("}\n")
        IO.write(distDir /  (customClass+".java"), sb.toString())
        (customClass, outlets)

    })
  }

  val xcodeSettings = Seq(
    xcodebuild in Compile <<= xcodeProjectRoot map {
      (root) =>
        run(Seq("rm","-fr","build"), root)
        (root / "../ios/lib/libappnative.a").delete()
        run(Seq("xcodebuild", "-sdk", "iphonesimulator7.1", "-target", "xcode_ios"), root)
        IO.listFiles(root / "build/Release-iphonesimulator/xcode_ios.app") filter (_.getName.endsWith(".nib")) foreach
          (file => IO.copyFile(file, root / "../ios/src/main/Resources" / file.getName))

        run(Seq("xcodebuild", "-sdk", "iphonesimulator7.1", "-target", "lib"), root)
        if ((root / "build/Release-iphonesimulator/liblib.a").exists()) {
          run(Seq("xcodebuild", "-sdk", "iphoneos7.1", "-target", "lib"), root)
          run(Seq("lipo", "-create", "build/Release-iphonesimulator/liblib.a", "./build/Release-iphoneos/liblib.a",
            "-output", "../ios/lib/libappnative.a"), root)
        }
    },
    xcodeProjectRoot <<= (baseDirectory) {
      base => base / "../xcode_ios"
    },
    createXibViewClass in Compile <<= xcodeProjectRoot map {
      (root) =>
        val files = IO.listFiles(root / "xcode_ios").filter(_.getName.endsWith(".xib"))
        files foreach (file =>
          makeClass(file, root / "../ios/generated/java/view")
          )
    },
    xcodebuild in Compile <<= xcodebuild in Compile dependsOn (createXibViewClass in Compile),
    unmanagedSourceDirectories in Compile <+= (baseDirectory)  (b => b / "generated/java"),
    cleanFiles <+= baseDirectory { base => base / "generated" },
    cleanFiles <+= xcodeProjectRoot { iosroot => iosroot / "build" }
  )
}