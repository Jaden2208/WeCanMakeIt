package com.whalez.wecanmakeit.ui.main.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.auth.Session
import com.kakao.usermgmt.UserManagement
import com.whalez.wecanmakeit.R
import com.whalez.wecanmakeit.UserSessionManager
import com.whalez.wecanmakeit.firestore.FirestoreViewModel
import kotlinx.android.synthetic.main.fragment_group.*

class GroupFragment : Fragment() {

    private lateinit var mContext: Context

    private lateinit var firestoreViewModel: FirestoreViewModel
    private lateinit var groupAdapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val kakaoId = UserSessionManager(mContext).currentID!!

        btn_create_group.setOnClickListener {
            val intent = Intent(mContext, CreateNewGroupActivity::class.java)
            startActivity(intent)
        }

        rv_my_group.apply {
            groupAdapter = GroupAdapter(GroupAdapter.TYPE_MY_GROUP)
            layoutManager = LinearLayoutManager(mContext)
            adapter = groupAdapter
            setHasFixedSize(true)
        }

        firestoreViewModel = ViewModelProvider(this)[FirestoreViewModel::class.java]
        firestoreViewModel.getUserGroupListFromFirestore(kakaoId).observe(viewLifecycleOwner,
            Observer { groupList ->
                groupAdapter.setGroupList(groupList)
                if (groupList.isNotEmpty())
                    Log.d("kkk관찰했다.", groupList[0].groupId)
                else Log.d("kkk관찰", "비어있음")
            })


    }
}