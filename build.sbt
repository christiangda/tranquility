scalaVersion := "2.10.6"

crossScalaVersions := Seq("2.10.6", "2.11.8")

// Disable parallel execution, the various Druid oriented tests need to claim ports
parallelExecution in ThisBuild := false

// Disable parallel execution, the various Druid oriented tests need to claim ports
parallelExecution in Test := false

concurrentRestrictions in Global += Tags.limitAll(1)


val jacksonOneVersion = "1.9.13"
// See https://github.com/druid-io/druid/pull/1669, https://github.com/druid-io/tranquility/pull/81 before upgrading Jackson
val jacksonTwoVersion = "2.8.4"
val jacksonTwoModuleScalaVersion = "2.8.4"
val druidVersion = "0.9.1.1"
val guiceVersion = "4.0"
val flinkVersion = "1.0.3"
val finagleVersion = "6.31.0"
val twitterUtilVersion = "6.30.0"
val samzaVersion = "0.8.0"
val sparkVersion = "2.0.2"
val scalatraVersion = "2.3.1"
val jettyVersion = "9.2.5.v20141112"
val apacheHttpVersion = "4.3.3"
val kafkaVersion = "0.8.2.2"
val airlineVersion = "0.7"
val stormVersion = "1.0.2"
val chillVersion = "0.8.1"
val googleGuavaVersion = "18.0"
val metamxJavaUtil = "0.27.11"
val metamxScalaUtil = "1.12.5"
val metamxEmitterVersion = "0.3.6"
val metamxHttpClientVersion = "1.0.5"
val slf4jVersion = "1.7.21"
val logbackVersion = "1.1.7"
val loggingLog4jVersion = "2.7"
val javaxValidationVersion = "1.1.0.Final"
val findbugsAnnotationsVersion = "3.0.1u2"
val findbugsjsr305Version = "3.0.1"
val ioNettyVersion = "3.10.6.Final"
val jerseyVersion = "1.19.3"
val curatorTestVersion = "3.1.0"

def dependOnDruid(artifact: String) = {
  ("io.druid" % artifact % druidVersion
    exclude("org.slf4j", "slf4j-log4j12")
    exclude("log4j", "log4j")
    exclude("org.apache.logging.log4j", "log4j-core")
    exclude("org.apache.logging.log4j", "log4j-api")
    exclude("org.apache.logging.log4j", "log4j-slf4j-impl")
    exclude("org.apache.logging.log4j", "log4j-1.2-api")
    exclude("javax.validation", "validation-api")
    exclude("com.google.inject.extensions", "guice-servlet")
    exclude("com.google.inject.extensions", "guice-multibindings")
    exclude("com.google.inject", "guice")
    exclude("com.google.guava", "guava")
    exclude("com.google.code.findbugs", "jsr305")
    exclude("com.twitter", "finagle-core")
    exclude("com.twitter", "finagle-http")
    exclude("com.fasterxml.jackson.dataformat", "jackson-dataformat-smile" )
    exclude("com.sun.jersey","jersey-core")
    exclude("com.sun.jersey","jersey-server")
    exclude("com.sun.jersey.contribs","jersey-guice")
    exclude("com.metamx", "server-metrics")
    exclude("com.metamx", "http-client")
    exclude("io.airlift", "airline")
    exclude("com.fasterxml.jackson.module", "jackson-module-scala")
    force()
    )
}

val coreDependencies = Seq(
  "com.metamx" %% "scala-util" % metamxScalaUtil exclude("log4j", "log4j")
    exclude("javax.validation", "validation-api")
    exclude("com.twitter", "util-core")
    exclude("com.twitter", "finagle-core")
    exclude("com.twitter", "finagle-http")
    exclude("com.metamx", "java-util")
    exclude("org.slf4j", "slf4j-api")
    exclude("io.netty", "netty")
    exclude("com.google.guava", "guava")
    exclude("com.fasterxml.jackson.datatype", "jackson-datatype-joda")
    exclude("com.fasterxml.jackson.core", "jackson-core")
    exclude("com.fasterxml.jackson.core", "jackson-annotations")
    exclude("com.fasterxml.jackson.core", "jackson-databind")
    exclude("com.fasterxml.jackson.dataformat", "jackson-dataformat-smile")
    exclude("com.fasterxml.jackson.module", "jackson-module-scala")
    exclude("com.metamx", "emitter")
    force(),
  "com.metamx" % "java-util" % metamxJavaUtil exclude("log4j", "log4j")
    exclude("javax.validation", "validation-api")
    exclude("com.twitter", "finagle-core")
    exclude("com.twitter", "finagle-http")
    exclude("org.slf4j", "slf4j-api")
    exclude("com.google.guava", "guava")
    exclude("com.fasterxml.jackson.core", "jackson-core")
    exclude("com.fasterxml.jackson.core", "jackson-annotations")
    exclude("com.fasterxml.jackson.core", "jackson-databind")
    force(),
  "com.metamx" % "emitter" % metamxEmitterVersion
    exclude("com.google.guava", "guava")
    exclude("com.metamx", "java-util")
    exclude("com.metamx", "http-client")
    force(),
  "com.metamx" % "http-client" % metamxHttpClientVersion
    exclude("com.google.guava", "guava")
    exclude("com.metamx", "java-util")
    exclude("io.netty", "netty")
    force(),
  // Curator uses Jackson 1.x internally, and older version cause problems with service discovery.
  "org.codehaus.jackson" % "jackson-core-asl" % jacksonOneVersion force(),
  "org.codehaus.jackson" % "jackson-mapper-asl" % jacksonOneVersion force(),
  // We use Jackson 2.x internally (and so does Druid).
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonTwoVersion force(),
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonTwoVersion force(),
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonTwoVersion force(),
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-smile" % jacksonTwoVersion force(),
  "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % jacksonTwoVersion force(),
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonTwoModuleScalaVersion force(),
  "com.fasterxml.jackson.module" % "jackson-module-jaxb-annotations" % jacksonTwoModuleScalaVersion force(),
  "io.netty" % "netty" % ioNettyVersion force(),
  "javax.validation" % "validation-api" % javaxValidationVersion force(),
  "com.twitter" %% "util-core" % twitterUtilVersion force(),
  "com.twitter" %% "finagle-core" % finagleVersion
    exclude("com.google.guava", "guava")
    exclude("io.netty", "netty")
    force(),
  "com.twitter" %% "finagle-http" % finagleVersion
    exclude("com.google.guava", "guava")
    force(),
  "org.slf4j" % "slf4j-api" % slf4jVersion force() force(),
  "org.slf4j" % "jul-to-slf4j" % slf4jVersion force() force(),
  "org.apache.httpcomponents" % "httpclient" % apacheHttpVersion force(),
  "org.apache.httpcomponents" % "httpcore" % apacheHttpVersion force(),
  "com.google.code.findbugs" % "annotations" % findbugsAnnotationsVersion
    exclude("com.google.code.findbugs", "jsr305")
    force(),
  "com.google.code.findbugs" % "jsr305" % findbugsjsr305Version force(),
  "com.sun.jersey" % "jersey-core" % jerseyVersion force(),
  "com.sun.jersey" % "jersey-server" % jerseyVersion force(),
  "com.sun.jersey.contribs" % "jersey-guice" % jerseyVersion
    exclude("com.google.inject.extensions", "guice-servlet")
    exclude("com.google.inject", "guice")
    force(),
  "io.airlift" % "airline" % airlineVersion
    exclude("com.google.code.findbugs", "annotations")
    exclude("com.google.guava", "guava")
    force()
) ++ Seq(
  dependOnDruid("druid-server"),
  "com.google.inject" % "guice" % guiceVersion
    exclude("com.google.guava", "guava")
    force(),
  "com.google.inject.extensions" % "guice-servlet" % guiceVersion
    exclude("com.google.inject", "guice")
    force(),
  "com.google.inject.extensions" % "guice-multibindings" % guiceVersion
    exclude("com.google.inject", "guice")
    force(),
  "com.google.inject.extensions" % "guice-servlet" % guiceVersion force(),
  "com.google.guava" % "guava" % googleGuavaVersion force()
)

val loggingDependencies = Seq(
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "org.apache.logging.log4j" % "log4j-to-slf4j" % loggingLog4jVersion,
  "org.apache.logging.log4j" % "log4j-api" % loggingLog4jVersion,
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % loggingLog4jVersion,
  "org.apache.logging.log4j" % "log4j-core" % loggingLog4jVersion,
  "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
  "org.slf4j" % "jul-to-slf4j" % slf4jVersion
)

def flinkDependencies(scalaVersion: String) = {
  Seq(
    "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "optional"
      exclude("log4j", "log4j")
      exclude("org.slf4j", "slf4j-log4j12")
      force()
  )
}

val stormDependencies = Seq(
  "org.apache.storm" % "storm-core" % stormVersion % "optional"
    exclude("org.slf4j", "slf4j-api")
    exclude("org.slf4j", "log4j-over-slf4j")
    exclude("ch.qos.logback", "logback-classic")
    force(),
  "com.twitter" %% "chill" % chillVersion)

val samzaDependencies = Seq(
  "org.apache.samza" % "samza-api" % samzaVersion % "optional"
)

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-streaming" % sparkVersion % "optional"
    exclude("org.slf4j", "log4j-over-slf4j")
    exclude("org.slf4j", "slf4j-log4j12")
    force()
)

val serverDependencies = Seq(
  "org.scalatra" %% "scalatra" % scalatraVersion,
  "org.eclipse.jetty" % "jetty-server" % jettyVersion,
  "org.eclipse.jetty" % "jetty-servlet" % jettyVersion,
  "javax.validation" % "validation-api" % javaxValidationVersion force()
) ++ loggingDependencies

val kafkaDependencies = Seq(
  "org.apache.kafka" %% "kafka" % kafkaVersion
    exclude("org.slf4j", "slf4j-log4j12")
    exclude("log4j", "log4j")
    force(),
  "io.airlift" % "airline" % airlineVersion
    exclude("com.google.code.findbugs", "annotations")
    exclude("com.google.guava", "guava")
    force()

) ++ loggingDependencies

val coreTestDependencies = Seq(
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  dependOnDruid("druid-services") % "test",
  "org.apache.curator" % "curator-test" % curatorTestVersion % "test"
    exclude("log4j", "log4j")
    force(),
  "com.sun.jersey" % "jersey-servlet" % "1.17.1" % "test" force(),
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "ch.qos.logback" % "logback-core" % logbackVersion % "test",
  "ch.qos.logback" % "logback-classic" % logbackVersion % "test",
  "org.apache.logging.log4j" % "log4j-to-slf4j" % loggingLog4jVersion % "test",
  "org.apache.logging.log4j" % "log4j-api" % loggingLog4jVersion % "test"
) ++ loggingDependencies.map(_ % "test")

def flinkTestDependencies(scalaVersion: String) = {
  Seq("org.apache.flink" % "flink-core" % flinkVersion % "test" classifier "tests",
    "org.apache.flink" %% "flink-runtime" % flinkVersion % "test" classifier "tests",
    "org.apache.flink" %% "flink-test-utils" % flinkVersion % "test"
  ).map(_ exclude("log4j", "log4j") exclude("org.slf4j", "slf4j-log4j12") force()) ++
    loggingDependencies.map(_ % "test")
}

// Force 2.10 here, makes update resolution happy, but since w'ere not building for 2.11
// we won't end up in runtime version hell by doing this.
val samzaTestDependencies = Seq(
  "org.apache.samza" % "samza-core_2.10" % samzaVersion % "test"
)

val serverTestDependencies = Seq(
  "org.scalatra" %% "scalatra-test" % scalatraVersion % "test"
)

val kafkaTestDependencies = Seq(
  "org.easymock" % "easymock" % "3.4" % "test"
)

lazy val commonSettings = Seq(
  organization := "io.druid",

  javaOptions := Seq("-Xms512m", "-Xmx512m", "-XX:MaxPermSize=512M"),

  // Target Java 7
  scalacOptions += "-target:jvm-1.7",
  javacOptions in compile ++= Seq("-source", "1.7", "-target", "1.7"),

  // resolve-term-conflict:object since storm-core has a package and object with the same name
  scalacOptions := Seq("-feature", "-deprecation", "-Yresolve-term-conflict:object"),

  licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),

  homepage := Some(url("https://github.com/druid-io/tranquility")),

  publishMavenStyle := true,

  publishTo := Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"),

  pomIncludeRepository := { _ => false },

  pomExtra := (
    <scm>
      <url>https://github.com/druid-io/tranquility.git</url>
      <connection>scm:git:git@github.com:druid-io/tranquility.git</connection>
    </scm>
      <developers>
        <developer>
          <name>Gian Merlino</name>
          <organization>Druid Project</organization>
          <organizationUrl>http://druid.io/</organizationUrl>
        </developer>
      </developers>),

  fork in Test := true
) ++ releaseSettings ++ net.virtualvoid.sbt.graph.Plugin.graphSettings ++ Seq(
  ReleaseKeys.publishArtifactsAction := PgpKeys.publishSigned.value
)

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(publishArtifact := false)
  .aggregate(core, flink, storm, samza, spark, server, kafka)

lazy val core = project.in(file("core"))
  .settings(commonSettings: _*)
  .settings(name := "tranquility-core")
  .settings(publishArtifact in(Test, packageBin) := true)
  .settings(libraryDependencies ++= (coreDependencies ++ coreTestDependencies))

lazy val flink = project.in(file("flink"))
  .settings(commonSettings: _*)
  .settings(name := "tranquility-flink")
  .settings(libraryDependencies <++= scalaVersion(flinkDependencies))
  .settings(libraryDependencies <++= scalaVersion(flinkTestDependencies))
  .dependsOn(core % "test->test;compile->compile")

lazy val spark = project.in(file("spark"))
  .settings(commonSettings: _*)
  .settings(name := "tranquility-spark")
  .settings(libraryDependencies ++= sparkDependencies)
  .dependsOn(core % "test->test;compile->compile")

lazy val storm = project.in(file("storm"))
  .settings(commonSettings: _*)
  .settings(name := "tranquility-storm")
  .settings(resolvers += "clojars" at "http://clojars.org/repo/")
  .settings(libraryDependencies ++= stormDependencies)
  .dependsOn(core % "test->test;compile->compile")

lazy val samza = project.in(file("samza"))
  .settings(commonSettings: _*)
  .settings(name := "tranquility-samza")
  .settings(libraryDependencies ++= (samzaDependencies ++ samzaTestDependencies))
  // don't compile or publish for Scala > 2.10
  .settings((skip in compile) := scalaVersion { sv => !sv.startsWith("2.10.") }.value)
  .settings((skip in test) := scalaVersion { sv => !sv.startsWith("2.10.") }.value)
  .settings(publishArtifact <<= scalaVersion { sv => sv.startsWith("2.10.") })
  .settings(publishArtifact in Test := false)
  .dependsOn(core % "test->test;compile->compile")

lazy val server = project.in(file("server"))
  .settings(commonSettings: _*)
  .settings(name := "tranquility-server")
  .settings(libraryDependencies ++= (serverDependencies ++ serverTestDependencies))
  .settings(publishArtifact in Test := false)
  .dependsOn(core % "test->test;compile->compile")

lazy val kafka = project.in(file("kafka"))
  .settings(commonSettings: _*)
  .settings(name := "tranquility-kafka")
  .settings(libraryDependencies ++= (kafkaDependencies ++ kafkaTestDependencies))
  .settings(publishArtifact in Test := false)
  .dependsOn(core % "test->test;compile->compile")

lazy val distribution = project.in(file("distribution"))
  .settings(commonSettings: _*)
  .settings(name := "tranquility-distribution")
  .settings(publishArtifact in Test := false)
  .settings(mainClass in Compile := Some("com.metamx.tranquility.distribution.DistributionMain"))
  .settings(executableScriptName := "tranquility")
  .settings(bashScriptExtraDefines += """addJava "-Dlogback.configurationFile=${app_home}/../conf/logback.xml"""")
  .enablePlugins(JavaAppPackaging)
  .dependsOn(kafka, server)
