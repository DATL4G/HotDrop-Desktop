package fragments

import tornadofx.Fragment
import tornadofx.button
import utils.DialogBuilder

class TestFragment : Fragment() {
    override val root = button("Click Here")

    init {
        root.setOnMouseClicked {
            val dialogBuilder = DialogBuilder(primaryStage.scene)
                .setTitle("Test")
                .setMessage("Message")
                .setPositiveButton("Close", "#000000")
                .create()

            dialogBuilder?.show()
        }
    }
}