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
      "com.github.tminglei" %% "slick-pg_jts"        % SlickPgVersion,
      "com.github.tminglei" %% "slick-pg_circe-json" % SlickPgVersion
    )

  val Http4sVersion       = "0.21.1"
  val CirceVersion        = "0.13.0"
  val CirceOpticsVersion  = "0.13.0"
  val SangriaCirceVersion = "1.3.0"
  val SangriaVersion      = "2.0.0-M3"
  val JwtVersion          = "4.2.0"

  private val http4sDependencies = Seq(
    "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
    "org.http4s" %% "http4s-circe"        % Http4sVersion,
    "org.http4s" %% "http4s-dsl"          % Http4sVersion,
    "io.circe"   %% "circe-generic"       % CirceVersion,
    "io.circe"   %% "circe-optics"        % CirceOpticsVersion
  )

  private val jwtDependencies = Seq(
    "com.pauldijou" %% "jwt-circe" % JwtVersion
  )

  private val sangriaDependencies = Seq(
    "org.sangria-graphql" %% "sangria"         % SangriaVersion,
    "org.sangria-graphql" %% "sangria-circe"   % SangriaCirceVersion,
    "io.codeheroes"       %% "commons-sangria" % CodeheroesCommonsVersion
  )

  private val grpcDependencies = Seq(
    "io.grpc"              % "grpc-netty"            % grpcNettyVersion,
    "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % grpcRuntimeVersion
  )

  private val miscDependencies = Seq(
    "io.codeheroes"        %% "commons-sangria" % CodeheroesCommonsVersion,
    "io.codeheroes"        %% "commons-core"    % CodeheroesCommonsVersion,
    "com.github.mpilquist" %% "simulacrum"      % SimulacrumVersion,
    "com.typesafe"         % "config"           % TypesafeConfigVersion
  )

  private val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % ScalaTestVersion % Test,
    "org.scalamock" %% "scalamock" % ScalaMockVersion % Test
  )

  val all: Seq[ModuleID] = Seq(
    http4sDependencies,
    sangriaDependencies,
    jwtDependencies,
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
