package com.ubb.alexandrustoica.reporter.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.R.layout.row_report
import com.ubb.alexandrustoica.reporter.domain.Report
import java.text.SimpleDateFormat

class ReportsRecyclerAdaptor(
        private val context: Context,
        private var list: List<Report>) :
        RecyclerView.Adapter<ReportsViewHolder>() {

    private var onBottomReachedAction: () -> Unit = { }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReportsViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cell = layoutInflater.inflate(row_report, parent, false)
        return ReportsViewHolder(cell)
    }

    override fun onBindViewHolder(holder: ReportsViewHolder?, position: Int) {
        if (position >= list.size - 1) onBottomReachedAction()
        holder?.itemView?.findViewById<TextView>(R.id.textLabel_rowReport)?.text =
                list[position].text
        holder?.itemView?.findViewById<TextView>(R.id.dateLabel_rowReport)?.text =
                SimpleDateFormat("yyyy-MM-dd").format(list[position].date.time)
        holder?.itemView?.setOnClickListener {
            context.startActivity(Intent(context, ReportActivity::class.java)
                    .also { it.putExtra("id", list[position].id) })
        }
    }

    fun addOnBottomReachedListener(provider: () -> Unit) {
        onBottomReachedAction = provider
    }

    fun updateData(data: List<Report>) {
        list = data
        notifyDataSetChanged()
    }
}
