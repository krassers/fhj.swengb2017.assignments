package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.nio.file.{Files, Paths}
import java.util.{Calendar, ResourceBundle}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Scene
import javafx.scene.control.{Label, TextField}

import at.fhj.swengb.apps.battleship.BattleShipProtocol.convert
import at.fhj.swengb.apps.battleship.model._
import at.fhj.swengb.apps.battleship.BattleShipProtocol._

import scala.util.Random

class BattleShipFxNewGame extends Initializable {

 // game config
 // **************************************************************
 val list1: List[String] = List("Battle", "Massacre", "Nice")
 val list2: List[String] = List("of", "at", "in")
 val list3: List[String] = List("Beer", "Bearstards", "Graz")
 // **************************************************************

 // game round is created at "create game"
 var game: GameRound = _

 @FXML private var gameName: TextField = _
 @FXML private var playerA: TextField = _
 @FXML private var playerB: TextField = _

 @FXML
 def back(): Unit = backToHome()
 def next(): Unit = createGame()
 def refresh(): Unit = refreshGameName()

 private var filename: String = _

 //def appendLog(message: String): Unit = log.appendText(message + "\n")

 override def initialize(location: URL, resources: ResourceBundle): Unit = initGame

 private def initGame(): Unit = {
  refreshGameName()
 }

  def createGame(): Unit = {

   // game infos
   val name = gameName.getText
   val player1 = playerA.getText
   val player2 = playerB.getText

   //battlefield
   val cellWidth = 0.0
   val cellHeigh = 0.0

   // TODO: Eingabevalidierung?

   println("Name: " + name + "Player1: " + player1 + "Player2: "+ player2)
   // create a new game and switch screen
   //val fieldA = BattleField(10, 10, Fleet(FleetConfig.EmptyFleet))
   //val fieldB = BattleField(10, 10, Fleet(FleetConfig.EmptyFleet))

   //player = scala.io.StdIn.readLine()
   //BattleShipGame(battleField, getCellWidth, getCellHeight, appendLog, "")
   //val battleGameA = BattleShipGame(fieldA,10,10,"Log",player1)
   //val battleGameB = BattleShipGame(fieldB,10,10,"Log",player2)
   //game =  GameRound(player1,player2,name,"log",battleGameA,battleGameB)


   val field = BattleField(10, 10, Fleet(FleetConfig.EmptyFleet))

   val battlefield: BattleField = BattleField.placeRandomly(field)
   val gameA = BattleShipGame(battlefield, (x: Int) => x.toDouble, (x: Int) => x.toDouble, x => (), player1)
   val gameB = BattleShipGame(battlefield, (x: Int) => x.toDouble, (x: Int) => x.toDouble, x => (), player2)

    game = GameRound(player1, player2, name, x => (), gameA, gameB, player1)
   saveGameState()
   BattleShipFxApp.display(BattleShipFxApp.loadEditGame,BattleShipFxApp.loadMain)
  }

 def saveGameState(): Unit = {    val datetime = Calendar.getInstance().getTime
  val test = datetime.toString.filterNot(x => x.isWhitespace ||  x.equals(':'))
  filename = test
  convert(game).writeTo(Files.newOutputStream(Paths.get("battleship/"+filename+".bin")))

  //appendLog("Saved the game")


 }

  def backToHome(): Unit = {
   BattleShipFxApp.display(BattleShipFxApp.loadWelcome,BattleShipFxApp.loadMain)
  }
 /**
   *
   * @return a random game name with items form list1, list2, list3
   */
 def getRandomGameName(): String = {
  var name = ""
  name = list1(Random.nextInt(list1.size)) + " " + list2(Random.nextInt(list2.size)) + " " + list3(Random.nextInt(list3.size))
  return name
 }

 /**
   *
   */
 def refreshGameName(): Unit = {
  var name =""
  name = getRandomGameName()
  gameName.setText(name)
 }

 /**
   *
   * @return the gameRound which was created the screen before
   */
 def getGameRound(): GameRound = {
  return game
 }


}