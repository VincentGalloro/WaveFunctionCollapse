package data.slots

import data.modules.Tile

class StaticSlot(tile: Tile) : Slot {

    override val settled = true
    override val possibleTiles = setOf(tile)
}