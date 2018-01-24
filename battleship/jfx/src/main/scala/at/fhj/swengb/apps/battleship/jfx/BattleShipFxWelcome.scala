package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.nio.file.{Files, Paths}
import java.util.ResourceBundle
import javafx.application.Application
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.TextArea
import javafx.scene.layout.GridPane
import javafx.stage.Stage

import at.fhj.swengb.apps.battleship.BattleShipProtobuf
import at.fhj.swengb.apps.battleship.BattleShipProtocol._
import at.fhj.swengb.apps.battleship.model._
import javafx.scene.layout.Pane
import java.io.{File, IOException}
import java.util.ResourceBundle
import javafx.scene.{Parent, Scene}

import at.fhj.swengb.apps.battleship.jfx.BattleShipFxApp.display

class BattleShipFxWelcome extends Initializable {

  @FXML def newGame(): Unit = {
    BattleShipFxApp.display(BattleShipFxApp.loadNewGame,BattleShipFxApp.loadMain)
  }

  @FXML def join(): Unit = {
    init()
    BattleShipFxApp.loadFxmlEditMode()
    BattleShipFxApp.display(BattleShipFxApp.loadEditGame,BattleShipFxApp.loadMain)
  }

  @FXML def highscore(): Unit = {
    BattleShipFxApp.loadFxmlHighscore()
    BattleShipFxApp.display(BattleShipFxApp.loadHighscore,BattleShipFxApp.loadMain)
  }

  @FXML def credits(): Unit = {
    BattleShipFxApp.display(BattleShipFxApp.loadCredits,BattleShipFxApp.loadMain)
  }

  
  override def initialize(location: URL, resources: ResourceBundle): Unit = {

  }

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
    val filename = "battleship/"++recentGame._1
    BattleShipFxApp.setFilename(filename)

    val gameRound = BattleShipFxApp.loadGameState(filename)
    gameRound.setNumberCurrentPlayers(2)
    BattleShipFxApp.setGameRound(gameRound)


    println(recentGame)
  }

}

