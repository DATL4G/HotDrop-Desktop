import fragments.ChooseDeviceFragment
import fragments.SearchFragment
import javafx.fxml.FXML
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import tornadofx.View
import utils.DialogBuilder


class MainView : View("HotDrop") {
    override val root : BorderPane by fxml("/fxml/main.fxml", true)

    private val centerPane: AnchorPane by fxid()
    private val searchFragment = SearchFragment()
    private val chooseDeviceFragment = ChooseDeviceFragment()
    private var settingsArray: Array<String>

    @FXML
    fun settingsDialog() {
        val dialogBuilder = DialogBuilder(primaryStage.scene)
            .setTitle("Settings")
            .setListItems(settingsArray, object: DialogBuilder.ItemClickListener{
                override fun onSelect(item: Int) {
                    when(item) {
                        2 -> openURL("https://paypal.me/datlag")
                    }
                }
            })
            .setPositiveButton("Close", "#db3236")
            .create()

        dialogBuilder?.show()
    }

    @FXML
    fun infoLayout(mouseEvent: MouseEvent) {
        centerPane.children.remove(searchFragment.root)
        centerPane.add(chooseDeviceFragment)
    }

    init {
        centerPane.add(searchFragment)
        settingsArray = arrayOf("Account", "Connectivity", "Donate")
    }

    fun openURL(url: String) {
        app.hostServices.showDocument("https://paypal.me/datlag")
    }
}

