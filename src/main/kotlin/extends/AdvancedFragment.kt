package extends

import javafx.scene.Parent
import javafx.scene.layout.AnchorPane
import tornadofx.Fragment

abstract class AdvancedFragment() : Fragment() {
    fun openURL(url: String) {
        app.hostServices.showDocument("https://paypal.me/datlag")
    }

    fun applyMaxSize() {
        AnchorPane.setBottomAnchor(root, 0.0)
        AnchorPane.setLeftAnchor(root, 0.0)
        AnchorPane.setRightAnchor(root, 0.0)
        AnchorPane.setTopAnchor(root, 0.0)
    }

    fun applyZIndex(order: Double = 10.0) {
        root.viewOrder = order
    }
}