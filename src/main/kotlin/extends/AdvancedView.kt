package extends

import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.layout.Pane
import tornadofx.View
import tornadofx.removeFromParent
import java.lang.Exception


abstract class AdvancedView(name: String? = null, icon: Node? = null) : View(name, icon) {
    abstract val fragmentPane: Pane

    fun showFragment(fragment: AdvancedFragment, pane: Pane = fragmentPane) {
        Platform.runLater{
            try {
                pane.add(fragment)
                fragment.root.isFocusTraversable = true
                fragment.root.isDisable = false
            } catch (e: Exception) {}
        }
    }

    fun hideFragment(fragment: AdvancedFragment) {
        Platform.runLater {
            try {
                fragment.root.isFocusTraversable = false
                fragment.root.isDisable = true
                fragmentPane.requestFocus()
                fragment.root.removeFromParent()
            } catch (e: Exception) {}
        }
    }

    fun openURL(url: String) {
        app.hostServices.showDocument(url)
    }

    val packageName = packageString

    companion object {
        const val packageString = "de.datlag.hotdrop"
    }
}