package ui

import data.modules.Tile
import data.slots.Slot
import velvet.main.VGraphics
import velvet.structs.Bounds
import velvet.structs.Position
import velvet.structs.ShadowMap
import velvet.structs.Vector
import velvet.velements.interact.UIEventHandler
import java.awt.image.BufferedImage

class GridContainer(private val slotGrid: Map<Position, Slot>,
                    private val imgMap: Map<Tile, BufferedImage>,
                    private val uiEventHandler: UIEventHandler) {

    val tiles = ShadowMap({ slotGrid.keys }, { TileContainer(slotGrid, it, imgMap) })

    fun update(){
        tiles.update()

        tiles.values.forEach {
            it.squareContainer.bounds = Bounds(it.position.vector*32 + 100).resize(Vector(32), Vector())
            it.update()
        }
    }

    fun render(g: VGraphics){
        tiles.values.forEach { it.render(g) }
    }
}