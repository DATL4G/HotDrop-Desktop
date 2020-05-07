package fragments

import extends.AdvancedFragment
import javafx.scene.layout.StackPane
import p2p.DiscoverHost
import view.FAB

class SearchFragment : AdvancedFragment() {
    override val root: StackPane by fxml("/fxml/search_fragment.fxml", true)
    private val fabSearch: FAB by fxid()

    private var search = false

    init {
        applyMaxSize()
        applyZIndex()

        fabSearch.setOnMouseClicked { setSearch(!search) }
    }

    fun setSearch(search: Boolean) {
        this.search = search
        if(this.search) {
            //start animation
            discoverHost.startDiscovery()
        } else {
            //stop animation
            discoverHost.stopDiscovery()
        }
    }

    companion object {
        private lateinit var discoverHost: DiscoverHost

        fun newInstance(discoverHost: DiscoverHost): SearchFragment {
            Companion.discoverHost = discoverHost
            return SearchFragment()
        }
    }
}