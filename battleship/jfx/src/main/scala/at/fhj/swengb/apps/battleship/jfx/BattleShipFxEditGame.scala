package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Scene
import javafx.scene.control.Alert.AlertType
import javafx.scene.control._
import javafx.scene.layout.GridPane
import javafx.scene.text.{Font, Text}

import at.fhj.swengb.apps.battleship.model._
import at.fhj.swengb.apps.battleship.jfx.BattleShipFxNewGame

class BattleShipFxEditGame extends Initializable {



  @FXML private var playerInfo: Text = _
  @FXML private var startPosX: TextField = _
  @FXML private var startPosY: TextField = _

  @FXML private var ships: ToggleGroup = _
  @FXML private var actShip: RadioButton = _
  @FXML private var alignment: ToggleGroup = _
  @FXML private var actAligment: ToggleButton = _

  @FXML
  private var battleGroundGridPane: GridPane = _

  private def getCellHeight(y: Int): Double = battleGroundGridPane.getRowConstraints.get(y).getPrefHeight

  private def getCellWidth(x: Int): Double = battleGroundGridPane.getColumnConstraints.get(x).getPrefWidth

  var game: GameRound = _
  var playerGame: BattleShipGame = _
  var filename: String = _
  var dir: Direction = _

  var checksum: Int = _


  @FXML
  def back(): Unit = backToHome()

  def startGame(): Unit = createGame()

  def placeShip(): Unit = placeShipOnField()

  def deleteShip(): Unit = deleteShipOnField()

  private def initGame(): Unit = {
    if (BattleShipFxApp.getGameRound() != null) {
      println("DEBUG: now we can load/edit it because we have a game")
      game = BattleShipFxApp.getGameRound()
      filename = BattleShipFxApp.getFilename()
      loadGameRoundForPlayer()

    }
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = initGame


  def loadGameRoundForPlayer(): Unit = {
    println("first load")
    println(game.getNumberCurrentPlayers())
    println(game.getCurrentPlayer())
    if (game.getNumberCurrentPlayers() != null) {
      if (game.getNumberCurrentPlayers() == 1) {
        // display edit mode for player
        playerInfo.setText(game.playerA + " @" + game.gameName + " vs " + game.playerB)
        playerGame = game.battleShipGameA
      } else if (game.getNumberCurrentPlayers() == 2) {
        // display for 2nd player
        playerInfo.setText(game.playerB + " @" + game.gameName + " vs " + game.playerA)
        playerGame = game.battleShipGameB
      }
    }

  }

  def createGame(): Unit = {

    // check if all vessels are placed:
    if(checksum > 18){
      //is ok
      println(game.battleShipGameA.gameState)
      println(game)
      BattleShipFxApp.setGameRound(game)
      BattleShipFxApp.setFilename(filename)
      BattleShipFxApp.saveGameState(filename)
      BattleShipFxApp.loadFxmlGameMode()
      BattleShipFxApp.display(BattleShipFxApp.loadGame, BattleShipFxApp.loadMain)
    }else {
      println("please place all vessels!")
    }

  }

  def backToHome(): Unit = {
    BattleShipFxApp.display(BattleShipFxApp.loadNewGame, BattleShipFxApp.loadMain)
  }

  def deleteShipOnField(): Unit = {

    this.ships.getSelectedToggle match {
      case t1: RadioButton => actShip = t1
      case _ => throw new ClassCastException
    }

    this.alignment.getSelectedToggle match {
      case t1: ToggleButton => actAligment = t1
      case _ => throw new ClassCastException
    }

    //println(actShip.getText)
    println(actAligment.getText)
    //toggle.getProperties[]
  }

  def isValidNum(str: String): Boolean = {
    var valid: Boolean = true
    if(str.isEmpty){
      return false
    }
    try{
      if(str.toInt > 10){
        return false
      }
    }catch{
      case e: NumberFormatException => return false
      case _ => return false
    }
    return valid
  }

  def placeShipOnField(): Unit = {
    try {
      this.ships.getSelectedToggle match {
        case t1: RadioButton => actShip = t1
        case _ => throw new ClassCastException
      }

      this.alignment.getSelectedToggle match {
        case t1: ToggleButton => actAligment = t1
        case _ => throw new ClassCastException
      }
    }catch{
      case e: NullPointerException => println("check your inputs")
      case e: ClassCastException => println("strange failure")
      case _ => println("smth else happend")
    }

      if (!this.isValidNum(startPosX.getText()) || !this.isValidNum(startPosY.getText())) {
        println("emtpy coords")
        val al = new Alert(AlertType.INFORMATION);
        al.setTitle("Error")
        al.setContentText("Enter valid Coords!")
        al.showAndWait()
      } else {
        // Coords are valid (Syntax)
        // actShip.setStyle()
        var pos = BattlePos(startPosX.getText().toInt, startPosY.getText().toInt)

        if(actAligment.getText.equals("Vertical")){
          dir = Vertical
        }else if(actAligment.getText.equals("Horizontal")){
          dir = Horizontal
        }
        var name = NonEmptyString(actShip.getText.split(' ').head)
        var len = actShip.getId.toInt
        var v = Vessel(name, pos, dir, len)
        //println("len: " + len)
        if(len > 28){
          println("Ship already placed - choose another ship!")
        }
        else{
          println("DEBUG: add new " + v.name + " at:" + pos.x + "/" + pos.y + " dir:" + actAligment.getText + " len:" +len)
          var newfield = playerGame.battleField.addAtPosition(v, pos)

          // check if there are changes
          if(newfield != playerGame.battleField){
            if(game.playerA.equals(playerGame.player)){
              //update field no1
              println("player 1")
              game = game.copy(battleShipGameA = BattleShipGame(newfield, getCellWidth,getCellHeight, x => (),game.playerA))
              game.setCurrentPlayer(game.playerA)
              //println("DADADSDS: " + game.getCurrentPlayer() + game.playerB.toString + game.gameName + game.playerA)

              game.battleShipGameA.update(game.battleShipGameA.gameState.length)
              // also update playerGame
              playerGame = game.battleShipGameA

            }else if(game.playerB.equals(playerGame.player)){
              println("player 2")
              game = game.copy(battleShipGameB = BattleShipGame(newfield, getCellWidth,getCellHeight, x => (),game.playerB))
              game.setCurrentPlayer(game.playerA)
              game.battleShipGameB.update(game.battleShipGameB.gameState.length)

              // also update player game
              playerGame = game.battleShipGameB
            }
            BattleShipFxApp.setGameRound(game)
            BattleShipFxApp.saveGameState(filename)

            // set id > 28 --> that we know if the ship was already set
            checksum += len
            actShip.setId(actShip.getId+"9")
          } else {
            println("Ship cannot be placed there!")
          }


        }
    }
  }
}