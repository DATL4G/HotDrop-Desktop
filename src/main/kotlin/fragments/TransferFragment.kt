package fragments

import extends.AdvancedFragment
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import p2p.cu2.model.Host
import utils.Device

class TransferFragment(host: Host) : AdvancedFragment() {
    override val root: StackPane by fxml("/fxml/transfer_fragment.fxml", true)

    private val hostName: Label by fxid()

    init {
        applyMaxSize()
        applyZIndex()

        hostName.text = host.name
    }
}