name := "cast"

version := "0.1"

scalaVersion := "2.12.12"

resolvers ++= Seq(
  "local ivy Repository" at "file://" + Path.userHome.absolutePath + "/.ivy2/local"
)
routesGenerator := InjectedRoutesGenerator
lazy val `cast` = (project in file(".")).enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided",
      "com.h2database" % "h2" % "1.4.192",
      jdbc))

lagomKafkaEnabled in ThisBuild := false
lagomCassandraEnabled in ThisBuild := false
