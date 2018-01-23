package at.fhj.swengb.apps.battleship.model

import java.util.Date

case class GameRound(playerA: String,
                     playerB: String,
                     gameName: String,
                     log:String => Unit,
                     battleShipGameA: BattleShipGame,
                     battleShipGameB: BattleShipGame) {



  private var winner: String = _
  private var numberCurrentPlayers: Int = _
  private var currentPlayer: String = _
  private var numOfShots: Int = _
  private var playDate: Date = _

  /*private val = battleShipGameA = createGame(playerA, getCellWidth, getCellHeight, log)
  private val = battleShipGameB = createGame(playerB, getCellWidth, getCellHeight, log)

  private def createGame(player: String,
                         getCellWidth: Int => Double,
                         getCellHeight: Int => Double,
                         log: String => Unit): BattleShipGame = {


    val field = BattleField(10, 10, Fleet(FleetConfig.Standard))

    val battlefield: BattleField = BattleField.placeRandomly(field)
    BattleShipGame(battlefield, getCellWidth, getCellHeight, log, player)
  }*/

  def resetNumberofShots() = numOfShots = 0

  def setDate(date: Date): Unit = this.playDate = date

  def getDate(): Date = return this.playDate

  def setWinner(winnerName: String): Unit = this.winner = winnerName

  def getWinner(): String = this.winner

  def incNumOfShots(): Unit = numOfShots += 1

  def setNumOfShots(numShots: Int): Unit = this.numOfShots = numShots

  def getNumOfShots(): Int = numOfShots

  def getNumberCurrentPlayers(): Int = numberCurrentPlayers

  def setNumberCurrentPlayers(number: Int) = this.numberCurrentPlayers = number

  def getCurrentPlayer(): String = currentPlayer

  def setCurrentPlayer(player: String) = this.currentPlayer = player

}
