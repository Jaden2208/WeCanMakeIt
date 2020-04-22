package com.whalez.wecanmakeit.ui.main.group

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
//            (layoutManager as LinearLayoutManager).stackFromEnd = true
            adapter = groupTalkAdapter
            addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
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
            })

        firestoreViewModel.getGroupTalkFromFirestore(groupId).observe(this,
            Observer { groupTalk ->
                groupTalkAdapter.setTalkList(groupTalk)
                if (groupTalk.isNotEmpty()) rv_group_talk.smoothScrollToPosition(groupTalk.size - 1)
            })

        rv_group_talk.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager =
                    rv_group_talk.layoutManager as LinearLayoutManager?
                val firstVisibleItemPosition =
                    linearLayoutManager!!.findFirstVisibleItemPosition()
                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount

                Log.d(TAG, "firstVisibleItemPosition: $firstVisibleItemPosition")
                Log.d(TAG, "visibleItemCount: $visibleItemCount")
                Log.d(TAG, "totalItemCount: $totalItemCount")

                if(firstVisibleItemPosition == 0){

                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }
        })

        btn_send.setOnClickListener {
            val message = et_message.text.toString().trim()
            if(message.isEmpty()) return@setOnClickListener
            val talk = GroupTalk(DateTime().millis, kakaoId, message)
            firestoreViewModel.addTalkOnGroupInFirestore(groupId, talk)
        }


    }
}
