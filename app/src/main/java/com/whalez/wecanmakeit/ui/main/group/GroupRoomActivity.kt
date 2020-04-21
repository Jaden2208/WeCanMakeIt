package com.whalez.wecanmakeit.ui.main.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.whalez.wecanmakeit.EXTRA_GROUP_ID
import com.whalez.wecanmakeit.R
import com.whalez.wecanmakeit.UserSessionManager
import com.whalez.wecanmakeit.firestore.FirestoreViewModel
import com.whalez.wecanmakeit.firestore.GroupTalk
import kotlinx.android.synthetic.main.activity_group_room.*
import kotlinx.android.synthetic.main.group_talk_item.*
import org.joda.time.DateTime

class GroupRoomActivity : AppCompatActivity() {

    val TAG = "kkk.GroupRoomActivity"

    private lateinit var groupTalkAdapter: GroupTalkAdapter
    private lateinit var firestoreViewModel: FirestoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_room)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        val kakaoId = UserSessionManager(this).currentID!!

        val groupId = intent.getStringExtra(EXTRA_GROUP_ID)!!

        rv_group_talk.apply {
            groupTalkAdapter = GroupTalkAdapter()
            layoutManager = LinearLayoutManager(this@GroupRoomActivity)
            adapter = groupTalkAdapter
            setHasFixedSize(true)
        }

        firestoreViewModel = ViewModelProvider(this)[FirestoreViewModel::class.java]
        firestoreViewModel.getGroupInfoFromFirestore(groupId).observe(this,
            Observer { group ->
                tv_group_type.text = group.groupType
                tv_group_name.text = group.groupName

                val talkList: ArrayList<GroupTalk> = ArrayList()
                for(talkJson in group.groupTalks){
                    val groupTalk = Gson().fromJson(talkJson, GroupTalk::class.java)
                    val timestamp = groupTalk.timestamp
                    val userId = groupTalk.userId
                    val message = groupTalk.message
                    val talkItem = GroupTalk(timestamp, userId, message)
                    talkList.add(talkItem)
                }
                groupTalkAdapter.setTalkList(talkList)
                rv_group_talk.smoothScrollToPosition(talkList.size-1)

            })

        btn_send.setOnClickListener {
            val message = et_message.text.toString()
            val talk = GroupTalk(DateTime().millis, kakaoId, message)
            firestoreViewModel.addTalkOnGroupInFirestore(groupId, talk, et_message)
        }


    }
}
