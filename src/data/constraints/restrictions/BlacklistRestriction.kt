package data.constraints.restrictions

import data.modules.Tile

class BlacklistRestriction(private val blacklistTiles: Set<Tile>) : TileSetRestriction {

    override fun applyRestriction(tiles: Set<Tile>) = tiles - blacklistTiles
}