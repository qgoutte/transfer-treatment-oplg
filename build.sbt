import com.sun.org.apache.xalan.internal.xsltc.cmdline.Compile

name          := "transfer-treatment-oplg"

version       := "0.1"

scalaVersion  := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"           % "2.5.21",
  "com.typesafe.akka" %% "akka-stream"          % "2.5.21",
  "com.typesafe.akka" %% "akka-http"            % "10.1.7",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7",
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  "com.typesafe.play" % "play-json_2.11" % "2.4.0-M2",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "Typesafe" at "https://repo.typesafe.com/typesafe/releases/"

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerExposedPorts ++= Seq(8080)

mainClass in Compile := Some("Application")