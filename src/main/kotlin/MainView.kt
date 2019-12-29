import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import tornadofx.View
import utils.DialogBuilder

class MainView : View("HotDrop") {
    override val root : BorderPane by fxml("/fxml/main.fxml", true)

    @FXML
    fun dialog(mouseEvent: MouseEvent) {
        val dialogBuilder = DialogBuilder(primaryStage.scene)
            .setTitle("Test")
            .setMessage("Message")
            .setPositiveButton("Close", "#000000")
            .create()

        dialogBuilder?.show()
    }
}