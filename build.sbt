name          := "transfer-treatment-oplg"
version       := "0.1"
scalaVersion  := "2.12.8"

lazy val akkaHttpVersion = "10.1.7"
lazy val akkaVersion     = "2.5.21"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerExposedPorts ++= Seq(8080)

mainClass in Compile := Some("Application")