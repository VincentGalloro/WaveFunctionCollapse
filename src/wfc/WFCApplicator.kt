package wfc

import data.slots.Slot
import data.slots.StaticSlot
import ui.GridContainer
import velvet.structs.Position
import velvet.velements.impl.SquareElement
import java.awt.Color

class WFCApplicator(private val slotGrid: MutableMap<Position, Slot>,
                    private val constraintApplicator: ConstraintApplicator,
                    private val gridContainer: GridContainer? = null) {

    private val unprocessed: MutableSet<Position> = HashSet()

    fun addUnprocessed(pos: Position){
        unprocessed.add(pos)
        (gridContainer?.tiles?.get(pos)?.squareContainer?.vElement as? SquareElement)?.fillColor = Color(100,100,100)
    }
    private fun popRandomUnprocessed(): Position{
        val pos = unprocessed.random()
        unprocessed.remove(pos)
        (gridContainer?.tiles?.get(pos)?.squareContainer?.vElement as? SquareElement)?.fillColor = null
        return pos
    }

    init{
        slotGrid.keys.forEach { addUnprocessed(it) }
    }

    fun step(){
        val neighbours = constraintApplicator.constraints.flatMap { it.condition.offsets }

        if(unprocessed.isEmpty()){
            slotGrid.keys
                    .filter { slotGrid[it]?.settled == false }
                    .shuffled()
                    .mapNotNull { slotGrid[it]?.possibleTiles?.to(it) }
                    .minBy { it.first.size }?.let {
                        if (it.first.isEmpty()) {
                            slotGrid.remove(it.second)
                        } else {
                            slotGrid[it.second] = StaticSlot(it.first.random())
                        }
                        neighbours.map { offs -> it.second + offs }.forEach { addUnprocessed(it) }
                    }
            return
        }

        val processPosition = popRandomUnprocessed()

        val changed = constraintApplicator.applyTo(slotGrid, processPosition)

        if(changed){ neighbours.map { it + processPosition }.forEach { addUnprocessed(it) } }
    }
}