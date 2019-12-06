package data.constraints.conditions

import data.slots.Slot
import velvet.structs.Position

interface SlotCondition {

    val offsets: Set<Position>

    fun checkCondition(slotGrid: Map<Position, Slot>, position: Position): Boolean
}