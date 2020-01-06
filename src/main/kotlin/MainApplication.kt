import javafx.scene.image.Image
import javafx.scene.input.KeyCombination
import javafx.stage.Stage
import tornadofx.App
import tornadofx.importStylesheet

class MainApplication : App(MainView::class) {

    override fun start(stage: Stage) {
        stageSetup(stage)
        importStylesheet("/css/style.css")
        super.start(stage)
    }

    private fun stageSetup(stage: Stage) {
        stage.icons += Image("/images/ic_launcher.png")
        stage.minHeight = 500.0
        stage.minWidth = 700.0
        stage.isAlwaysOnTop = false
        stage.isMaximized = false
        stage.isFullScreen = false
        stage.fullScreenExitHint = null
        stage.fullScreenExitKeyCombination = KeyCombination.NO_MATCH
        stage.isResizable = true
    }

}