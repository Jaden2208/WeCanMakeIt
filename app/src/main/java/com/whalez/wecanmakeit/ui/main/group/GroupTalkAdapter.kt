package com.whalez.wecanmakeit.ui.main.group

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.whalez.wecanmakeit.R
import com.whalez.wecanmakeit.firestore.FirestoreViewModel
import com.whalez.wecanmakeit.firestore.GroupTalk
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.group_talk_item.view.*
import org.joda.time.DateTime


class GroupTalkAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "kkk.GroupTalkAdapter"

    private var talkList: List<GroupTalk> = ArrayList()
    lateinit var firestoreViewModel: FirestoreViewModel

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.group_talk_item, parent, false)
        return GroupTalkViewHolder(view)
    }

    override fun getItemCount(): Int = talkList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val talk = talkList[position]
        val mHolder = holder as GroupTalkViewHolder
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .signature(ObjectKey(DateTime().minuteOfDay()))

        firestoreViewModel =
            ViewModelProvider((mContext as GroupRoomActivity))[FirestoreViewModel::class.java]
        firestoreViewModel.getUserInfoFromFirestore(talk.userId)
            .observe((mContext as GroupRoomActivity),
                Observer { userInfo ->
                    Glide.with(mHolder.itemView)
                        .load(userInfo.profileImgUrl)
                        .apply(requestOptions)
                        .into(mHolder.profileImg)
                    mHolder.name.text = userInfo.nickname
                })

        mHolder.message.text = talk.message
    }

    fun setTalkList(talkList: List<GroupTalk>) {
        this.talkList = talkList
        notifyDataSetChanged()
    }

    inner class GroupTalkViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val profileImg: CircleImageView = itemView.civ_profile_image
        val name: TextView = itemView.tv_name
        val message: TextView = itemView.tv_message
    }
}