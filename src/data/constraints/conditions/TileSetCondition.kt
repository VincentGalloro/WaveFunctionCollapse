package data.constraints.conditions

import data.modules.Tile
import data.slots.Slot
import velvet.structs.Position

abstract class TileSetCondition(private val offset: Position) : SlotCondition {

    override fun checkCondition(slotGrid: Map<Position, Slot>, position: Position): Boolean {
        return slotGrid[position + offset]?.possibleTiles?.let{ checkCondition(it) } ?: false
    }

    abstract fun checkCondition(tiles: Set<Tile>): Boolean
}