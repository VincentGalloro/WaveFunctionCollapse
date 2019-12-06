package data.constraints.restrictions

import data.modules.Tile

interface TileSetRestriction {

    fun applyRestriction(tiles: Set<Tile>): Set<Tile>
}