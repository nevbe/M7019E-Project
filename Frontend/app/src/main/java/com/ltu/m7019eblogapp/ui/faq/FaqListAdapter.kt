package com.ltu.m7019eblogapp.ui.faq

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.ltu.m7019eblogapp.R


class FaqListAdapter(private val context: Context) : BaseExpandableListAdapter() {


    private val questionsList : List<String> = FaqData().getData().keys.toList()
    private val answersMap : HashMap<String, String> = FaqData().getData()

    override fun getGroupCount(): Int {
        return questionsList.size
    }

    override fun getChildrenCount(p0: Int): Int {
        return 1 // 1 answer per question :)
    }

    override fun getGroup(p0: Int): Any {
        return questionsList[p0]
    }

    override fun getChild(p0: Int, p1: Int): Any {
        return answersMap[questionsList[p0]]!!
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return 0 // 1 child per group
    }

    override fun hasStableIds(): Boolean {
        return false //TODO: Is this true?
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(parent?.context)
            view = inflater.inflate(R.layout.fragment_faq_list_item, parent, false)
        }

        // Set the question text for the group
        val questionTextView = view?.findViewById<TextView>(R.id.faq_list_item)
        questionTextView?.text = questionsList[groupPosition]

        return view!!

    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(parent?.context)
            view = inflater.inflate(R.layout.fragment_faq_list_item, parent, false)
        }

        // Set the answer text for the child
        val answerTextView = view?.findViewById<TextView>(R.id.faq_list_item)
        val question = questionsList[groupPosition]
        val answer = answersMap[question]
        answerTextView?.text = answer

        return view!!
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return false //TODO: Test :)
    }

}