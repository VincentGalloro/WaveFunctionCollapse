package data.constraints.restrictions

import data.modules.Tile

class WhitelistRestriction(private val whitelistTiles: Set<Tile>) : TileSetRestriction {

    override fun applyRestriction(tiles: Set<Tile>) = tiles.intersect(whitelistTiles)
}