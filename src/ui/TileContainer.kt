package ui

import data.modules.Tile
import data.slots.Slot
import velvet.main.VGraphics
import velvet.structs.Bounds
import velvet.structs.Position
import velvet.structs.ShadowMap
import velvet.structs.Vector
import velvet.velements.container.ActuatedVContainer
import velvet.velements.container.VContainer
import velvet.velements.impl.ImageElement
import velvet.velements.impl.SquareElement
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.ceil
import kotlin.math.sqrt

class TileContainer(private val slotGrid: Map<Position, Slot>,
                    val position: Position,
                    val imgMap : Map<Tile, BufferedImage>) {

    private val squareElement = SquareElement()
    val squareContainer = VContainer(squareElement)

    val subTiles = ShadowMap({ slotGrid[position]?.possibleTiles ?: emptySet() },
            { ActuatedVContainer(ImageElement(imgMap[it])) })

    fun update(){
        subTiles.update()

        val subBound = squareContainer.bounds.scale(Vector(0.7), Vector(0.5))

        val width = ceil(sqrt(subTiles.values.size.toDouble())).toInt()
        val size = subBound.size / width

        if(subTiles.values.size > 1) {
            subTiles.values.forEachIndexed { index, vContainer ->
                vContainer.layoutActuator.targetBounds =
                        Bounds(Position.fromIndex(index, width).vector * size + subBound.start)
                                .resize(size, Vector())
            }
            squareElement.outlineColor = Color.BLACK
        }
        else{
            subTiles.values.forEach {
                it.layoutActuator.targetBounds = squareContainer.bounds//.scale(Vector(0.8), Vector(0.5))
            }
            squareElement.outlineColor = null
        }

        subTiles.values.forEach { it.update() }
    }

    fun render(g: VGraphics){
        squareContainer.render(g)
        subTiles.values.forEach { it.render(g) }
    }
}