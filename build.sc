// import Mill dependency
import mill._
import mill.define.Sources
import mill.modules.Util
import mill.scalalib.scalafmt.ScalafmtModule
import mill.scalalib.TestModule.ScalaTest
import mill.scalalib._
// support BSP
import mill.bsp._

object playground extends SbtModule with ScalafmtModule { m =>
  val useChisel3 = false
  override def millSourcePath = os.pwd / "src"
  override def scalaVersion = if (useChisel3) "2.13.10" else "2.13.14"
  override def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit"
  )
  override def sources = T.sources {
    super.sources() ++ Seq(PathRef(millSourcePath / "main"))
  }
  override def ivyDeps = Agg(
    if (useChisel3) ivy"edu.berkeley.cs::chisel3:3.6.0" else
    ivy"org.chipsalliance::chisel:6.4.0"
  )
  override def scalacPluginIvyDeps = Agg(
    if (useChisel3) ivy"edu.berkeley.cs:::chisel3-plugin:3.6.0" else
    ivy"org.chipsalliance:::chisel-plugin:6.4.0"
  )
  object test extends SbtModuleTests with TestModule.ScalaTest with ScalafmtModule {
    override def sources = T.sources {
      super.sources() ++ Seq(PathRef(this.millSourcePath / "test"))
    }
    override def ivyDeps = super.ivyDeps() ++ Agg(
      if (useChisel3) ivy"edu.berkeley.cs::chiseltest:0.6.0" else
      ivy"edu.berkeley.cs::chiseltest:6.0.0"
    )
  }
  def repositoriesTask = T.task { Seq(
    coursier.MavenRepository("https://repo.scala-sbt.org/scalasbt/maven-releases"),
    coursier.MavenRepository("https://oss.sonatype.org/content/repositories/releases"),
    coursier.MavenRepository("https://oss.sonatype.org/content/repositories/snapshots"),
  ) ++ super.repositoriesTask() }
}
