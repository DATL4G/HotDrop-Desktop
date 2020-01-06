package extends

import javafx.scene.Node
import javafx.scene.layout.Pane
import tornadofx.View
import tornadofx.removeFromParent


abstract class AdvancedView(name: String? = null, icon: Node? = null) : View(name, icon) {
    abstract val fragmentPane: Pane

    fun showFragment(fragment: AdvancedFragment) {
        fragmentPane.add(fragment)
        fragment.root.isFocusTraversable = true
        fragment.root.isDisable = false
    }

    fun hideFragment(fragment: AdvancedFragment) {
        fragmentPane.requestLayout()
        fragment.root.isFocusTraversable = false
        fragment.root.isDisable = true
        fragmentPane.requestFocus()
        fragment.root.removeFromParent()
    }

    fun openURL(url: String) {
        app.hostServices.showDocument(url)
    }
}