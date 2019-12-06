package data.constraints

import data.constraints.conditions.SlotCondition
import data.constraints.restrictions.TileSetRestriction
import data.slots.Slot
import velvet.structs.Position

class Constraint(val condition: SlotCondition,
                 val restriction: TileSetRestriction) {

    fun applyConstraint(slotGrid: MutableMap<Position, Slot>, position: Position) {
        if(condition.checkCondition(slotGrid, position)){
            slotGrid[position] = Slot.createSlot(
                    restriction.applyRestriction(slotGrid[position]?.possibleTiles ?: emptySet()))
        }
    }
}