object Dependencies {
  import sbt._
  import scalapb.compiler.Version
  val CodeheroesCommonsVersion = "0.127_2.13"
  val ScalaTestVersion         = "3.0.8"
  val ScalaMockVersion         = "4.4.0"
  val SimulacrumVersion        = "0.19.0"
  val TypesafeConfigVersion    = "1.4.0"

  val grpcNettyVersion: String   = Version.grpcJavaVersion
  val grpcRuntimeVersion: String = Version.scalapbVersion
  val SlickPgVersion             = "0.18.0"

  private val postgresDependencies =
    Seq(
      "io.codeheroes"       %% "commons-postgres"    % CodeheroesCommonsVersion,
      "com.github.tminglei" %% "slick-pg"            % SlickPgVersion,
      "com.github.tminglei" %% "slick-pg_circe-json" % SlickPgVersion
    )

  val CirceVersion = "0.13.0" // TODO:bcm use or loose it

  private val grpcDependencies = Seq(
    "io.grpc"              % "grpc-netty"            % grpcNettyVersion,
    "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % grpcRuntimeVersion
  )

  private val miscDependencies = Seq(
    "io.codeheroes"        %% "commons-core" % CodeheroesCommonsVersion,
    "com.github.mpilquist" %% "simulacrum"   % SimulacrumVersion,
    "com.typesafe"         % "config"        % TypesafeConfigVersion
  )

  private val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % ScalaTestVersion % Test,
    "org.scalamock" %% "scalamock" % ScalaMockVersion % Test
  )

  val all: Seq[ModuleID] = Seq(
    postgresDependencies,
    grpcDependencies,
    testDependencies,
    miscDependencies
  ).flatten

  val additionalResolvers: Seq[Resolver] = Seq(
    Resolver.bintrayRepo("codeheroes", "maven"),
    Resolver.typesafeRepo("releases")
  )

}
