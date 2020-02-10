package model

enum class FieldEvent { OPEN, MARK, DISMARK, EXPLODE, RESTART }

data class Field(val row: Int, val column: Int) {
    private val neighbors = ArrayList<Field>()
    private val callbacks = ArrayList<(Field, FieldEvent) -> Unit>()

    var marked = false
    var openned = false
    var mined = false

    val unmarked: Boolean get() = !marked
    val closed: Boolean get() = !openned
    val safe: Boolean get() = !mined

    val objectiveReached: Boolean get() = safe && openned || mined && marked
    val minedNeighbors: Int get() = neighbors.filter { it.mined }.size
    val safeNeighborhood: Boolean get() = neighbors.map { it.safe }.reduce { result, safe -> result && safe }

    fun addNeighborhood(neighbor: Field) {
        neighbors.add(neighbor)
    }

    fun onEvent(callback: (Field, FieldEvent) -> Unit) {
        callbacks.add(callback)
    }

    fun open() {
        if (closed) {
            openned = true
            if (mined) {
                callbacks.forEach {
                    it(this, FieldEvent.EXPLODE)
                }
            } else {
                callbacks.forEach {
                    it(this, FieldEvent.OPEN)
                    neighbors.filter { it.closed && it.safe && it.safeNeighborhood }.forEach { it.open() }
                }
            }
        }
    }


    fun alterMark() {
        if (closed) {
            marked != marked
            val event = if (marked) FieldEvent.MARK else FieldEvent.DISMARK
            callbacks.forEach { it(this, event) }

        }
    }

    fun setAsMine() {
        mined = true
    }

    fun restart() {
        openned = false
        marked = false
        mined = false
        callbacks.forEach { it(this, FieldEvent.RESTART) }
    }

}