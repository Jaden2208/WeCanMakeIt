package com.whalez.wecanmakeit.ui.main.setting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.whalez.wecanmakeit.*
import com.whalez.wecanmakeit.ui.main.todo.TodoViewModel
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_logout.setOnClickListener {
            UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                override fun onCompleteLogout() {
                    val builder = simpleBuilder(mContext)
                    builder.setMessage("로그아웃 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예") { _, _ ->
                            kakaoLogin = 0
                            shortToast(mContext, "로그아웃되었습니다.")
                            goToLoginPage(mContext)
                        }
                        .setNegativeButton("아니요") { _, _ -> }
                        .show()
                }
            })
        }

        btn_delete_account.setOnClickListener {
            UserManagement.getInstance().requestUnlink(object : UnLinkResponseCallback() {
                override fun onSuccess(result: Long?) {
                    val builder = simpleBuilder(mContext)
                    builder.setMessage("회원탈퇴 하시겠습니까? 저장된 모든 데이터가 삭제됩니다.")
                        .setCancelable(false)
                        .setPositiveButton("예") { _, _ ->
                            kakaoLogin = 0
                            shortToast(mContext, "회원탈퇴되었습니다.")

                            // Room DB 삭제
                            ViewModelProvider(this@SettingFragment)[TodoViewModel::class.java].deleteAll()

                            // Firestore 사용자 정보 삭제
                            removeAllUserDataOnFirestore()

                            goToLoginPage(mContext)
                        }
                        .setNegativeButton("아니요") { _, _ -> }
                        .show()
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.d("kkk", "sessionClosed")
                }
            })
        }
    }

    private fun removeAllUserDataOnFirestore(){
        val userSessionManager = UserSessionManager(mContext)
        val kakaoId = userSessionManager.currentID
        if (kakaoId == null) {
            Log.d("kkk", "SharedPreference에 있는 kakaoId 값이 null 임.")
            return
        }
        // SharedPreference 사용자 정보 삭제
        userSessionManager.removeUserDataFromSharedReference()

        // firestore 사용자 정보 삭제 시작.
        val firestore = FirebaseFirestore.getInstance()
        val userFirestore = firestore.collection("users").document(kakaoId)
        userFirestore.get().addOnSuccessListener {
            val userGroupList = it.data!!["group"] as List<String>
            val groupFirestore = firestore.collection("groups")
            for (g in userGroupList) {
                groupFirestore.document(g)
                    .update("group_members", FieldValue.arrayRemove(kakaoId))
                    .addOnSuccessListener {
                        Log.d("kkk", "그룹에서 탈퇴한 회원은 삭제")
                        userFirestore.delete()
                            .addOnSuccessListener {
                                Log.d("kkk", "회원 정보 삭제 완료")
                            }
                            .addOnFailureListener {
                                Log.d("kkk", "회원 정보 삭제 실패 ${it.message}")
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.d("kkk", "그룹에서 탈퇴한 회원 삭제 실패 : ${e.message}")
                    }
            }
        }
    }
}