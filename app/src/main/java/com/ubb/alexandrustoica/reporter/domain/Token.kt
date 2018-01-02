package com.ubb.alexandrustoica.reporter.domain

import android.content.Context
import com.ubb.alexandrustoica.reporter.R

data class Token(private val token: String) {
    constructor(context: Context) :
            this(context.getSharedPreferences(
                    context.getString(R.string.preferences_file_key),
                    Context.MODE_PRIVATE).getString("token", ""))
    val value = token
}