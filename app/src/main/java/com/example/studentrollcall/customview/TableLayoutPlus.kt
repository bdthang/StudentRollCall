package com.example.studentrollcall.customview

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setPadding
import com.example.studentrollcall.R
import com.example.studentrollcall.model.Entry
import com.example.studentrollcall.model.User

class TableLayoutPlus : TableLayout {
    lateinit var onItemClickListener: OnItemClickListener
    private val TAG = "TableLayoutPlus"

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context?) : super(context)

    fun drawTable(data: HashMap<User, MutableList<Entry>>) {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)

        data.forEach { (user, entries) ->
            val row = TableRow(context)
            row.isClickable = true
            row.setBackgroundResource(outValue.resourceId)
            row.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

            row.addView(newUserCell(user))

            var currentColumn = 1
            entries.sortedBy { it.session }.forEach { entry ->
                while (entry.session != currentColumn) {
                    row.addView(newCell(" "))
                    currentColumn++
                }
                row.addView(newCell("X", entry))

                currentColumn++
            }

            super.addView(row)
        }
    }

    private fun newCell(s: String, entry: Entry? = null): TextView {
        val newCell = TextView(context)
        newCell.setPadding(16)
        newCell.text = s
        newCell.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        if (entry != null) {
            newCell.setOnClickListener {
                onItemClickListener.onItemClick(entry)
            }
        }
        return newCell
    }

    private fun newUserCell(user: User): TextView {
        val newCell = TextView(context)
        newCell.setPadding(16)
        newCell.text = user.name
        newCell.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        newCell.setOnClickListener {
            onItemClickListener.onUserClick(user)
        }
        return newCell
    }

    fun drawHeader(width: Int) {
        super.removeAllViews()
        val row = TableRow(context)
        row.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        row.addView(newCell(context.getString(R.string.table_header)))

        repeat(width) { index ->
            val num = index + 1
            row.addView(newCell(num.toString()))
        }

        super.addView(row)
    }

    interface OnItemClickListener {
        fun onItemClick(entry: Entry)
        fun onUserClick(user: User)
    }

}