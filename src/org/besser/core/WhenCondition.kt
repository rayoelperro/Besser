package org.besser.core

/**
 * Created by rayoe on 31/08/2018.
 */
class WhenCondition(parser: Parser) : Doing(parser, parser.levels) {

    var saved = false
    var saving = false
    var opened = true
    private val lines = mutableListOf<Array<Token<*>>>()
    private var deep = 1

    fun exe() {
        levels.add(Context(levels.last(), ContextType.WhenCondition))
        Parser(levels).parse(lines.toTypedArray())
        Context.pour(levels.last(), levels[levels.size - 2])
        levels.removeAt(levels.size - 1)
    }

    override fun run(tks: Array<Token<*>>): Token<*> {
        val cheks = tks.nameKeywords().nameOperators()
        when {
            cheks.lineStructure().joinToString(" ") == makeStructure("END") -> {
                deep--
                if (deep == 0) {
                    if (lines.isNotEmpty()) {
                        exe()
                    }
                    action.changeDoing(DoingType.Execute)
                } else {
                    if (opened) {
                        TOER("Trying to run code outside any block")
                    }
                    if (saving) {
                        lines += tks
                    }
                }
            }
            cheks.lineStructure().joinToString(" ").startsWith("OR") -> {
                if (deep == 1) {
                    if (opened) {
                        opened = false
                    }
                    val exp = cheks.copyOfRange(1, cheks.size)
                    val rts = Parser(levels).parseLine(exp)
                    if (rts.id != "BOOL")
                        TOER("The expression must return a boolean")
                    if (saving) {
                        saving = false
                        saved = true
                    }
                    if (!saved) {
                        if ((rts.value as String).toBoolean()) {
                            saving = true
                        }
                    }
                } else {
                    if (opened) {
                        TOER("Trying to run code outside any block")
                    }
                    if (saving) {
                        lines += tks
                    }
                }
            }
            else -> {
                if (cheks.lineStructure().joinToString(" ").endsWith("THEN")) {
                    deep++
                }
                if (opened) {
                    TOER("Trying to run code outside any block")
                }
                if (saving) {
                    lines += tks
                }
            }
        }
        return Token("RES", "NONE")
    }
}