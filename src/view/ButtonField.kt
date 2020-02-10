package view

import model.Field
import model.FieldEvent
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingUtilities

private val COLOR_BG_NORMAL = Color(184, 184, 184)
private val COLOR_BG_MARKED = Color(8, 179, 247)
private val COLOR_BG_EXPLOSION = Color(189, 66, 68)
private val COLOR_TXT_GREEN = Color(0, 100, 0)


class ButtonField(private val field: Field) : JButton() {
    init {
        font = font.deriveFont(Font.BOLD)
        background = COLOR_BG_NORMAL
        isOpaque = true
        border = BorderFactory.createBevelBorder(0)
        addMouseListener(MouseClickListener(field, { it.open() }, { it.alterMark() }))

        field.onEvent(this::applyStyle)
    }

    private fun applyStyle(field: Field, fieldEvent: FieldEvent) {
        when (fieldEvent) {
            FieldEvent.EXPLODE -> applyStyleExploded()
            FieldEvent.OPEN -> applyStyleOpened()
            FieldEvent.MARK -> applyStyleMarked()
            else -> applyStyleDefault()
        }
        SwingUtilities.invokeLater() {
            repaint()
            validate()
        }

    }

    private fun applyStyleDefault() {
        background = COLOR_BG_NORMAL
        text = ""
        border = BorderFactory.createBevelBorder(0)
    }

    private fun applyStyleOpened() {
        background = COLOR_BG_NORMAL
        border = BorderFactory.createLineBorder(Color.GRAY)
        foreground = when (field.minedNeighbors) {
            1 -> COLOR_TXT_GREEN
            2 -> Color.BLUE
            3 -> Color.YELLOW
            4, 5, 6 -> Color.RED
            else -> Color.PINK
        }
        text = if (field.minedNeighbors > 0) field.minedNeighbors.toString() else ""
    }

    private fun applyStyleMarked() {
        background = COLOR_BG_MARKED
        text = "M"
        foreground = Color.BLACK
    }

    private fun applyStyleExploded() {
        background = COLOR_BG_EXPLOSION
        text = "X"
    }


}