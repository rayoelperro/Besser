package org.besser.core

/**
 * Created by rayoe on 27/08/2018.
 */
open class Instance {

    val base : Elem
    val properties : MutableMap<String, Token<*>>

    constructor(base: Elem) {
        this.base = base
        properties = mutableMapOf()
    }

    constructor(base : Elem, arg : Array<Token<*>>) {
        this.base = base
        properties = mutableMapOf()
        if (arg.size != base.fields.size)
            TOER("Wrong number of shared arguments")
        var plu = 0
        for (i in base.fields){
            if (i.value == arg[plu].id)
                properties[i.key] = arg[plu]
            else
                TOER("Wrong type(${i.value} != ${arg[plu].id}) in element: $plu")
            plu++
        }
    }

    open fun run(name : String, arg: Array<Token<*>>) : Token<*> {
        return base.getMethod(name).exec(arg, this)
    }
}