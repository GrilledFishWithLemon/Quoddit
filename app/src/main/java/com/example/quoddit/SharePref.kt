package com.example.quoddit

import android.content.Context
import android.content.SharedPreferences

class SharePref(context: Context){
    internal var mySharedPref: SharedPreferences

    init {
        mySharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE)
    }

    fun setNightModeState(state:Boolean?){
        val editor = mySharedPref.edit()
        editor.putBoolean("Night Mode", state!!)
        editor.commit()
    }

    fun loadNightModeState(): Boolean?{
        return mySharedPref.getBoolean("Night Mode", false)
    }

}
