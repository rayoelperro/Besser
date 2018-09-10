package org.besser.core

/**
 * Created by rayoe on 29/08/2018.
 */
class JavaElem(val class_obj : Class<*>, name: String) : Elem(name) {

    override fun getInstance(args : Array<Token<*>>) : Instance {
        return JavaInstance(this, args)
    }

    fun getField(name : String) : Token<*> {
        return JavaInstance(this).getField(name)
    }

    fun setField(name : String, value : Token<*>) {
        JavaInstance(this).setField(name, value)
    }

    fun run(name : String, arg : Array<Token<*>>) : Token<*>{
        return JavaInstance(this).run(name, arg)
    }
}