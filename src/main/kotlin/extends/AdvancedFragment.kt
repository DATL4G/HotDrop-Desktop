package extends

import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.layout.AnchorPane
import tornadofx.Fragment
import tornadofx.removeFromParent
import java.lang.Exception

abstract class AdvancedFragment(name: String? = null, icon: Node? = null) : Fragment(name, icon) {
    fun openURL(url: String) {
        app.hostServices.showDocument(url)
    }

    fun applyMaxSize() {
        AnchorPane.setBottomAnchor(this.root, 0.0)
        AnchorPane.setLeftAnchor(this.root, 0.0)
        AnchorPane.setRightAnchor(this.root, 0.0)
        AnchorPane.setTopAnchor(this.root, 0.0)
    }

    fun applyZIndex(order: Double = 10.0) {
        root.viewOrder = order
    }

    fun hideFragment() {
        Platform.runLater {
            try {
                this.root.isFocusTraversable = false
                this.root.isDisable = true
                this.root.parent.requestFocus()
                this.root.removeFromParent()
            } catch (e: Exception) {}
        }
    }
}