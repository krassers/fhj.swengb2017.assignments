package at.fhj.swengb.apps.battleship.model

import java.text.SimpleDateFormat
import java.util.Date
import javafx.beans.property.{SimpleIntegerProperty, SimpleStringProperty}

class HighscoreLine {

    val date: SimpleStringProperty = new SimpleStringProperty()
    val winner: SimpleStringProperty = new SimpleStringProperty()
    val gameName: SimpleStringProperty = new SimpleStringProperty()
    val numOfShots: SimpleIntegerProperty = new SimpleIntegerProperty()

    private var game: GameRound = _

    def setDate(date: Date): Unit = this.date.set(new SimpleDateFormat("yyyy/MM/dd").format(date))

    def setWinner(win: String): Unit = this.winner.set(win)

    def setGameName(name: String): Unit = this.gameName.set (name)

    def setNumOfShots(clicks: Int): Unit = this.numOfShots.set(clicks)

    def setGameRound(gameReplay: GameRound): Unit = {
      game = gameReplay
    }

    def getGameRound: GameRound = return game
  }

  object HighscoreLine {
    def apply(game: GameRound): HighscoreLine = {
      val line = new HighscoreLine
      line.setDate(game.getDate())
      line.setWinner(game.getWinner())
      line.setGameName(game.gameName)
      line.setNumOfShots(game.getNumOfShots())
      line.setGameRound(game)

      return line

    }
}
