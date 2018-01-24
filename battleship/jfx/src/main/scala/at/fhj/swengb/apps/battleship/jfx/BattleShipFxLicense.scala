package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.ScrollPane
import javafx.scene.text.{Text, TextFlow}

import scala.io.Source



class BattleShipFxLicense extends Initializable {

 @FXML private var licenseText: TextFlow = _
 //@FXML private val scroll: ScrollPane = _

 @FXML def back(): Unit = {
  BattleShipFxApp.display(BattleShipFxApp.loadWelcome,BattleShipFxApp.loadMain)
 }


 override def initialize(location: URL, resources: ResourceBundle): Unit =
  {
   //scroll.setStyle("-fx-background: rgb(80,80,80);")
   val file = Source.fromFile("battleship/license.txt")
   val license = file.getLines.mkString
   file.close()
   val text: Text = new Text(license)
   licenseText.getChildren.addAll(text)
  }





}