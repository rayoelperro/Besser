package org.besser.core

/**
 * Created by rayoe on 31/08/2018.
 */
class WhileLoop(parser: Parser, var condition : Boolean, val noGroupedLine : Array<Token<*>>) : Doing(parser, parser.levels) {

    private val lines = mutableListOf<Array<Token<*>>>()
    private var deep = 1

    fun exe() {
        levels.add(Context(levels.last(), ContextType.WhileLoop))
        Parser(levels).parse(lines.toTypedArray())
        Context.pour(levels.last(), levels[levels.size - 2])
        levels.removeAt(levels.size - 1)
    }

    override fun run(tks: Array<Token<*>>): Token<*> {
        val chs = tks.nameKeywords()
        when {
            chs.lineStructure().joinToString(" ") == makeStructure("END") -> {
                deep--
                if (deep == 0) {
                    if (condition) {
                        exe()
                        while ((Parser(levels).parseLine(noGroupedLine.copyOfRange(1, noGroupedLine.size - 1)).value as String).toBoolean())
                            exe()
                    }
                    action.changeDoing(DoingType.Execute)
                } else {
                    lines += tks
                }
            }
            else -> {
                if (chs.lineStructure().joinToString(" ").endsWith("THEN")) {
                    deep++
                }
                lines += tks
            }
        }
        return Token("RES", "NONE")
    }
}