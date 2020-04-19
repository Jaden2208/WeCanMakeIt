package com.whalez.wecanmakeit.ui.main.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.whalez.wecanmakeit.R
import com.whalez.wecanmakeit.firestore.Group
import kotlinx.android.synthetic.main.my_group_item.view.*

class GroupAdapter(private val type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_MY_GROUP = 0
        const val TYPE_SEARCHED_GROUP = 1
    }

    private var groupList: List<Group> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (type == TYPE_MY_GROUP) {
            val view = inflater.inflate(R.layout.my_group_item, parent, false)
            return MyGroupViewHolder(view)
        } else {
            // my_group_item 다른 레이아웃으로 바꿔주기.
            val view = inflater.inflate(R.layout.my_group_item, parent, false)
            return SearchedGroupViewHolder(view)
        }
    }

    override fun getItemCount(): Int = groupList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val group = groupList[position]
        if (type == TYPE_MY_GROUP) {
            val mHolder = holder as MyGroupViewHolder
            mHolder.groupType.text = group.groupType
            mHolder.groupName.text = group.groupName
        } else {

        }
    }

    inner class MyGroupViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val groupType: TextView = itemView.tv_group_type
        val groupName: TextView = itemView.tv_group_name
//        val groupRecentContent: TextView = itemView.tv_group_recent_content
    }

    inner class SearchedGroupViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

    }

}