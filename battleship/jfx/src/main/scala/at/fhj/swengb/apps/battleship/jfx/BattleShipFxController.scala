/*package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.util.{Calendar, ResourceBundle}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.TextArea
import javafx.scene.layout.GridPane
import java.nio.file.{Files, Paths}

import at.fhj.swengb.apps.battleship.model._
import at.fhj.swengb.apps.battleship.BattleShipProtobuf
import at.fhj.swengb.apps.battleship.BattleShipProtocol._



class BattleShipFxController extends Initializable {



  @FXML
  private var battleGroundGridPane: GridPane = _

  var battleshipGame: BattleShipGame = _
  var gameRound: GameRound = _

  @FXML
  private var log: TextArea = _

  @FXML
  def newGame(): Unit = initGame()

  def saveGame(): Unit = saveGameState()

  def loadGame(): Unit = loadGameState()

  override def initialize(url: URL, rb: ResourceBundle): Unit = initGame()

  private def getCellHeight(y: Int): Double = battleGroundGridPane.getRowConstraints.get(y).getPrefHeight

  private def getCellWidth(x: Int): Double = battleGroundGridPane.getColumnConstraints.get(x).getPrefWidth

  def appendLog(message: String): Unit = log.appendText(message + "\n")

  private var filename: String = _

  /**
    * Create a new game.
    *
    * This means
    *
    * - resetting all cells to 'empty' state
    * - placing your ships at random on the battleground
    *
    */
  /*def init(game: BattleShipGame): Unit = {
    battleshipGame = game
    battleGroundGridPane.getChildren.clear()
    for (c <- game.getCells) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
    }
    game.getCells().foreach(c => c.init)
  }*/

  def init(game: GameRound): Unit = {
    gameRound = game
    battleGroundGridPane.getChildren.clear()
    for (c <- game.getGameA.getCells) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
    }
      game.getGameA.getCells().foreach(c => c.init)
  }


  private def initGame(): Unit = {
    val field = BattleField(10, 10, Fleet(FleetConfig.Standard))

    val battlefield: BattleField = BattleField.placeRandomly(field)
    val gameA = BattleShipGame(battlefield, getCellWidth, getCellHeight, appendLog, "PlayerA")
    val gameB = BattleShipGame(battlefield, getCellWidth, getCellHeight, appendLog, "PlayerB")

    val game: GameRound = GameRound("PlayerA", "PlayerB", "Battle of Bearstards", appendLog, gameA, gameB, )
    init(game)
    appendLog("New game started.")
  }


  def saveGameState(): Unit = {    val datetime = Calendar.getInstance().getTime
    val test = datetime.toString.filterNot(x => x.isWhitespace ||  x.equals(':'))
    filename = test
    convert(gameRound).writeTo(Files.newOutputStream(Paths.get("battleship/"+filename+".bin")))

    appendLog("Saved the game")


  }

  /*def loadGameState(): Unit = {

    val reload = BattleShipProtobuf.Game.BattleShipGame.parseFrom(Files.newInputStream(Paths.get("battleship/"+filename+".bin")))

    val gameWithOldValues = BattleShipGame(convert(reload).battleField, getCellWidth, getCellHeight, appendLog, "")
    gameWithOldValues.gameState = convert(reload).gameState
    init(gameWithOldValues)
    gameWithOldValues.update(battleshipGame.gameState.length)
    appendLog("Loaded the game")


  }*/
  def loadGameState(): Unit = {
    val reload = BattleShipProtobuf.Game.parseFrom(Files.newInputStream(Paths.get("battleship/"+filename+".bin")))

    val gameWithOldValues = GameRound(gameRound.playerA,
                                      gameRound.playerB,
                                      gameRound.gameName,
                                      appendLog,
                                      convert(reload).getGameA,
                                      convert(reload).getGameA)

    gameWithOldValues.getGameA.gameState = convert(reload).getGameA.gameState
    //gameWithOldValues.battleShipGameB.gameState = convert(reload).battleShipGameB.gameState
    init(gameWithOldValues)
    gameWithOldValues.getGameA.update(gameRound.getGameA.gameState.length)
    //gameWithOldValues.battleShipGameB.update(gameRound.battleShipGameB.gameState.length)
    appendLog("Loaded the game")
  }
}*/







