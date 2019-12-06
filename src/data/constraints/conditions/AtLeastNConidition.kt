package data.constraints.conditions

import data.slots.Slot
import velvet.structs.Position

class AtLeastNConidition(private val conditions: List<SlotCondition>,
                         private val minimumMatch: Int) : SlotCondition {

    override val offsets: Set<Position>
        get() = conditions.asSequence().flatMap { it.offsets.asSequence() }.toSet()

    override fun checkCondition(slotGrid: Map<Position, Slot>, position: Position): Boolean {
        return conditions.filter { it.checkCondition(slotGrid, position) }.count() >= minimumMatch
    }
}