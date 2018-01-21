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
import java.io.IOException
import java.util.ResourceBundle
import javafx.scene.{Parent, Scene}

import at.fhj.swengb.apps.battleship.jfx.BattleShipFxApp.display

class BattleShipFxWelcome extends Initializable {

  @FXML def newGame(): Unit = {
    BattleShipFxApp.display(BattleShipFxApp.loadNewGame,BattleShipFxApp.loadMain)
  }

  @FXML def join(): Unit = {
    BattleShipFxApp.loadFxmlJoinMode()
    BattleShipFxApp.display(BattleShipFxApp.loadJoin,BattleShipFxApp.loadMain)
  }

  @FXML def highscore(): Unit = {
    BattleShipFxApp.display(BattleShipFxApp.loadHighscore,BattleShipFxApp.loadMain)
  }

  @FXML def credits(): Unit = {
    BattleShipFxApp.display(BattleShipFxApp.loadCredits,BattleShipFxApp.loadMain)
  }




  override def initialize(location: URL, resources: ResourceBundle): Unit = {

  }

}

