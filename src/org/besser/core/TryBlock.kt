package org.besser.core

/**
 * Created by rayoe on 04/09/2018.
 */
class TryBlock(parser : Parser) : Doing(parser, parser.levels) {

    var except = false
    private val trylines = mutableListOf<Array<Token<*>>>()
    private val exceptlines = mutableListOf<Array<Token<*>>>()
    private var deep = 1

    fun exe() {
        val lsize = levels.size
        try {
            levels.add(Context(levels.last(), ContextType.Try))
            Parser(levels).parse(trylines.toTypedArray()) // This is where the error could raise
            Context.pour(levels.last(), levels[levels.size - 2])
            levels.removeAt(levels.size - 1)
        } catch (e: Throwable) {
            if (levels.size > lsize) {
                Context.pour(levels.last(), levels[levels.size - 2])
                levels.removeAt(levels.size - 1)
            }
            val cntx = Context(levels.last(), ContextType.Except)
            cntx.arguments.add(Token("STRING", e.toString()))
            levels.add(cntx)
            Parser(levels).parse(exceptlines.toTypedArray())
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
                    exe()
                } else {
                    if (except) {
                        exceptlines += tks
                    } else {
                        trylines += tks
                    }
                }
            }
            cheks.lineStructure().joinToString(" ") == makeStructure("EXCEPT") -> {
                if (except) {
                    TOER("More than one except statement")
                }
                except = true
            }
            else -> {
                if (cheks.lineStructure().joinToString(" ").endsWith("THEN")) {
                    deep++
                }
                if (except) {
                    exceptlines += tks
                } else {
                    trylines += tks
                }
            }
        }
        return Token("RES", "NONE")
    }
}