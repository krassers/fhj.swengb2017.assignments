package at.fhj.swengb.apps.battleship.jfx



import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.layout.StackPane


class BattleShipFxSplash extends Initializable {

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}
    def splash(): Unit = {
      BattleShipFxApp.display(BattleShipFxApp.loadWelcome, BattleShipFxApp.loadMain)
    }
}