ThisBuild / version      := "1.0.1"
ThisBuild / scalaVersion := "3.3.0"
ThisBuild / scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

ThisBuild / testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

val zioVersion        = "2.0.9"
val tapirVersion      = "1.2.6"
val zioLoggingVersion = "2.1.8"
val zioConfigVersion  = "3.0.7"
val sttpVersion       = "3.8.8"
val javaMailVersion   = "1.6.2"
val stripeVersion     = "22.12.0"

val commonDependencies = Seq(
  "com.softwaremill.sttp.tapir"   %% "tapir-sttp-client" % tapirVersion,
  "com.softwaremill.sttp.tapir"   %% "tapir-json-zio"    % tapirVersion,
  "com.softwaremill.sttp.client3" %% "zio"               % sttpVersion
)

val serverDependencies = commonDependencies ++ Seq(
  // server dependencies
  "com.softwaremill.sttp.tapir" %% "tapir-zio" % tapirVersion, // Brings in zio, zio-streams
  "com.softwaremill.sttp.tapir"   %% "tapir-zio-http-server"   % tapirVersion, // Brings in zhttp
  "com.softwaremill.sttp.tapir"   %% "tapir-swagger-ui-bundle" % tapirVersion,
  "com.softwaremill.sttp.tapir"   %% "tapir-sttp-stub-server"  % tapirVersion % "test",
  "com.softwaremill.sttp.tapir"   %% "tapir-sttp-client"       % tapirVersion,
  "com.softwaremill.sttp.tapir"   %% "tapir-json-zio"          % tapirVersion,
  "com.softwaremill.sttp.client3" %% "zio"                     % sttpVersion,
  "dev.zio"                       %% "zio-logging"             % zioLoggingVersion,
  "dev.zio"                       %% "zio-logging-slf4j"       % zioLoggingVersion,
  "ch.qos.logback"                 % "logback-classic"         % "1.4.4",
  "dev.zio"                       %% "zio-test"                % zioVersion,
  "dev.zio"                       %% "zio-test-junit"          % zioVersion % "test",
  "dev.zio"                       %% "zio-test-sbt"            % zioVersion % "test",
  "dev.zio"                       %% "zio-test-magnolia"       % zioVersion % "test",
  "dev.zio"                       %% "zio-mock"                % "1.0.0-RC9" % "test",
  "dev.zio"                       %% "zio-config"              % zioConfigVersion,
  "dev.zio"                       %% "zio-config-magnolia"     % zioConfigVersion,
  "dev.zio"                       %% "zio-config-typesafe"     % zioConfigVersion,
  "io.getquill"                   %% "quill-jdbc-zio"          % "4.6.0.1",
  "org.postgresql"                 % "postgresql"              % "42.5.0",
  "org.flywaydb"                   % "flyway-core"             % "9.7.0",
  "io.github.scottweaver" %% "zio-2-0-testcontainers-postgresql" % "0.9.0",
  "dev.zio"               %% "zio-prelude"                       % "1.0.0-RC16",
  "com.auth0"              % "java-jwt"                          % "4.2.1",
  "com.sun.mail"           % "javax.mail"                        % javaMailVersion,
  "com.stripe"             % "stripe-java"                       % stripeVersion
)

lazy val common = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/common"))
  .settings(
    libraryDependencies ++= commonDependencies
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % "2.5.0" // implementations of java.time classes for Scala.JS,
    )
  )

lazy val server = (project in file("modules/server"))
  .settings(
    libraryDependencies ++= serverDependencies
  )
  .dependsOn(common.jvm)

lazy val app = (project in file("modules/app"))
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir"   %%% "tapir-sttp-client" % tapirVersion,
      "com.softwaremill.sttp.tapir"   %%% "tapir-json-zio"    % tapirVersion,
      "com.softwaremill.sttp.client3" %%% "zio"               % sttpVersion,
      "dev.zio"                       %%% "zio-json"          % "0.4.2",
      "io.frontroute"                 %%% "frontroute"        % "0.18.1" // Brings in Laminar 16
    ),
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    semanticdbEnabled               := true,
    autoAPIMappings                 := true,
    scalaJSUseMainModuleInitializer := true,
    Compile / mainClass             := Some("com.rockthejvm.reviewboard.App")
  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(common.js)

lazy val root = (project in file("."))
  .settings(
    name := "scalar-2024"
  )
  .aggregate(server, app)
  .dependsOn(server, app)

val buildSettings = Seq(
  name            := "rockthejvm-reviewboard",
  dockerBaseImage := "openjdk:21-slim-buster", // can use a different JDK
  dockerExposedPorts ++= Seq(4041),
  Compile / mainClass         := Some("com.rockthejvm.reviewboard.Application"),
  Compile / resourceDirectory := ((server / Compile / resourceDirectory).value)
)

lazy val stagingBuild = (project in (file("build/staging")))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(buildSettings)
  .dependsOn(server)

lazy val prodBuild = (project in (file("build/prod")))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(buildSettings)
  .dependsOn(server)
