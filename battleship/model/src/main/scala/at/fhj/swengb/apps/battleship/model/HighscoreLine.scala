package at.fhj.swengb.apps.battleship.model

import java.text.SimpleDateFormat
import java.util.Date
import javafx.beans.property.{SimpleIntegerProperty, SimpleStringProperty}

case class HighscoreL(date: Date, winner: String, gameName: String, numShots: Int)

class HighscoreLine {

  val date: SimpleStringProperty = new SimpleStringProperty()
  val winner: SimpleStringProperty = new SimpleStringProperty()
  val gameName: SimpleStringProperty = new SimpleStringProperty()
  val numOfShots: SimpleIntegerProperty = new SimpleIntegerProperty()

  def setDate(date: Date): Unit = this.date.set(new SimpleDateFormat("yyyy-MM-dd").format(date))

  def setWinner(win: String): Unit = this.winner.set(win)

  def setGameName(name: String): Unit = this.gameName.set(name)

  def setNumOfShots(clicks: Int): Unit = this.numOfShots.set(clicks)

}

object HighscoreLine {
  def apply(hl: HighscoreL): HighscoreLine = {
    val line = new HighscoreLine
    line.setDate(hl.date)
    line.setWinner(hl.winner)
    line.setGameName(hl.gameName)
    line.setNumOfShots(hl.numShots)
    return line

  }
}