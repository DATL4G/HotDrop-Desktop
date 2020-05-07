import javafx.application.Platform
import javafx.scene.image.Image
import javafx.scene.input.KeyCombination
import javafx.stage.Stage
import p2p.cu2.connect.CU2Connect
import p2p.cu2.discovery.CU2Discovery
import tornadofx.App
import tornadofx.importStylesheet
import kotlin.system.exitProcess

class MainApplication : App(MainView::class) {

    var cU2Connect: CU2Connect? = null
    var cU2Discovery: CU2Discovery? = null

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

    override fun stop() {
        super.stop()
        cU2Connect?.stopReceiving(true)
        cU2Discovery?.stopDiscovery()
        Platform.exit()
        exitProcess(0)
    }

}