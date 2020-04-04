import sbt.Keys.baseDirectory
enablePlugins(SbtNativePackager)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")

version := "0.0"

val protoSettings = Seq(
  PB.targets in Compile := Seq(
    scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
  )
)

dockerBaseImage := "openjdk:13-jdk-buster"
daemonUser in Docker := "root"
dockerRepository := Some("docker.codeheroes.io")
dockerExposedPorts := Seq(8080)

addCompilerPlugin(scalafixSemanticdb)

lazy val `manual-ecommerce-provider` = (project in file("."))
  .settings(
    organization := "com.guys.coding",
    name := "manual-ecommerce-provider",
    scalaVersion := "2.13.1",
    resolvers ++= Dependencies.additionalResolvers,
    libraryDependencies ++= Dependencies.all,
    scalacOptions ++= CompilerOps.all,
    parallelExecution in Test := false
  )
  .settings(protoSettings: _*)

PB.protoSources in Compile := Seq(
  baseDirectory.value / ".." / ".." / "creator" / "creator-protobufs" / "manual-ecommerce-provider"
)
