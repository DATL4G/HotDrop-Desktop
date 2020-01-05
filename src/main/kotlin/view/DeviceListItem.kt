package view

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import org.kordamp.ikonli.javafx.FontIcon

class DeviceListItem(private val item: String, private val clickListener: ClickListener): Label() {

    @FXML
    private var deviceFAB: FAB? = null

    @FXML
    private var deviceIcon: FontIcon? = null

    @FXML
    private var deviceName: Label? = null

    private var fxmlLoader = FXMLLoader(javaClass.getResource("/fxml/device_fab.fxml"))

    init {
        fxmlLoader.setController(this)
        graphic = fxmlLoader.load()

        deviceName?.let {
            it.text = item
        }
        deviceFAB?.let {
            it.setOnMouseClicked {
                clickListener.onClick(item)
            }
        }
    }

    interface ClickListener {
        fun onClick(item: String)
    }
}