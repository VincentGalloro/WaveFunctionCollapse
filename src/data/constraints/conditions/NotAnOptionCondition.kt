package data.constraints.conditions

import data.modules.Tile
import velvet.structs.Position

class NotAnOptionCondition(offset: Position,
                           private val options: Set<Tile>) : TileSetCondition(offset) {

    override val offsets = setOf(offset)

    override fun checkCondition(tiles: Set<Tile>) = tiles.none { it in options }
}