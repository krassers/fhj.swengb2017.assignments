package at.fhj.swengb.apps.battleship.model

case class GameRound(playerA: String,
                     playerB: String,
                     gameName: String,
                     log:String => Unit,
                     battleShipGameA: BattleShipGame,
                     battleShipGameB: BattleShipGame) {

  private var numberCurrentPlayers: Int = _
  private var currentPlayer: String = _

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

  def setNumberCurrentPlayers(number: Int) = this.numberCurrentPlayers = number

  def getNumberCurrentPlayers(): Int = numberCurrentPlayers

  def getCurrentPlayer(): String = currentPlayer

  def setCurrentPlayer(player: String) = this.currentPlayer = player

}
