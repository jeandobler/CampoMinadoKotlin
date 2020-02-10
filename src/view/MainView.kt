package view

import model.Board
import model.BoardEvent
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

fun main() {
    MainView()
}

class MainView : JFrame() {
    private val board = Board(16, 30, 89)
    private val boardPanel = BoardPanel(board)

    init {
        board.onEvent { this::showResults }
        add(boardPanel)

        setSize(690, 438)
        setLocationRelativeTo(null)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        title = "Campo Minado"
        isVisible = true

    }

    private fun showResults(boardEvent: BoardEvent) {
        SwingUtilities.invokeLater {
            val msg = when (boardEvent) {
                BoardEvent.VICTORY -> "Você Ganhou"
                BoardEvent.DEFEAT -> "Voce Perdeu"
            }
            println(msg)

            JOptionPane.showMessageDialog(this, msg)
            board.restart()

            boardPanel.repaint()
            boardPanel.validate()
        }
    }

}
