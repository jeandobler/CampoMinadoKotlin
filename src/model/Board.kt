package model


enum class BoardEvent { VICTORY, DEFEAT }

class Board(val rowsQuantity: Int, val columnsQuantity: Int, val minesQuantity: Int) {

    private val fields = ArrayList<ArrayList<Field>>()
    private val callbacks = ArrayList<(BoardEvent) -> Unit>()

    init {
        generateFields()
        assocNeighbors()
        sortMines()

    }

    private fun sortMines() {
        val generator = java.util.Random()

        var randomRow = -1
        var randomColumn = -1
        var currentMines = 0

        while (currentMines <= this.minesQuantity) {
            randomRow = generator.nextInt(rowsQuantity)
            randomColumn = generator.nextInt(columnsQuantity)
            val randomField = fields[randomRow][randomColumn]
            if (randomField.safe) {
                randomField.mined = true
                currentMines++
            }

        }
    }

    private fun objectiveCompleted(): Boolean {
        var winner = true
        forEachFields { if (!it.objectiveReached) winner = false }
        return winner
    }

    private fun verifyWinOrLoss(field: Field, event: FieldEvent) {
        if (event == FieldEvent.EXPLODE) {
            callbacks.forEach { it(BoardEvent.DEFEAT) }
        } else {
            callbacks.forEach { it(BoardEvent.VICTORY) }

        }

    }

    private fun assocNeighbors() {
        forEachFields {
            assocNeighbors(it)
        }
    }

    private fun assocNeighbors(field: Field) {

        val (row, column) = field
        val rows = arrayOf(row - 1, row, row + 1)
        val columns = arrayOf(column - 1, column, column + 1)

        rows.forEach { l ->
            columns.forEach { c ->
                val current = fields.getOrNull(l)?.getOrNull(c)
                current?.takeIf { field != it }?.let { field.addNeighborhood(it) }
            }

        }
    }

    private fun generateFields() {
        for (row in 0 until rowsQuantity) {
            fields.add(ArrayList())
            for (column in 0 until columnsQuantity) {
                val newField = Field(row, column)
                newField.onEvent(this::verifyWinOrLoss)
                fields[row].add(newField)

            }
        }
    }

    fun forEachFields(callback: (Field) -> Unit) {
        fields.forEach { rows -> rows.forEach(callback) }
    }

    fun onEvent(callback: (BoardEvent) -> Unit) {
        callbacks.add(callback)
    }

    fun restart() {
        forEachFields { it.restart() }
        sortMines()
    }
}
