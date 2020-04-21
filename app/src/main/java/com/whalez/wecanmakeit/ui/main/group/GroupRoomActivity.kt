package com.whalez.wecanmakeit.ui.main.group

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.whalez.wecanmakeit.EXTRA_GROUP_ID
import com.whalez.wecanmakeit.R
import com.whalez.wecanmakeit.UserSessionManager
import com.whalez.wecanmakeit.firestore.FirestoreViewModel
import com.whalez.wecanmakeit.firestore.GroupTalk
import kotlinx.android.synthetic.main.activity_group_room.*
import org.joda.time.DateTime

class GroupRoomActivity : AppCompatActivity() {

    val TAG = "kkk.GroupRoomActivity"

    private lateinit var groupTalkAdapter: GroupTalkAdapter
    private lateinit var firestoreViewModel: FirestoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_room)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        val kakaoId = UserSessionManager(this).currentID!!

        val groupId = intent.getStringExtra(EXTRA_GROUP_ID)!!

        rv_group_talk.apply {
            groupTalkAdapter = GroupTalkAdapter()
            layoutManager = LinearLayoutManager(this@GroupRoomActivity)
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            adapter = groupTalkAdapter
            addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                Log.d(TAG, "oldTop: $oldTop")
                Log.d(TAG, "top: $top")
                Log.d(TAG, "oldBottom: $oldBottom")
                Log.d(TAG, "bottom: $bottom")
                if(oldBottom == bottom) return@addOnLayoutChangeListener
                val adapterSize = rv_group_talk.adapter!!.itemCount
                if(adapterSize > 0) rv_group_talk.smoothScrollToPosition(adapterSize - 1)
            }
        }

        firestoreViewModel = ViewModelProvider(this)[FirestoreViewModel::class.java]
        firestoreViewModel.getGroupInfoFromFirestore(groupId).observe(this,
            Observer { group ->
                tv_group_type.text = group.groupType
                tv_group_name.text = group.groupName

//                val talkList: ArrayList<GroupTalk> = ArrayList()
                // 여기에 그룹 룸 가져올수 있도록
//                for(talkJson in group.groupTalks){
//                    val groupTalk = Gson().fromJson(talkJson, GroupTalk::class.java)
//                    val timestamp = groupTalk.timestamp
//                    val userId = groupTalk.userId
//                    val message = groupTalk.message
//                    val talkItem = GroupTalk(timestamp, userId, message)
//                    talkList.add(talkItem)
//                }
//                groupTalkAdapter.setTalkList(talkList)
//                if (talkList.size > 0) rv_group_talk.smoothScrollToPosition(talkList.size - 1)

            })

        firestoreViewModel.getGroupTalkFromFirestore(groupId).observe(this,
            Observer { groupTalk ->
                groupTalkAdapter.setTalkList(groupTalk)
                if (groupTalk.isNotEmpty()) rv_group_talk.smoothScrollToPosition(groupTalk.size - 1)
            })

        btn_send.setOnClickListener {
            val message = et_message.text.toString().trim()
            if(message.isEmpty()) return@setOnClickListener
            val talk = GroupTalk(DateTime().millis, kakaoId, message)
            firestoreViewModel.addTalkOnGroupInFirestore(groupId, talk)
        }


    }
}
