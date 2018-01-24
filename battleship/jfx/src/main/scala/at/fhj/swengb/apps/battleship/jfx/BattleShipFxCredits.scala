package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Scene


class BattleShipFxCredits extends Initializable {


 @FXML def back(): Unit = {
  BattleShipFxApp.display(BattleShipFxApp.loadWelcome,BattleShipFxApp.loadMain)
 }

 @FXML def openLicense(): Unit = {
  BattleShipFxApp.display(BattleShipFxApp.loadLicense,BattleShipFxApp.loadMain)
 }


 override def initialize(location: URL, resources: ResourceBundle): Unit = {

 }





}