package org.besser.core

/**
 * Created by rayoe on 30/08/2018.
 */
class IfCondition(parser : Parser, val condition : Boolean) : Doing(parser, parser.levels) {

    var pass_else = false
    private val lines = mutableListOf<Array<Token<*>>>()
    private var deep = 1

    fun exe() {
        levels.add(Context(levels.last(), ContextType.IfCondition))
        Parser(levels).parse(lines.toTypedArray())
        Context.pour(levels.last(), levels[levels.size - 2])
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
                    if (condition xor pass_else) {
                        lines += tks
                    }
                }
            }
            cheks.lineStructure().joinToString(" ") == makeStructure("ELSE") -> {
                if (deep == 1) {
                    if (pass_else) {
                        TOER("More than one else statement")
                    }
                    pass_else = true
                }
            }
            else -> {
                if (cheks.lineStructure().joinToString(" ").endsWith("THEN")) {
                    deep++
                }
                if (condition xor pass_else) {
                    lines += tks
                }
            }
        }
        return Token("RES", "NONE")
    }
}