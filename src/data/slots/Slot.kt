package data.slots

import data.modules.Tile

interface Slot {

    companion object{
        fun createSlot(tiles: Set<Tile>) : Slot{
            if(tiles.size == 1){ return StaticSlot(tiles.first()) }
            return FluxSlot(tiles)
        }
    }

    val settled: Boolean

    val possibleTiles: Set<Tile>
}