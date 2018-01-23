package at.fhj.swengb.apps.battleship.jfx

import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util
import java.util.{Calendar, Date, ResourceBundle}
import javafx.beans.property.{SimpleIntegerProperty, SimpleStringProperty}
import javafx.beans.value.ObservableValue
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Scene
import javafx.scene.control.{TableColumn, TableView}
import javafx.util.Callback

import at.fhj.swengb.apps.battleship.jfx.DataSource.getFiles
import at.fhj.swengb.apps.battleship.model.{GameRound, HighscoreL, HighscoreLine}


class BattleShipFxHighscore extends Initializable {

  import JfxUtils._

  type HighscoreLineTC[T] = TableColumn[HighscoreLine, T]

  @FXML private var tableViewHighscore: TableView[HighscoreLine] = _

  //@FXML private var btReplay: Button = _

  @FXML private var colDate: HighscoreLineTC[String] = _
  @FXML private var colWinner: HighscoreLineTC[String] = _
  @FXML private var colGameName: HighscoreLineTC[String] = _
  @FXML private var colNumOfMoves: HighscoreLineTC[Int] = _
  //@FXML private var colReplay: Button = _

  val lines = mkObservableList(DataSource.names.map(HighscoreLine(_)))

    /**
      * Change Scene to Game scene
      */
    @FXML def back(): Unit = {
      BattleShipFxApp.display(BattleShipFxApp.loadWelcome,BattleShipFxApp.loadMain)
    }

  def initTableViewColumn[T]: (HighscoreLineTC[T], (HighscoreLine) => Any) => Unit =
    initTableViewColumnCellValueFactory[HighscoreLine, T]

  override def initialize(location: URL, resources: ResourceBundle): Unit = {

    tableViewHighscore.setItems(lines)

    initTableViewColumn[String](colDate, _.date)
    initTableViewColumn[String](colWinner, _.winner)
    initTableViewColumn[String](colGameName, _.gameName)
    initTableViewColumn[Int](colNumOfMoves,_.numOfShots)


    println(DataSource.getFiles())

  }
}

/**
  * util functions to bridge the javafx / scala gap
  */
object JfxUtils {

  type TCDF[S, T] = TableColumn.CellDataFeatures[S, T]

  import scala.collection.JavaConversions._

  def mkObservableList[T](collection: Iterable[T]): ObservableList[T] = {
    FXCollections.observableList(new util.ArrayList[T](collection))
  }

  private def mkCellValueFactory[S, T](fn: TCDF[S, T] => ObservableValue[T]): Callback[TCDF[S, T], ObservableValue[T]] = {
    new Callback[TCDF[S, T], ObservableValue[T]] {
      def call(cdf: TCDF[S, T]): ObservableValue[T] = fn(cdf)
    }
  }

  def initTableViewColumnCellValueFactory[S, T](tc: TableColumn[S, T], f: S => Any): Unit = {
    tc.setCellValueFactory(mkCellValueFactory(cdf => f(cdf.getValue).asInstanceOf[ObservableValue[T]]))
  }

}

object DataSource {

  def getFiles(): List[File] = {
    val d = new File("battleship")
    if (d.exists && d.isDirectory)
      d.listFiles.filter(x => x.isFile && x.getName.contains(".bin")).toList
    else
      List[File]()
  }
  print("LALALLALALL:")
  println(getFiles().map(x => (x.getName, BattleShipFxApp.loadGameState("battleship/"+x.getName).getDate())))
  println()
  val names = getFiles().map {
    (x => HighscoreL(BattleShipFxApp.loadGameState("battleship/"+x.getName).getDate(),
                     BattleShipFxApp.loadGameState("battleship/"+x.getName).getWinner(),
                     BattleShipFxApp.loadGameState("battleship/"+x.getName).gameName,
                     BattleShipFxApp.loadGameState("battleship/"+x.getName).getNumOfShots() ))
  }
  val data =
    (0 to getFiles().size) map {
      case i => HighscoreL(Calendar.getInstance().getTime(),i.toString,"sdf",5)
    }
}



