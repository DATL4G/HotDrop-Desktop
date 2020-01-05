import com.jfoenix.controls.JFXListView
import fragments.TestFragment
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import tornadofx.View
import tornadofx.label
import utils.DialogBuilder


class MainView : View("HotDrop") {
    override val root : BorderPane by fxml("/fxml/main.fxml", true)

    private val centerPane: AnchorPane by fxid()
    private val testFragment = TestFragment()
    private var listView: JFXListView<Label>

    @FXML
    fun settingsDialog() {
        val dialogBuilder = DialogBuilder(primaryStage.scene)
            .setTitle("Settings")
            .setView(listView)
            .setPositiveButton("Close", "#db3236")
            .create()

        dialogBuilder?.show()
    }

    @FXML
    fun infoLayout(mouseEvent: MouseEvent) {
        centerPane.children.remove(testFragment.root)
    }

    init {
        centerPane.add(testFragment)

        listView = JFXListView()
        listView.items.add(label("Account"))
        listView.items.add(label("Connectivity"))
        listView.items.add(label("Donate"))
    }
}

