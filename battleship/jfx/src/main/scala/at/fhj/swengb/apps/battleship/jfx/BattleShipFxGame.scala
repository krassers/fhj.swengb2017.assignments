package at.fhj.swengb.apps.battleship.jfx

import javafx.scene.control.TextArea
import java.net.URL
import java.nio.file.{Files, Paths}
import java.util.{Calendar, Date, ResourceBundle}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.layout.GridPane
import javafx.scene.text.Text

import at.fhj.swengb.apps.battleship.BattleShipProtobuf
import at.fhj.swengb.apps.battleship.BattleShipProtocol.convert
import at.fhj.swengb.apps.battleship.model._


class BattleShipFxGame extends Initializable {


  private var gameRound: GameRound = _
  private var fileName: String = _
  private var numberPlayers: Int = _
  private var currentPlayer: String = _
  private var date: Date = _
  private var numShots: Int = _
  /*private var newBsGameA: BattleShipGame = _
  private var newBsGameB: BattleShipGame = _*/

  @FXML private var ownGridPane: GridPane = _

  @FXML private var enemyGridPane: GridPane = _

  @FXML private var log: TextArea = _

  @FXML private var headline: Text = _

  @FXML private var playerTurn: Text = _

  @FXML
  def giveUp(): Unit = {
    //alert(AlertType.INFORMATION,"Information", "Actual game state will be saved! Going to Menu")
    gameRound.setNumberCurrentPlayers(gameRound.getNumberCurrentPlayers()-1)
    gameRound.setWinner(gameRound.playerB+" (give up)")
    BattleShipFxApp.setGameRound(gameRound)
    BattleShipFxApp.setFilename(fileName)
    BattleShipFxApp.saveGameState(fileName)
    BattleShipFxApp.display(BattleShipFxApp.loadWelcome,BattleShipFxApp.loadMain)
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = initGame()

  private def getCellHeight(y: Int): Double = {
    ownGridPane.getRowConstraints.get(y).getPrefHeight
    enemyGridPane.getRowConstraints.get(y).getPrefHeight
  }

  private def getCellWidth(x: Int): Double = {
    ownGridPane.getColumnConstraints.get(x).getPrefWidth
    enemyGridPane.getColumnConstraints.get(x).getPrefWidth
  }

  def appendLog(message: String): Unit = log.appendText(message + "\n")



  def save(): Unit = saveGameState()

  def load(): Unit = loadGameState()

  /**
    * Create a new game.
    *
    * This means
    *
    * - resetting all cells to 'empty' state
    * - placing your ships at random on the battleground
    *
    */

  // TODO Hide enemy battlefield
  // TODO Implement GameState

  def init(game: GameRound): Unit = {

    setLabels()
    val newBsGameA = game.battleShipGameA.copy(getCellWidth = this.getCellWidth, getCellHeight = this.getCellHeight, log = appendLog)
    val newBsGameB = game.battleShipGameB.copy(getCellWidth = this.getCellWidth, getCellHeight = this.getCellHeight, log = appendLog)

    val round = game.copy(battleShipGameA = newBsGameA, battleShipGameB = newBsGameB)
    round.setDate(date)
    round.setNumOfShots(numShots)
    round.setWinner("")
    BattleShipFxApp.setGameRound(round)
    gameRound = BattleShipFxApp.getGameRound()

    ownGridPane.setDisable(true)
    println("init: -------- " ++ game.getNumberCurrentPlayers.toString)
    if(game.getNumberCurrentPlayers() == 1) {
      ownGridPane.getChildren.clear()
      for (c <- newBsGameA.getCells) {
        ownGridPane.add(c, c.pos.x, c.pos.y)
      }
      newBsGameA.getCells().foreach(c => c.init)

      enemyGridPane.getChildren.clear()
      for (c <- newBsGameB.getCells) {
        enemyGridPane.add(c, c.pos.x, c.pos.y)
      }
      newBsGameB.getCells().foreach(c => c.init)
    }
    else if(game.getNumberCurrentPlayers() == 2) {
      ownGridPane.getChildren.clear()
      for (c <- newBsGameB.getCells) {
        ownGridPane.add(c, c.pos.x, c.pos.y)
      }
      newBsGameB.getCells().foreach(c => c.init)


      enemyGridPane.getChildren.clear()
      for (c <- newBsGameA.getCells) {
        enemyGridPane.add(c, c.pos.x, c.pos.y)
      }
      newBsGameA.getCells().foreach(c => c.init)
    }
  }

  def initAfterReload(game: GameRound): Unit = {
    setLabels()
    game.setDate(date)
    game.setNumOfShots(numShots)
    game.setWinner("")

    BattleShipFxApp.setGameRound(game)
    gameRound = game

    ownGridPane.setDisable(true)

    println("initAfterReload ----------- " ++ numberPlayers.toString)
    if(numberPlayers == 1) {
      ownGridPane.getChildren.clear()
      for (c <- game.battleShipGameA.getCells) {
        ownGridPane.add(c, c.pos.x, c.pos.y)
      }
      game.battleShipGameA.getCells().foreach(c => c.init)


      enemyGridPane.getChildren.clear()
      for (c <- game.battleShipGameB.getCells) {
        enemyGridPane.add(c, c.pos.x, c.pos.y)
      }
      //game.battleShipGameB.getCells().foreach(c => c.init)
    }
    else if(numberPlayers == 2) {
      ownGridPane.getChildren.clear()
      for (c <- game.battleShipGameB.getCells) {
        ownGridPane.add(c, c.pos.x, c.pos.y)
      }
      game.battleShipGameB.getCells().foreach(c => c.init)


      enemyGridPane.getChildren.clear()
      for (c <- game.battleShipGameA.getCells) {
        enemyGridPane.add(c, c.pos.x, c.pos.y)
      }
      //game.battleShipGameA.getCells().foreach(c => c.init)
    }
  }

  private def initGame(): Unit = {
    if(BattleShipFxApp.getGameRound() != null) {

      gameRound = BattleShipFxApp.getGameRound()
      fileName = BattleShipFxApp.getFilename()
      numberPlayers = gameRound.getNumberCurrentPlayers()
      gameRound.setCurrentPlayer(gameRound.playerA)
      currentPlayer = gameRound.getCurrentPlayer()
      date = gameRound.getDate()
      numShots = 10
      println(currentPlayer + " " + gameRound)
      init(gameRound)
      appendLog("New game started.")
      //loadGameState()
    }
  }

  def setLabels(): Unit = {
    if(numberPlayers==1){
      headline.setText(gameRound.playerA ++ " " ++ "@" ++ gameRound.gameName ++ " " ++ "vs" ++ " " ++ gameRound.playerB)
    } else if(numberPlayers==2) {
      headline.setText(gameRound.playerB ++ " " ++ "@" ++ gameRound.gameName ++ " " ++ "vs" ++ " " ++ gameRound.playerA)
    }

      if(currentPlayer.takeRight(1) != "s")
        playerTurn.setText(currentPlayer ++ "'" ++ "s" ++ " " ++ "turn")
      else
        playerTurn.setText(currentPlayer ++ "'" ++ " " ++ "turn")
  }

  /*def saveGameState(): Unit = {
   val datetime = Calendar.getInstance().getTime
   val test = datetime.toString.filterNot(x => x.isWhitespace ||  x.equals(':'))
   filename = "battleship"
   convert(gameRound).writeTo(Files.newOutputStream(Paths.get("battleship/"+filename+".bin")))

   appendLog("Saved the game")

  }

  def loadGameState(): Unit = {
   val reload = BattleShipProtobuf.Game.parseFrom(Files.newInputStream(Paths.get("battleship/battleship.bin")))

   val gameWithOldValues = GameRound(convert(reload).playerA,
     convert(reload).playerB,
     convert(reload).gameName,
     appendLog,
     convert(reload).getGameA,
     convert(reload).getGameA,
     convert(reload).numberCurrentPlayers,
     convert(reload).currentPlayer)

   gameWithOldValues.getGameA.gameState = convert(reload).getGameA.gameState
   gameWithOldValues.battleShipGameB.gameState = convert(reload).battleShipGameB.gameState
   init(gameWithOldValues)
   gameWithOldValues.getGameA.update(gameRound.getGameA.gameState.length)
   gameWithOldValues.battleShipGameB.update(gameRound.battleShipGameB.gameState.length)
   appendLog("Loaded the game")
  }*/

  def saveGameState(): Unit = {
    if(currentPlayer == gameRound.playerA) {
      gameRound.setCurrentPlayer(gameRound.playerB)
      currentPlayer = gameRound.playerB
    }
    else {
      gameRound.setCurrentPlayer(gameRound.playerA)
      currentPlayer = gameRound.playerA

    }
    setLabels()

    gameRound.setDate(date)
    gameRound.incNumOfShots()
    gameRound.setWinner("")
    BattleShipFxApp.saveGameState(fileName)
    appendLog("Saved the game")
  }

  def loadGameState(): Unit = {

    val reload = BattleShipProtobuf.Game.parseFrom(Files.newInputStream(Paths.get(fileName)))
    println("loadGameState: ---------------- " ++ fileName)
    val gameWithOldValues = GameRound(convert(reload).playerA,
      convert(reload).playerB,
      convert(reload).gameName,
      x=>(),
      convert(reload).battleShipGameA,
      convert(reload).battleShipGameB)

    val newGameOldValues = gameWithOldValues.copy(battleShipGameA = gameWithOldValues.battleShipGameA.copy(getCellWidth = this.getCellWidth, getCellHeight = this.getCellHeight, log = appendLog),
      battleShipGameB = gameWithOldValues.battleShipGameB.copy(getCellWidth = this.getCellWidth, getCellHeight = this.getCellHeight, log = appendLog))

    newGameOldValues.battleShipGameA.gameState = convert(reload).battleShipGameA.gameState
    newGameOldValues.battleShipGameB.gameState = convert(reload).battleShipGameB.gameState

    initAfterReload(newGameOldValues)
    newGameOldValues.battleShipGameA.update(gameRound.battleShipGameA.gameState.length)
    newGameOldValues.battleShipGameB.update(gameRound.battleShipGameB.gameState.length)
    appendLog("Loaded the game")
    if(currentPlayer == reload.getPlayerA){
      currentPlayer = reload.getPlayerB
    }else{
      currentPlayer = reload.getPlayerA
    }
    numShots = reload.getNumOfShots()
    setLabels()
  }


}

