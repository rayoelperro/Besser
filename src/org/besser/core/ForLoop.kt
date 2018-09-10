package org.besser.core

/**
 * Created by rayoe on 31/08/2018.
 */
class ForLoop(parser : Parser, val variable : String, val arr : Array<Token<*>>) : Doing(parser, parser.levels) {

    private val lines = mutableListOf<Array<Token<*>>>()
    private var deep = 1

    fun save() {
        for (a in arr) {
            levels.add(Context(levels.last(), ContextType.ForLoop))
            levels.last()["@" + variable] = a
            Parser(levels).parse(lines.toTypedArray())
            Context.pour(levels.last(), levels[levels.size - 2])
            levels.removeAt(levels.size - 1)
        }
        action.changeDoing(DoingType.Execute)
    }

    override fun run(tks: Array<Token<*>>): Token<*> {
        val cheks = tks.nameKeywords()
        when {
            cheks.lineStructure().joinToString(" ") == makeStructure("END") -> {
                deep--
                if (deep == 0){
                    save()
                } else {
                    if (cheks.lineStructure().joinToString(" ").endsWith("THEN")) {
                        deep++
                    }
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