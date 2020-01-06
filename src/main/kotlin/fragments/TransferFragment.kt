package fragments

import extends.AdvancedFragment
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import utils.Device

class TransferFragment(device: Device) : AdvancedFragment() {
    override val root: StackPane by fxml("/fxml/transfer_fragment.fxml", true)

    private val hostName: Label by fxid()

    init {
        applyMaxSize()
        applyZIndex()

        hostName.text = device.name
    }
}