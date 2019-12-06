package wfc

import data.constraints.Constraint
import data.slots.Slot
import velvet.structs.Position

class ConstraintApplicator {

    val constraints: MutableList<Constraint> = ArrayList()

    fun applyTo(slotGrid: MutableMap<Position, Slot>, position: Position): Boolean{
        if(slotGrid[position]?.settled != false){ return false }
        val old = slotGrid[position]?.possibleTiles ?: emptySet()
        constraints.forEach { it.applyConstraint(slotGrid, position) }
        return (slotGrid[position]?.possibleTiles ?: emptySet()) != old
    }
}