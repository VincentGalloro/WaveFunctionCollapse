import data.constraints.Constraint
import data.constraints.conditions.NotAnOptionCondition
import data.constraints.conditions.OnlyOptionCondition
import data.constraints.restrictions.BlacklistRestriction
import data.constraints.restrictions.WhitelistRestriction
import data.modules.Tile
import wfc.ConstraintApplicator
import data.slots.FluxSlot
import data.slots.Slot
import ui.GridContainer
import velvet.main.Mouse
import velvet.main.VGraphics
import velvet.main.game.graphics.SpriteSheet
import velvet.structs.Position
import velvet.velements.interact.UIEventHandler
import wfc.WFCApplicator
import java.awt.Color
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


//This class is only intended for testing / proof of concept and is not intended to be used in the final product

class WaveFunctionCollapse(uiEventHandler: UIEventHandler) {

    private val slotGrid: MutableMap<Position, Slot> = HashMap()
    private val constraintApplicator = ConstraintApplicator()
    private val wfcApplicator: WFCApplicator

    private val gridContainer: GridContainer
    private val tiles = List(26) { Tile() }

    init{
        val sprites = SpriteSheet(ImageIO.read(File("res/tiles.png")), Position(16,16))

        val imgMap: MutableMap<Tile, BufferedImage> = HashMap()
        for(i in 0 until 7){ imgMap[tiles[i]] = sprites.getImage(Position(i,0)) }
        for(i in 0 until 7){ imgMap[tiles[i + 7]] = sprites.getImage(Position(i,1)) }
        for(i in 0 until 4){ imgMap[tiles[i + 14]] = sprites.getImage(Position(i,2)) }
        for(i in 0 until 4){ imgMap[tiles[i + 18]] = sprites.getImage(Position(i,3)) }
        for(i in 0 until 4){ imgMap[tiles[i + 22]] = sprites.getImage(Position(i,4)) }

        Position(10,10).gridIterate { slotGrid[it] = FluxSlot(tiles.toSet()) }

        val tilesByPath = List(2) {
            listOf(setOf(tiles[1 + it*7], tiles[3 + it*7], tiles[6 + it*7]),
                    setOf(tiles[2 + it*7], tiles[3 + it*7], tiles[4 + it*7]),
                    setOf(tiles[1 + it*7], tiles[4 + it*7], tiles[5 + it*7]),
                    setOf(tiles[2 + it*7], tiles[5 + it*7], tiles[6 + it*7]))}
        //grass,dirt,grass/dirt (clockwise), dirt/grass
        val tilesByMat: List<List<Set<Tile>>> = listOf(
                listOf(tiles.subList(0,7).toSet() + setOf(tiles[15], tiles[19], tiles[20]),
                        tiles.subList(0,7).toSet() + setOf(tiles[16], tiles[20], tiles[21]),
                        tiles.subList(0,7).toSet() + setOf(tiles[17], tiles[21], tiles[18]),
                        tiles.subList(0,7).toSet() + setOf(tiles[14], tiles[18], tiles[19])),
                listOf(tiles.subList(7,14).toSet() + setOf(tiles[17], tiles[23], tiles[24]),
                        tiles.subList(7,14).toSet() + setOf(tiles[14], tiles[24], tiles[25]),
                        tiles.subList(7,14).toSet() + setOf(tiles[15], tiles[25], tiles[22]),
                        tiles.subList(7,14).toSet() + setOf(tiles[16], tiles[22], tiles[23])),
                listOf(setOf(tiles[14], tiles[18], tiles[25]),
                        setOf(tiles[15], tiles[19], tiles[22]),
                        setOf(tiles[16], tiles[20], tiles[23]),
                        setOf(tiles[17], tiles[21], tiles[24])),
                listOf(setOf(tiles[16], tiles[21], tiles[22]),
                        setOf(tiles[17], tiles[18], tiles[23]),
                        setOf(tiles[14], tiles[19], tiles[24]),
                        setOf(tiles[15], tiles[20], tiles[25])))


        constraintApplicator.constraints.let {
            for (i in 0 until 4) {
                for(j in 0 until 2) {
                    it.add(Constraint(
                            OnlyOptionCondition(Position.DIRS[i], tilesByPath[j][(i + 2) % 4]),
                            WhitelistRestriction(tilesByPath[j][i])))
                    it.add(Constraint(
                            NotAnOptionCondition(Position.DIRS[i], tilesByPath[j][(i + 2) % 4]),
                            BlacklistRestriction(tilesByPath[j][i])))
                }
                for(j in 0 until 2){
                    it.add(Constraint(
                            OnlyOptionCondition(Position.DIRS[i], tilesByMat[j][(i + 2) % 4]),
                            WhitelistRestriction(tilesByMat[j][i])))
                    it.add(Constraint(
                            NotAnOptionCondition(Position.DIRS[i], tilesByMat[j][(i + 2) % 4]),
                            BlacklistRestriction(tilesByMat[j][i])))
                }
                for(j in 0 until 2){
                    it.add(Constraint(
                            OnlyOptionCondition(Position.DIRS[i], tilesByMat[j+2][(i + 2) % 4]),
                            WhitelistRestriction(tilesByMat[3-j][i])))
                    it.add(Constraint(
                            NotAnOptionCondition(Position.DIRS[i], tilesByMat[j+2][(i + 2) % 4]),
                            BlacklistRestriction(tilesByMat[3-j][i])))
                }
            }
        }

        gridContainer = GridContainer(slotGrid, imgMap, uiEventHandler)
        wfcApplicator = WFCApplicator(slotGrid, constraintApplicator, gridContainer)
    }

    var expand: Position? = null
    var gSize: Int = 5
    fun update(mouse: Mouse){
        gSize -= mouse.scrollAmount

        expand = ((mouse.pos-100)/(32*gSize)).floor().position

        if(mouse.isPressed(Mouse.LEFT)){
            expand?.let { expand ->
                Position(gSize,gSize).gridIterate {
                    slotGrid[it + expand * gSize] = FluxSlot(tiles.toSet())
                }
            }
        }

        gridContainer.update()

        if(mouse.isPressed(Mouse.LEFT)){
            expand?.let { expand ->
                Position(gSize,gSize).gridIterate {
                    wfcApplicator.addUnprocessed(it + expand*gSize)
                }
            }
        }

        for(i in 0 until 10){
            wfcApplicator.step()
        }
    }

    fun render(g: VGraphics){
        gridContainer.render(g)

        g.setColor(Color.red)
        expand?.let {
            g.draw(Rectangle(it.x*32*gSize + 100, it.y*32*gSize + 100, 32 * gSize, 32 * gSize))
        }
    }
}