package org.mjstudio.ggonggang.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import org.mjstudio.gfree.domain.enumerator.Major
import org.mjstudio.ggonggang.databinding.SimpleSpinnerItemBinding

class SearchSpinnerAdapter(private val context: Context) : BaseAdapter() {

    val strAdapter = Major.StringAdapter(context.resources)

    val items = Major.getMajorsForSearchFilter().map { it.code }

    fun getIndexWithItemName(name : String) : Int = items.indexOf(name)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if(convertView==null) {
            val v = SimpleSpinnerItemBinding.inflate(LayoutInflater.from(context),parent,false)
            v.textViewSpinnerItem.text = strAdapter.toUi(items[position])
            return v.root
        }
        return convertView
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return items.size
    }
}
