package org.besser.core

/**
 * Created by rayoe on 24/08/2018.
 */
class Context : LinkedHashMap<String, Token<*>> {
    val type : ContextType
    var arguments : MutableList<Token<*>>
    var self : Instance? = null
    var noGroupedLine : Array<Token<*>>? = null
    var funyield : Fun? = null

    companion object {
        fun pour(a : Context, b : Context) {
            for (e in a){
                if (b.containsKey(e.key))
                    b[e.key] = e.value
            }
        }
    }

    constructor(args: Array<String>): super(){
        type = ContextType.Global
        arguments = mutableListOf()
        for (arg in args)
            arguments.add(Token("STRING", arg))
    }

    constructor(args: Array<Token<*>>, type: ContextType): super(){
        this.type = type
        arguments = mutableListOf()
        for (arg in args)
            arguments.add(arg)
    }

    constructor(upper : Context, type : ContextType): super(){
        funyield = upper.funyield
        for (tok in upper)
            this[tok.key] = tok.value
        this.type = type
        arguments = mutableListOf()
    }
}