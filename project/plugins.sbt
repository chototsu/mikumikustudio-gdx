resolvers += Resolver.url("scalasbt snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots"))(Resolver.ivyStylePatterns)

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

addSbtPlugin("org.scala-sbt" % "sbt-android" % "0.7")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.9.2")

addSbtPlugin("info.projectkyoto" % "sbt-robovm" % "0.1.0-SNAPSHOT")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.1") 

