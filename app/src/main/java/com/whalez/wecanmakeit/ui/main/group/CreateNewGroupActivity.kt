package com.whalez.wecanmakeit.ui.main.group

import android.app.Activity
import android.app.Activity.RESULT_OK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.whalez.wecanmakeit.*
import com.whalez.wecanmakeit.firestore.FirestoreViewModel
import com.whalez.wecanmakeit.firestore.Group
import kotlinx.android.synthetic.main.activity_create_new_group.*
import java.util.*

class CreateNewGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_group)

        btn_save.setOnClickListener {
            val kakaoId = UserSessionManager(this).currentID!!
            val groupName = et_group_name.text.trim().toString()
            val groupType = et_group_type.text.trim().toString()
            if(groupName.isBlank() || groupType.isBlank()){
                shortToast(this, "그룹 타입과 그룹 명 모두 입력해주세요.")
                return@setOnClickListener
            }

            val groupId = UUID.randomUUID().toString()
            intent.apply {
                putExtra(EXTRA_GROUP_ID, groupId)
                putExtra(EXTRA_GROUP_NAME, groupName)
                putExtra(EXTRA_GROUP_TYPE, groupType)
                putExtra(EXTRA_GROUP_USER, arrayListOf(kakaoId))
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}
