package at.fhj.swengb.apps.battleship.jfx

import javafx.application.{Application, Preloader}
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import scala.util.{Failure, Success, Try}
import java.io.IOException
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.text.SimpleDateFormat
import java.util.Calendar
import javafx.scene.control.ProgressBar
import javafx.scene.image.ImageView
import javafx.scene.layout.{BorderPane, Pane}

import at.fhj.swengb.apps.battleship.BattleShipProtobuf
import at.fhj.swengb.apps.battleship.BattleShipProtocol.convert
import at.fhj.swengb.apps.battleship.model.GameRound
import com.sun.javafx.application.LauncherImpl
import at.fhj.swengb.apps.battleship.BattleShipProtocol._




/**object BattleShipFxApp {
  def main(args: Array[String]): Unit = {

    Application.launch(classOf[BattleShipFxApp], args: _*)
  }
}


class BattleShipFxApp extends Application {

  //val triedRoot = Try(FXMLLoader.load[Parent](getClass.getResource("/at/fhj/swengb/apps/battleship/jfx/battleshipfx.fxml")))
 // val welcomeScreen = Try(FXMLLoader.load[Parent](getClass.getResource("/at/fhj/swengb/apps/battleship/jfx/welcome_screen.fxml")))

  //val css = "/at/fhj/swengb/apps/battleship/jfx/battleship.css"

  override def start(stage: Stage): Unit = {
  * welcomeScreen match {
  * case Success(root) =>
  *stage.setScene(new Scene(root))
  *stage.show()
  *stage.getScene.getStylesheets.clear()
  *stage.getScene.getStylesheets.add(css)
  *stage.setTitle("BattleShip")
  * case Failure(e) => e.printStackTrace()
  * }
  * }
  * */








object BattleShipFxApp {

  def main(args: Array[String]): Unit = {

    Application.launch(classOf[BattleShipFxApp], args: _*)

  }

  var filename: String = _
  var gameRound: GameRound = _

  private var main: Stage = _
  private var splash: Scene = _
  private var welcome: Scene = _
  private var newGame: Scene = _
  private var editGame: Scene = _
  private var highscore: Scene = _
  private var credits: Scene = _
  private var join: Scene = _
  private var game: Scene = _


  def loadMain: Stage = main
  def loadSplash: Scene = splash
  def loadWelcome: Scene = welcome
  def loadNewGame: Scene = newGame
  def loadEditGame: Scene = editGame
  def loadHighscore: Scene = highscore
  def loadCredits: Scene = credits
  def loadJoin: Scene = join
  def loadGame: Scene = game

  val css = "/at/fhj/swengb/apps/battleship/jfx/battleship.css"



  def loadFxml(): Unit = {
    splash = load("/at/fhj/swengb/apps/battleship/jfx/splash.fxml")
    welcome = load("/at/fhj/swengb/apps/battleship/jfx/welcome_screen.fxml")
    newGame = load("/at/fhj/swengb/apps/battleship/jfx/new_game.fxml")

    highscore = load("/at/fhj/swengb/apps/battleship/jfx/highscore_screen.fxml")
    credits = load("/at/fhj/swengb/apps/battleship/jfx/credit_screen.fxml")

  }

  def loadFxmlEditMode(): Unit = {

    editGame = load("/at/fhj/swengb/apps/battleship/jfx/edit_screen.fxml")
  }
  def loadFxmlGameMode(): Unit = {
    game = load("/at/fhj/swengb/apps/battleship/jfx/game_screen.fxml")
  }

  def loadFxmlJoinMode(): Unit = {
    join = load("/at/fhj/swengb/apps/battleship/jfx/join_screen.fxml")
  }

  private def load(file: String): Scene = {
    val triedRoot = Try(FXMLLoader.load[Parent](getClass.getResource(file)))
    triedRoot match {
      case Success(root) =>
        val scene: Scene = new Scene(root)
        scene.getStylesheets.clear()
        scene.getStylesheets.add(css)
        scene
      case Failure(e) => {
        e.printStackTrace()
        null
      }
    }
  }


  def display(scene: Scene, stage: Stage): Unit = {
    stage.setScene(scene)

    stage.show()
  }

  def setGameRound(game: GameRound): Unit = {
    this.gameRound = game
  }

  def setFilename(fname: String): Unit = {
    this.filename = fname
  }

  /**
    *
    * @return the gameRound which is needed
    */
  def getGameRound(): GameRound = {
    return gameRound
  }

  /**
    *
    * @return
    */
  def getFilename(): String = {
    return filename

  }

  def saveGameState(fname: String): Unit = {
    filename = fname
    convert(gameRound).writeTo(Files.newOutputStream(Paths.get(filename)))
  }

  def loadGameState(fname: String): GameRound = {
    filename = fname
    val reload = BattleShipProtobuf.Game.parseFrom(Files.newInputStream(Paths.get(filename)))
    val gameWithOldValues = GameRound(convert(reload).playerA,
      convert(reload).playerB,
      convert(reload).gameName,
      x=>(),
      convert(reload).battleShipGameA,
      convert(reload).battleShipGameB)

    gameWithOldValues.battleShipGameA.gameState = convert(reload).battleShipGameA.gameState
    gameWithOldValues.battleShipGameB.gameState = convert(reload).battleShipGameB.gameState
    gameWithOldValues.setWinner(reload.getWinner)
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.parse(reload.getStartDate)
    gameWithOldValues.setDate(date)
    gameWithOldValues.setNumberCurrentPlayers(reload.getNumOfShots)
    gameWithOldValues
  }


  class BattleShipFxApp extends Application {


    override def init(): Unit = {
      BattleShipFxApp.loadFxml()
    }


    override def start(stage: Stage): Unit = {
      stage.setTitle("Battleship")
      stage.setResizable(false)
      BattleShipFxApp.main = stage
      BattleShipFxApp.display(BattleShipFxApp.loadSplash,stage)

    }


  }

}