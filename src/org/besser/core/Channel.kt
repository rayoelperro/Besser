package org.besser.core

/**
 * Created by rayoe on 05/09/2018.
 */
class Channel(private var value : Token<*>) {
    fun set(nv : Token<*>) {
        value = nv
    }
    fun get() = value
}