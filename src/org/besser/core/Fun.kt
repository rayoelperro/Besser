package org.besser.core

/**
 * Created by rayoe on 26/08/2018.
 */
open class Fun(val name : String, val context : MutableList<Context>, val scon : Context) {

    val lines: MutableList<Array<Token<*>>> = mutableListOf()
    var end_var: String? = null

    open fun exec(args: Array<Token<*>>, instance: Instance? = null, yield_fun : Fun? = null): Token<*> {
        context.add(Context(scon, ContextType.Function))
        context.last().self = instance
        context.last().funyield = yield_fun
        for (arg in args) {
            context.last().arguments.add(arg)
        }
        Parser(context).parse(lines.toTypedArray())
        val tr = if(end_var == null)
            Token("RES", "NONE")
        else
            context.last()[end_var as String] as Token<*>
        Context.pour(context.last(), scon)
        context.removeAt(context.size - 1)
        return tr
    }
}