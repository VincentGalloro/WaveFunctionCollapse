package data.constraints.conditions

import data.slots.Slot
import velvet.structs.Position

class AllCondition(private val conditions: List<SlotCondition>) : SlotCondition {

    override val offsets: Set<Position>
        get() = conditions.asSequence().flatMap { it.offsets.asSequence() }.toSet()

    override fun checkCondition(slotGrid: Map<Position, Slot>, position: Position): Boolean {
        return conditions.all { it.checkCondition(slotGrid, position) }
    }
}