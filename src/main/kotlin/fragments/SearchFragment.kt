package fragments

import extends.AdvancedFragment
import javafx.scene.layout.StackPane

class SearchFragment : AdvancedFragment() {
    override val root: StackPane by fxml("/fxml/search_fragment.fxml", true)

    init {
        applyMaxSize()
    }
}