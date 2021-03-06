package at.fhj.swengb.apps.battleship.jfx

import javafx.scene.control.{Alert, Button, TextArea}
import java.net.URL
import java.nio.file.{Files, Paths}
import java.util.{Calendar, Date, ResourceBundle}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Alert.AlertType
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
  @FXML private var btsave: Button = _
  @FXML private var btload: Button = _
  @FXML private var ownGridPane: GridPane = _

  @FXML private var enemyGridPane: GridPane = _

  @FXML private var log: TextArea = _

  @FXML private var headline: Text = _

  @FXML private var playerTurn: Text = _


  @FXML
  def giveUp(): Unit = {
    //alert(AlertType.INFORMATION,"Information", "Actual game state will be saved! Going to Menu")
    appendLog("G I V E up")
    saveGameState()
    alert(AlertType.INFORMATION, "Give up", "give up - other player has won!")
    gameRound.setNumberCurrentPlayers(gameRound.getNumberCurrentPlayers()-1)
    //gameRound.setWinner(gameRound.playerB+" (give up)")
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
   //println("init: -------- " ++ game.getNumberCurrentPlayers())
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
    //setLabels()
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
      println("--------------------START GAME --------------------- with currPlayer: "+ currentPlayer  + "at SCREEEEEN: " + numberPlayers)
      init(gameRound)
      appendLog("New game started.")
      btsave.setDisable(true)
      enemyGridPane.setDisable(true)
      //loadGameState()
    }
  }

  def setLabels(): Unit = {
    if(numberPlayers==1){
      headline.setText(gameRound.playerA ++ " " ++ "@" ++ gameRound.gameName ++ " " ++ "vs" ++ " " ++ gameRound.playerB)
    } else if(numberPlayers==2) {
      headline.setText(gameRound.playerB ++ " " ++ "@" ++ gameRound.gameName ++ " " ++ "vs" ++ " " ++ gameRound.playerA)
    }
    if(currentPlayer != null) {
      if (currentPlayer.takeRight(1) != "s")
        playerTurn.setText(currentPlayer ++ "'" ++ "s" ++ " " ++ "turn")
      else
        playerTurn.setText(currentPlayer ++ "'" ++ " " ++ "turn")
    }
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
    println("SAVE for " + currentPlayer + "at SCREEN: " + numberPlayers)


    if((currentPlayer == gameRound.playerA && numberPlayers == 1) || (currentPlayer == gameRound.playerB && numberPlayers == 2)){
      // our turn and save possible
      if (currentPlayer == gameRound.playerA) {
        gameRound.setCurrentPlayer(gameRound.playerB)
        currentPlayer = gameRound.playerB
      }
      else {
        gameRound.setCurrentPlayer(gameRound.playerA)
        currentPlayer = gameRound.playerA

      }
      setLabels()
        btsave.setDisable(true)
        enemyGridPane.setDisable(true)

      if(log.getParagraphs.toString.contains("G A M E")){
        // means current player has one
        alert(AlertType.INFORMATION,"Game over","Congratulation, you have won !!!!!!!")
        println("SAVE meth: WINNER:" + currentPlayer)
        gameRound.setWinner(currentPlayer)
        //gameRound.setDate(date)
        BattleShipFxApp.setGameRound(gameRound)
        BattleShipFxApp.setFilename(fileName)
        BattleShipFxApp.saveGameState(fileName)
        BattleShipFxApp.display(BattleShipFxApp.loadWelcome,BattleShipFxApp.loadMain)
      }


      gameRound.setDate(date)
      gameRound.incNumOfShots()
      gameRound.setWinner("")
      BattleShipFxApp.saveGameState(fileName)
      appendLog("Saved the game")
    }
    else{
      println("you can SAVE nothing because its not your turn")
    }

  }
  def getPlayerForScreen(num: Int): String = {
    var help = ""
    if(num == 1){
      help = gameRound.playerA
    } else {
      help = gameRound.playerB
    }
    return  help
  }
  def loadGameState(): Unit = {
      //println("LOAD for " + currentPlayer + "at SCREEN: " + numberPlayers)

      val reload = BattleShipProtobuf.Game.parseFrom(Files.newInputStream(Paths.get(fileName)))
      println("loadGameState: ---------------- " + currentPlayer)
      val gameWithOldValues = GameRound(convert(reload).playerA,
        convert(reload).playerB,
        convert(reload).gameName,
        x => (),
        convert(reload).battleShipGameA,
        convert(reload).battleShipGameB)

      val newGameOldValues = gameWithOldValues.copy(battleShipGameA = gameWithOldValues.battleShipGameA.copy(getCellWidth = this.getCellWidth, getCellHeight = this.getCellHeight, log = appendLog),
        battleShipGameB = gameWithOldValues.battleShipGameB.copy(getCellWidth = this.getCellWidth, getCellHeight = this.getCellHeight, log = appendLog))

      newGameOldValues.battleShipGameA.gameState = convert(reload).battleShipGameA.gameState
      newGameOldValues.battleShipGameB.gameState = convert(reload).battleShipGameB.gameState

      initAfterReload(newGameOldValues)
      newGameOldValues.battleShipGameA.update(gameRound.battleShipGameA.gameState.length)
      newGameOldValues.battleShipGameB.update(gameRound.battleShipGameB.gameState.length)

      println(log.getParagraphs.toString)
      if(log.getParagraphs.toString.contains("G A M E ")){
        // means other player has one
        alert(AlertType.INFORMATION,"Game over","Game is over other Player has won!")
        if(reload.getCurrentPlayer == gameRound.playerA){
          gameRound.setWinner(gameRound.playerB)
          println("WInner" + gameRound.playerB)
        }else {
          gameRound.setWinner(gameRound.playerA)
          println("WInner" + gameRound.playerA)
        }
        gameRound.setCurrentPlayer(reload.getCurrentPlayer)
        BattleShipFxApp.setGameRound(gameRound)
        BattleShipFxApp.setFilename(fileName)
        BattleShipFxApp.saveGameState(fileName)
        BattleShipFxApp.display(BattleShipFxApp.loadWelcome,BattleShipFxApp.loadMain)

      } else if(log.getParagraphs.toString.contains("G I V E")){
        alert(AlertType.INFORMATION,"Game over","Game is over you have won!")
        gameRound.setWinner(reload.getCurrentPlayer)
        gameRound.setCurrentPlayer(reload.getCurrentPlayer)
        BattleShipFxApp.setGameRound(gameRound)
        BattleShipFxApp.setFilename(fileName)
        BattleShipFxApp.saveGameState(fileName)
        BattleShipFxApp.display(BattleShipFxApp.loadWelcome,BattleShipFxApp.loadMain)
      }
      appendLog("Loaded the game")


      println(reload.getCurrentPlayer())
      println(getPlayerForScreen(numberPlayers))
      // wenn screen1 = currentplayer
      if (reload.getCurrentPlayer() == getPlayerForScreen(numberPlayers)) {
        //currentPlayer = newGameOldValues.playerB
        currentPlayer = reload.getCurrentPlayer
        gameRound.setCurrentPlayer(reload.getCurrentPlayer)
        //lockplayer A
        println("NUMPLAYERS:"+numberPlayers)
          println("SAVE for SCREEN 1: enable gridpane")
          enemyGridPane.setDisable(false)
          btsave.setDisable(false)
          //btsave.setDisable(false)
          //btload.setDisable(true)
      } else {
        //currentPlayer = newGameOldValues.playerA
        //gameRound.setCurrentPlayer(currentPlayer)
        //lockplayer B
        //if (numberPlayers == 2) {
          //enemyGridPane.setDisable(false)
          //btsave.setDisable(false)
          //btsave.setDisable(false)
          //btload.setDisable(true
      }
      numShots = reload.getNumOfShots()
      setLabels()
  }
  def alert(alertType: AlertType,title: String, text: String): Unit ={
    val al = new Alert(alertType);
    al.setTitle(title)
    al.setContentText(text)
    al.showAndWait()
  }

}

