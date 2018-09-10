package org.besser.core

import org.frenegen.core.CustomEvent

/**
 * Created by rayoe on 03/09/2018.
 */
class Event(val instance: Instance, repeat: String, until: String) {

    val innerEvent = CustomEvent({
        (instance.run(until, arrayOf<Token<*>>()).value as String).toBoolean()
    }, {
        instance.run(repeat, arrayOf<Token<*>>())
    })
}