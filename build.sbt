name          := "transfer-treatment-oplg"
version       := "0.1"
scalaVersion  := "2.12.8"

lazy val akkaHttpVersion = "10.1.7"
lazy val akkaVersion     = "2.5.21"
lazy val mongoVersion    = "2.6.0"
lazy val kafkaVersion    = "2.1.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

  "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
  "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
  "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
  "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test,

  "org.mongodb.scala" %% "mongo-scala-driver"   % mongoVersion,
  "org.apache.kafka"  %% "kafka"                % kafkaVersion,
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerExposedPorts ++= Seq(8080)

mainClass in Compile := Some("Application")