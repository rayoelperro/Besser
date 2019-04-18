package org.besser.core

/**
 * Created by rayoe on 06/09/2018.
 */
class RepeatLoop(parser : Parser, val times : Int, val variable : String? = null) : Doing(parser, parser.levels) {

    private val lines = mutableListOf<Array<Token<*>>>()
    private var deep = 1

    fun save() {
        for (i in 1..times) {
            levels.add(Context(levels.last(), ContextType.RepeatLoop))
            if (variable != null)
                levels.last()["@" + variable] = Token("NUMBER", i.toString())
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