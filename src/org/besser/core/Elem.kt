package org.besser.core

/**
 * Created by rayoe on 26/08/2018.
 */
open class Elem(val name : String) {
    val fields : MutableMap<String, String> = mutableMapOf()

    val methods : MutableList<Fun> = mutableListOf()

    open fun getInstance(args : Array<Token<*>>) : Instance {
        return Instance(this, args)
    }

    fun getMethod(name: String) : Fun {
        for (m in methods)
            if (m.name == name)
                return m
        TOER("Trying to access to an undeclared method: $name")
    }

}