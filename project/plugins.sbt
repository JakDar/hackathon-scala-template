addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.4.1")
addSbtPlugin("org.scalameta"    % "sbt-scalafmt"        % "2.3.1")

addSbtPlugin("com.thesamet"  % "sbt-protoc"   % "0.99.25")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.11")
libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.9.6"
