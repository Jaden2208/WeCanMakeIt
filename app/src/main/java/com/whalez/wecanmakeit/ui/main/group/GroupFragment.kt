package com.whalez.wecanmakeit.ui.main.group

import android.app.Activity
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
import com.whalez.wecanmakeit.*
import com.whalez.wecanmakeit.firestore.FirestoreViewModel
import com.whalez.wecanmakeit.firestore.Group
import kotlinx.android.synthetic.main.fragment_group.*

class GroupFragment : Fragment() {

    private lateinit var mContext: Context

    private lateinit var firestoreViewModel: FirestoreViewModel
    private lateinit var groupAdapter: GroupAdapter

    private lateinit var kakaoId: String

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

        kakaoId = UserSessionManager(mContext).currentID!!

        btn_create_group.setOnClickListener {
            val intent = Intent(mContext, CreateNewGroupActivity::class.java)
            startActivityForResult(intent, CREATE_NEW_GROUP)
        }

        rv_my_group.apply {
            groupAdapter = GroupAdapter(GroupAdapter.TYPE_MY_GROUP)
            layoutManager = LinearLayoutManager(mContext)
            adapter = groupAdapter
            setHasFixedSize(true)
        }

        firestoreViewModel = ViewModelProvider(this)[FirestoreViewModel::class.java]
        firestoreViewModel.getUserGroupListFromFirestore(kakaoId).observe(viewLifecycleOwner,
            Observer { groupList -> groupAdapter.setGroupList(groupList) })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CREATE_NEW_GROUP && resultCode == Activity.RESULT_OK && data != null){
            val groupId = data.getStringExtra(EXTRA_GROUP_ID)!!
            val groupName = data.getStringExtra(EXTRA_GROUP_NAME)!!
            val groupType = data.getStringExtra(EXTRA_GROUP_TYPE)!!
            val listWithUser = data.getStringArrayListExtra(EXTRA_GROUP_USER)!!
            val group = Group(groupId, groupName, groupType, listWithUser)
            firestoreViewModel.createNewGroupOnFirestore(group)
        }
    }
}