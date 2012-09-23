import sbt._
import Keys._

object Regolic extends Build {

  val testIntegration: TaskKey[Unit] = TaskKey[Unit]("test-integration", "Execute integration tests")
  val testAll: TaskKey[Unit] = TaskKey[Unit]("test-all", "Execute unit and integration tests")
  val smtlibParser: TaskKey[File] = TaskKey[File]("smtlib-parser", "Build the smtlib parser")
  val script: TaskKey[File] = TaskKey[File]("script", "Create the run script")

  val testIntegrationTask = testIntegration <<= (compile in Compile, baseDirectory, script) map { (_, base, _) => 
    val exitCode = base + "/scripts/run-regression.sh" !;
    if(exitCode != 0) sys.error("Failure")
  }
  val testAllTask = testAll <<= (test in Test, testIntegration) map { (_, _) => ()}


  lazy val root = Project("root", file("."), settings = Project.defaultSettings ++ Seq(testIntegrationTask, testAllTask))
}