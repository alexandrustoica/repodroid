package com.ubb.alexandrustoica.reporter.components

import android.content.Context
import android.support.v7.app.AlertDialog

class BasicAlertDialog(
        private val context: Context,
        private val message: String,
        private val title: String = "Error") :
        ShowProtocol<AlertDialog> {

    override fun show(): AlertDialog =
            AlertDialog.Builder(context)
                    .also { it.setMessage(message) }
                    .also { it.setTitle(title) }
                    .show()
}