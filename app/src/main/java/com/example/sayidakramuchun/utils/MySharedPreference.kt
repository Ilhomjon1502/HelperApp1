package com.example.sayidakramuchun.utils

import android.content.Context
import android.content.SharedPreferences

object MySharedPreference {
    private const val NAME = "catch_file_name"
    private const val MODE = Context.MODE_PRIVATE

    private lateinit var preferences: SharedPreferences

    fun init(context: Context){
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation:(SharedPreferences.Editor) -> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var name:String
    get() = preferences.getString("name", "null")!!
    set(value) = preferences.edit{
        if (value!=null){
            it.putString("name", value)
        }
    }

    var number:String
        get() = preferences.getString("number", "null")!!
        set(value) = preferences.edit{
            if (value!=null){
                it.putString("number", value)
            }
        }

    var numberHelp:String
        get() = preferences.getString("numberHelp", "null")!!
        set(value) = preferences.edit{
            if (value!=null){
                it.putString("numberHelp", value)
            }
        }

    var sana:String
        get() = preferences.getString("sana", "null")!!
        set(value) = preferences.edit{
            if (value!=null){
                it.putString("sana", value)
            }
        }
}