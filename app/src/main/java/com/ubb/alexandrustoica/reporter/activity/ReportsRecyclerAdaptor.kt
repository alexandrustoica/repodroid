package com.ubb.alexandrustoica.reporter.activity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.R.layout.row_report
import com.ubb.alexandrustoica.reporter.domain.Report
import java.text.SimpleDateFormat

class ReportsRecyclerAdaptor(private val list: List<Report>) :
        RecyclerView.Adapter<ReportsViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReportsViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cell = layoutInflater.inflate(row_report, parent, false)
        return ReportsViewHolder(cell)
    }

    override fun onBindViewHolder(holder: ReportsViewHolder?, position: Int) {
        holder?.itemView?.findViewById<TextView>(R.id.textLabel_rowReport)?.text =
                list[position].text
        holder?.itemView?.findViewById<TextView>(R.id.dateLabel_rowReport)?.text =
                SimpleDateFormat("yyyy-MM-dd").format(list[position].date.time)
    }
}
