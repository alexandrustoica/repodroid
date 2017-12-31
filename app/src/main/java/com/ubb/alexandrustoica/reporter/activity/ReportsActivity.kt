package com.ubb.alexandrustoica.reporter.activity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.domain.AsyncResponse
import com.ubb.alexandrustoica.reporter.task.CallbackAsyncResponse
import com.ubb.alexandrustoica.reporter.task.GetReportsRequestTask

import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.content_reports.*
import kotlinx.android.synthetic.main.row_report.view.*
import java.text.SimpleDateFormat

class ReportsRecyclerAdaptor(private val list: List<Report>) :
        RecyclerView.Adapter<ReportsViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReportsViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cell = layoutInflater.inflate(R.layout.row_report, parent, false)
        return ReportsViewHolder(cell)
    }

    override fun onBindViewHolder(holder: ReportsViewHolder?, position: Int) {
        holder?.itemView?.textLabel_rowReport?.text = list[position].text
        holder?.itemView?.dateLabel_rowReport?.text =
                SimpleDateFormat("yyyy-MM-dd").format(list[position].date.time)
    }

}

class ReportsViewHolder(view: View) :
        RecyclerView.ViewHolder(view)

class ReportsActivity : AppCompatActivity(), CallbackAsyncResponse<List<Report>, String> {

    override fun onTaskCompleted(result: AsyncResponse<List<Report>, String>) {
        result.ifResult { reportsRecyclerView_reports.adapter = ReportsRecyclerAdaptor(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        setSupportActionBar(toolbar)
        val pref = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val token = pref.getString(getString(R.string.token), "")
        GetReportsRequestTask(token,this@ReportsActivity).execute(0)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        reportsRecyclerView_reports.layoutManager = LinearLayoutManager(this@ReportsActivity)
    }
}
