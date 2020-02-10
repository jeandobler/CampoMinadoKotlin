package view

import javafx.scene.input.MouseButton
import model.Field
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

class MouseClickListener(
    private val field: Field,
    private val onLeftClick: (Field) -> Unit,
    private val onRightClick: (Field) -> Unit
) : MouseListener {

    override fun mousePressed(e: MouseEvent?) {
        when (e?.button) {
            MouseEvent.BUTTON1 -> onLeftClick(field)
            MouseEvent.BUTTON3 -> onRightClick(field)
            MouseEvent.BUTTON2 -> onRightClick(field)

        }
    }

    override fun mouseReleased(p0: MouseEvent?) {}
    override fun mouseEntered(p0: MouseEvent?) {}
    override fun mouseClicked(p0: MouseEvent?) {}
    override fun mouseExited(p0: MouseEvent?) {}

}
