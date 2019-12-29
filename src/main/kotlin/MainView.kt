import javafx.fxml.FXML
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import tornadofx.View

class MainView : View("HotDrop") {
    override val root : BorderPane by fxml("/fxml/main.fxml", true)


}