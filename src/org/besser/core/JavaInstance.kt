package org.besser.core

import java.util.*

/**
 * Created by rayoe on 29/08/2018.
 */
class JavaInstance : Instance {

    private val jbase = base as JavaElem
    private val innerInstance : Any?

    constructor(base : Elem, args : Array<Token<*>>) : super(base) {
        val parsed = parseToJava(args)
        if(parsed.isNotEmpty()) {
            val con = jbase.class_obj.getConstructor(*getParsedClass(parsed))
            innerInstance = con.newInstance(*parsed)
        } else {
            val con = jbase.class_obj.getConstructor()
            innerInstance = con.newInstance()
        }
    }

    constructor(base : Elem, ins : Any) : super(base) {
        innerInstance = ins
    }

    constructor(base : Elem) : super(base) {
        innerInstance = null
    }

    override fun run(name : String, arg : Array<Token<*>>): Token<*> {
        val mpar = parseToJava(arg)
        return javaToSingle(if(mpar.isNotEmpty()) {
            val meth = jbase.class_obj.getMethod(name, *getParsedClass(mpar))
            meth.invoke(innerInstance, *mpar)
        } else {
            val meth = jbase.class_obj.getMethod(name)
            meth.invoke(innerInstance)
        })
    }

    fun getField(name : String) : Token<*> {
        val fl = jbase.class_obj.getDeclaredField(name)
        fl.isAccessible = true
        return javaToSingle(fl.get(innerInstance))
    }

    fun setField(name : String, value : Token<*>) {
        val fl = jbase.class_obj.getDeclaredField(name)
        fl.isAccessible = true
        fl.set(innerInstance, singleToJava(value))
    }

    fun getParsedClass(parsed : Array<*>) : Array<Class<*>> {
        val m = mutableListOf<Class<*>>()
        for (p in parsed) {
            m += (p as Any).javaClass
        }
        return m.toTypedArray()
    }

    fun parseToJava(args: Array<Token<*>>) : Array<*> {
        val m = mutableListOf<Any?>()
        for (a in args)
            m += singleToJava(a)
        return m.toTypedArray()
    }

    fun javaToSingle(obj: Any?) : Token<*> {
        return when(obj) {
            is Byte, is Short, is Int, is Long, is Float,
            is Double -> Token("NUMBER", obj.toString())
            is String, is Char -> Token("STRING", obj.toString())
            is Boolean -> Token("BOOL", obj.toString())
            is Instance -> Token("INSTANCE", obj)
            is Array<*> -> {
                val mut = mutableListOf<Token<*>>()
                for (i in obj)
                    mut += javaToSingle(i as Any)
                Token("ARRAY", mut.toTypedArray())
            }
            null -> Token("RES", "NONE")
            else -> {
                val cl = obj.javaClass
                Token("INSTANCE", JavaInstance(JavaElem(cl, cl.simpleName), obj))
            }
        }
    }

    fun singleToJava(args: Token<*>) : Any? {
        return when(args.id) {
            "NUMBER" -> (args.value as String).toDouble()
            "STRING" -> (args.value as String)
            "BOOL" -> (args.value as String).toBoolean()
            "INSTANCE" -> {
                if (args.value is JavaInstance){
                    args.value.innerInstance as Any
                }else {
                    args.value as Instance
                }
            }
            "ARRAY" -> {
                val lis = args.value as Array<Token<*>>
                if (lis.isNotEmpty()) {
                    val mut = mutableListOf<Any?>()
                    var tok = lis[0].id
                    for (l in lis) {
                        if (l.id != tok) {
                            tok = "OBJ"
                        }
                        mut += singleToJava(l)
                    }
                    val ta = mut.toTypedArray()
                    when (tok) {
                        "NUMBER" -> ta as Array<Double>
                        "STRING" -> ta as Array<String>
                        "BOOL" -> ta as Array<Boolean>
                        "INSTANCE" -> ta
                        "CHANNEL" -> TOER("Channels can not be used in java")
                        "ARRAY" -> ta as Array<Array<Any>>
                        "JBYTE" -> ta as Array<Byte>
                        "JSHORT" -> ta as Array<Short>
                        "JINT" -> ta as Array<Int>
                        "JLONG" -> ta as Array<Long>
                        "JFLOAT" -> ta as Array<Float>
                        "JDOUBLE" -> ta as Array<Double>
                        else -> ta
                    }
                } else {
                    emptyArray<Any>()
                }
            }
            "CHANNEL" -> TOER("Channels can not be used in java")
            "JBYTE" -> (args.value as String).toDouble().toByte()
            "JSHORT" -> (args.value as String).toDouble().toShort()
            "JINT" -> (args.value as String).toDouble().toInt()
            "JLONG" -> (args.value as String).toDouble().toLong()
            "JFLOAT" -> (args.value as String).toDouble().toFloat()
            "JDOUBLE" -> (args.value as String).toDouble()
            else -> TOER("Wrong shared type: ${args.id}")
        }
    }
}