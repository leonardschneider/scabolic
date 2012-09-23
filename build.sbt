name := "Regolic"

version := "0.1"

scalaVersion := "2.9.1"

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "net.sf.squirrel-sql.thirdparty.non-maven" % "java-cup" % "11a"

script <<= (baseDirectory, fullClasspath in Runtime, mainClass in Runtime) map { (base, cp, main) =>
  val template = """#!/bin/sh
java -classpath "%s" %s "$@"
"""
    val mainStr = main getOrElse error("No main class specified")
    val contents = template.format(cp.files.absString, mainStr)
    val out = base / "regolic"
    IO.write(out, contents)
    out.setExecutable(true)
    out
}

cleanFiles <+= baseDirectory { base => base / "regolic" }

cleanFiles <+= baseDirectory { base => base / "lib" / "smt-parser.jar" }

smtlibParser <<= (unmanagedJars in Compile, managedClasspath in Compile, baseDirectory) map { (unmanaged, managed, base) =>
  val classpath: String = (unmanaged ++ managed).map(_.data).mkString(":")
  val smtlibDir = base / "smt-parser"
  val exitCode = Process("make CLASSPATH=" + classpath, smtlibDir) !;
  if(exitCode != 0)
    error("Failure")
  val jar = smtlibDir / "smt-parser.jar"
  val libJar = base / "lib" / "smt-parser.jar"
  IO.copyFile(jar, libJar)
  libJar
}

compile <<= (compile in Compile) dependsOn smtlibParser

clean <<= (clean, baseDirectory) map { (c, base) =>
  val smtlibDir = base / "smt-parser"
  Process("make clean", smtlibDir) !;
  c
}
