logLevel := Level.Warn

// Code formatting

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.2")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "0.3")

// Code Quality plugins

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.2.1")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")

addSbtPlugin("de.johoop" % "cpd4sbt" % "1.2.0")

// Utilities

addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.5")

addSbtPlugin("com.markatta" % "taglist-plugin" % "1.3.1")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.1")

// IDE Integration

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")
