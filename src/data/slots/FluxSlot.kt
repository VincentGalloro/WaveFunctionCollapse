package data.slots

import data.modules.Tile

class FluxSlot(override val possibleTiles: Set<Tile>) : Slot {

    override val settled = false
}