import velvet.main.VGraphics
import velvet.main.Velvet
import velvet.structs.Position
import velvet.structs.ShadowMap

class Level : Velvet(Position(1500,832)) {

    private lateinit var waveFunctionCollapse: WaveFunctionCollapse

    override fun init() {
        waveFunctionCollapse = WaveFunctionCollapse(uiEventHandler)
    }

    override fun update() {
        waveFunctionCollapse.update(mouse)
    }

    override fun render(g: VGraphics) {
        waveFunctionCollapse.render(g)
    }

    override fun onClose() {
    }
}

fun main(){
    Velvet.start(Level(), "WFC")
}