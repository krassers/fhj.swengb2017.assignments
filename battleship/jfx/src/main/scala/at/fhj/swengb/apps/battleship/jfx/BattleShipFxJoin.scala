package at.fhj.swengb.apps.battleship.jfx

import java.io.File
import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Scene
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util
import javafx.beans.InvalidationListener
import javafx.collections.{FXCollections, ListChangeListener, ObservableArray, ObservableList}
import javafx.scene.control.ListView

import scala.collection.mutable.ListBuffer



class BattleShipFxJoin extends Initializable {

 @FXML var gamesToJoin: ListView[String] = _

 @FXML def next(): Unit = {
  BattleShipFxApp.display(BattleShipFxApp.loadEditGame,BattleShipFxApp.loadMain)
 }

 @FXML def back(): Unit = {
  BattleShipFxApp.display(BattleShipFxApp.loadWelcome,BattleShipFxApp.loadMain)
 }


 override def initialize(location: URL, resources: ResourceBundle): Unit = init()


 def getFiles(): List[File] = {
  val d = new File("battleship")
  if (d.exists && d.isDirectory)
   d.listFiles.filter(x => x.isFile && x.getName.contains(".bin")).toList
  else
   List[File]()

 }


 def init(): Unit = {
  val files = getFiles()
  val names = files.map(x => (x.getName, x.lastModified()))
  val recentGame = names.max

  BattleShipFxApp.setFilename(recentGame._1)

  println(recentGame)
 }

}