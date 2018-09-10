package org.besser.core

/**
 * Created by rayoe on 04/09/2018.
 */
class Isolated(parser: Parser, val array: Array<Token<*>>) : Doing(parser, parser.levels) {

    private val lines = mutableListOf<Array<Token<*>>>()
    private var deep = 1

    fun exe() {
        levels.add(Context(array, ContextType.Block))
        Parser(levels).parse(lines.toTypedArray())
        levels.removeAt(levels.size - 1)
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