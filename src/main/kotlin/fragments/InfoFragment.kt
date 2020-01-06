package fragments

import extends.AdvancedFragment
import javafx.fxml.FXML
import javafx.scene.layout.StackPane
import utils.DialogBuilder

class InfoFragment : AdvancedFragment() {
    override val root: StackPane by fxml("/fxml/info_fragment.fxml", true)

    private val dependencyList: ArrayList<String> = ArrayList()
    private val dependencyLinks: ArrayList<String> = ArrayList()

    @FXML
    fun closeFragment() = hideFragment()

    fun openGithub() {
        openURL("https://github.com/DatL4g/HotDrop-Desktop")
    }

    fun showDependencies() {
        DialogBuilder(primaryStage.scene)
            .setTitle("Dependencies")
            .setListItems(dependencyList.toTypedArray(), object: DialogBuilder.ItemClickListener{
                override fun onSelect(item: Int) {
                    openURL(dependencyLinks[item])
                }
            })
            .setPositiveButton("Okay", "#db3236")
            .create()?.show()
    }

    fun showAbout() {
        DialogBuilder(primaryStage.scene)
            .setTitle("About")
            .setMessage("Hello, I'm a 18 years old random Dude who programs some Android Apps, Desktop Applications and Websites.\n\n" +
                    "I'm working in a Team called InteraApps and we publish nearly any of our Projects on Github.\n" +
                    "Feel free to follow or contact us there.\n" +
                    "Please report bugs on GitHub so everyone can easily track status.")
            .setNegativeButton("Privacy Policy", "#db3236", object: DialogBuilder.OnClickListener{
                override fun onClick() {
                    openURL("https://interaapps.de/dsgvo")
                }
            })
            .setPositiveButton("Close", "#db3236")
            .create()?.show()
    }

    init {
        applyMaxSize()
        applyZIndex(8.0)

        dependencyList.add("TornadoFX")
        dependencyList.add("JavaFX")
        dependencyList.add("JFoenix")
        dependencyList.add("Ikonli")

        dependencyLinks.add("https://tornadofx.io")
        dependencyLinks.add("https://openjfx.io")
        dependencyLinks.add("https://github.com/jfoenixadmin/JFoenix")
        dependencyLinks.add("https://github.com/kordamp/ikonli")
    }
}