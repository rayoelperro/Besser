package org.besser.core

import java.net.URL
import java.net.URLClassLoader

/**
 * Created by rayoe on 29/08/2018.
 */
class Loader {

    constructor(path: String, classes: Array<Token<*>>, contexts: MutableList<Context>) {
        val root = URLClassLoader(arrayOf<URL>(URL(path)), this.javaClass.classLoader)
        generator(root, classes, contexts)
    }

    constructor(classes: Array<Token<*>>, contexts: MutableList<Context>) {
        val root = this.javaClass.classLoader
        generator(root, classes, contexts)
    }

    fun generator(root: ClassLoader, classes: Array<Token<*>>, contexts: MutableList<Context>) {
        for (classname in classes) {
            val aclass = Class.forName(classname.value as String, false, root)
            val alem = JavaElem(aclass, aclass.simpleName)
            contexts.last()["@" + alem.name] = Token("ELEMENT", alem)
        }
    }
}