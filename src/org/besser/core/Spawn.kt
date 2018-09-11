package org.besser.core

/**
 * Created by rayoe on 11/09/2018.
 */
class Spawn(action : Parser) : Doing(action, action.levels) {
    companion object {
        fun spawnFun(function : Fun, args: Array<Token<*>>, instance: Instance? = null, yield_fun : Fun? = null) {
            Thread({
                function.exec(args, instance, yield_fun)
            }).start()
        }
    }

    private val lines = mutableListOf<Array<Token<*>>>()
    private var deep = 1

    fun exe() {
        Thread({
            val nlevels = mutableListOf(Context(levels.last(), ContextType.Spawn))
            Parser(nlevels).parse(lines.toTypedArray())
            nlevels.removeAt(levels.size - 1)
        }).start()
        action.changeDoing(DoingType.Execute)
    }

    override fun run(tks: Array<Token<*>>): Token<*> {
        val cheks = tks.nameKeywords()
        when {
            cheks.lineStructure().joinToString(" ") == makeStructure("END") -> {
                deep--
                if (deep == 0){
                    exe()
                } else {
                    lines += tks
                }
            }
            else -> {
                if (cheks.lineStructure().joinToString(" ").endsWith("THEN")) {
                    deep++
                }
                lines += tks
            }
        }
        return Token("RES", "NONE")
    }
}