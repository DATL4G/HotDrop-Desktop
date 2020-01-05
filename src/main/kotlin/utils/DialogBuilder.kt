package utils

import com.jfoenix.animation.alert.JFXAlertAnimation
import com.jfoenix.controls.JFXAlert
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXDialogLayout
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.paint.Paint
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import tornadofx.add

class DialogBuilder internal constructor(scene: Scene) {

    var title: String? = null
    var message: String? = null
    var customView: Node? = null
    var positiveButton: JFXButton? = null
    var negativeButton: JFXButton? = null
    private var window: Window? = null
    private var jfxDialogLayout: JFXDialogLayout? = null
    private var positiveButtonPaint: Paint? = null
    private var negativeButtonPaint: Paint? = null
    private var alert: JFXAlert<String>? = null

    init {
        window = scene.window
    }

    fun setTitle(title: String): DialogBuilder {
        this.title = title
        return this
    }

    fun setMessage(message: String): DialogBuilder {
        this.message = message
        return this
    }

    fun setView(node: Node): DialogBuilder {
        this.customView = node
        return this
    }

    fun setPositiveButton(buttonText: String, color: String? = null, listener: OnClickListener? = null): DialogBuilder {
        color?.let {
            positiveButtonPaint = Paint.valueOf(it)
        }

        positiveButton = JFXButton(buttonText)
        positiveButton?.let {
            it.isDefaultButton = true
            it.textFill = positiveButtonPaint
            it.buttonType = JFXButton.ButtonType.FLAT
            it.setOnAction {
                alert?.hideWithAnimation()
                listener?.onClick()
            }
        }
        return this
    }

    fun setNegativeButton(buttonText: String, color: String?, listener: OnClickListener?): DialogBuilder {
        color?.let {
            negativeButtonPaint = Paint.valueOf(it)
        }

        negativeButton = JFXButton(buttonText)
        negativeButton?.let {
            it.isCancelButton = true
            it.textFill = negativeButtonPaint
            it.buttonType = JFXButton.ButtonType.FLAT
            it.setOnAction {
                alert?.hideWithAnimation()
                listener?.onClick()
            }
        }
        return this
    }

    fun create(): JFXAlert<String>? {
        alert = JFXAlert(window as Stage?)
        alert?.let {
            it.initModality(Modality.APPLICATION_MODAL)
            it.isOverlayClose = false
            it.animation = JFXAlertAnimation.BOTTOM_ANIMATION
        }

        jfxDialogLayout = JFXDialogLayout()
        jfxDialogLayout?.let {
            it.setHeading(Label(title))
            if(message != null && customView != null) {
                val vBox = VBox(Label(message))
                vBox.add(customView!!)
                it.setBody(vBox)
            } else if(customView != null) {
                it.setBody(customView)
            } else {
                it.setBody(VBox(Label(message)))
            }

            if(positiveButton != null && negativeButton != null) {
                it.setActions(negativeButton, positiveButton)
            } else {
                if(positiveButton != null) {
                    it.setActions(positiveButton)
                } else if (negativeButton != null) {
                    it.setActions(negativeButton)
                }
            }
        }

        alert?.setContent(jfxDialogLayout)
        return alert
    }

    fun show() {
        alert?.showAndWait()
    }

    interface OnClickListener{
        fun onClick()
    }
}