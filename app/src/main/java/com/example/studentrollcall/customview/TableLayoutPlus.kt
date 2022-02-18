package com.example.studentrollcall.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setPadding
import com.example.studentrollcall.R
import com.example.studentrollcall.model.Entry

class TableLayoutPlus : TableLayout {

    private val TAG = "TableLayoutPlus"

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context?): super(context)

    fun outLineTable(width: Int, height: Int) {

        addHeaderRow(width)
        repeat(height) { addDataRow(width) }
    }

    private fun addHeaderRow(width: Int) {
        val row = TableRow(context)
        row.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        val tvNothing = TextView(context)
        tvNothing.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        tvNothing.setPadding(8)
        tvNothing.text = context.getString(R.string.table_header)
        row.addView(tvNothing)

        repeat(width) { index ->
            val tvNum = TextView(context)
            tvNum.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            val num = index + 1
            tvNum.text = num.toString()
            tvNum.setPadding(8)
            row.addView(tvNum)
        }

        super.addView(row)

    }

    private fun addDataRow(width: Int) {
        val row = TableRow(context)
        row.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        val tvName = TextView(context)
        tvName.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        tvName.setPadding(8)
        row.addView(tvName)

        repeat(width) {
            val tvCheckIcon = TextView(context)
            tvCheckIcon.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            tvCheckIcon.setPadding(8)
            row.addView(tvCheckIcon)
        }

        super.addView(row)
    }

    fun fillData(entryGroup: Map<String, List<Entry>>) {
        var i = 1
        entryGroup.forEach { aStudentEntries ->
            val row: TableRow = this.getChildAt(i) as TableRow

            val tvName: TextView = row.getChildAt(0) as TextView
            tvName.text = aStudentEntries.key

            aStudentEntries.value.forEach {
                val tvCheck: TextView = row.getChildAt(it.session) as TextView
                tvCheck.text = "✓"
            }

            i++
        }

    }

}