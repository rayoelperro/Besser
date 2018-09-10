package org.besser.core

import org.frenegen.core.on

/**
 * Created by rayoe on 03/09/2018.
 */
class OnEvent(parser: Parser, val event: Event) : Doing(parser, parser.levels) {

    private val lines = mutableListOf<Array<Token<*>>>()
    private var deep = 1

    fun exe() {
        {
            levels.add(Context(levels.last(), ContextType.OnEvent))
            levels.last().self = event.instance
            Parser(levels).parse(lines.toTypedArray())
            Context.pour(levels.last(), levels[levels.size - 2])
            levels.removeAt(levels.size - 1)
            Unit
        } on event.innerEvent
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