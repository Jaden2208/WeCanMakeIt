package com.whalez.wecanmakeit.ui.main.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.whalez.wecanmakeit.R
import com.whalez.wecanmakeit.UserSessionManager
import com.whalez.wecanmakeit.shortToast
import kotlinx.android.synthetic.main.activity_create_new_group.*
import java.util.*

class CreateNewGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_group)

        btn_save.setOnClickListener {
            val groupName = et_group_name.text.trim().toString()
            val groupType = et_group_type.text.trim().toString()
            if(groupName.isBlank() || groupType.isBlank()){
                shortToast(this, "그룹 타입과 그룹 명 모두 입력해주세요.")
                return@setOnClickListener
            }
            createNewGroup(groupName, groupType)
        }

    }

    private fun createNewGroup(groupName: String, groupType: String) {
        val groupId = UUID.randomUUID().toString()
        val userKakaoId = UserSessionManager(this).currentID.toString()
        val group = hashMapOf(
            "group_id" to groupId,
            "group_name" to groupName,
            "group_type" to groupType,
            "group_members" to listOf(userKakaoId)
        )
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("groups").document(groupId).set(group)
            .addOnSuccessListener {
                firestore.collection("users").document(userKakaoId)
                    .update("group", FieldValue.arrayUnion(groupId))
                    .addOnSuccessListener {
                        shortToast(this, "새 그룹이 생성되었습니다.")
                        finish()
                    }.addOnFailureListener {
                        Log.d("kkk", "사용자 그룹 엄데이트 실패: ${it.message}")
                    }
            }
            .addOnFailureListener {
                Log.d("kkk", "새 그룹 생성 실패 : ${it.message}")
            }
    }


}
