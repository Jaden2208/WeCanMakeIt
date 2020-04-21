package com.whalez.wecanmakeit.firestore

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.whalez.wecanmakeit.shortToast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot

class FirestoreViewModel : ViewModel() {

    private val TAG = "kkk.FirestoreViewModel"
    private val firestoreRepository = FirestoreRepository()
    private var userGroups: MutableLiveData<List<Group>> = MutableLiveData()
    private var userInfo: MutableLiveData<User> = MutableLiveData()

    //    var savedAddresses: MutableLiveData<List<AddressItem>> = MutableLiveData()
    private var currentGroupInfo: MutableLiveData<Group> = MutableLiveData()

    // save user to firebase
    fun saveUserToFirestore(user: User) {
        firestoreRepository.createNewUser(user).addOnFailureListener {
            Log.e(TAG, "Failed to create user!")
        }
    }

    fun getUserGroupListFromFirestore(kakaoId: String): LiveData<List<Group>> {
        firestoreRepository.getUserGroups(kakaoId).addSnapshotListener { value, e ->
            if (e != null) {
                Log.d(TAG, "Failed to get user groups : ${e.message}")
                return@addSnapshotListener
            }
            val groupList: MutableList<Group> = mutableListOf()
            for (doc in value!!) {
                val groupItem = doc.toObject(Group::class.java)
                groupList.add(groupItem)
            }
            userGroups.value = groupList
        }
        return userGroups
    }

    fun getUserInfoFromFirestore(kakaoId: String): LiveData<User> {
        firestoreRepository.getUserInfo(kakaoId).addSnapshotListener { doc, e ->
            if(doc == null || e != null){
                Log.d(TAG, "Failed to get user info : ${e!!.message}")
                return@addSnapshotListener
            }
            val user = User(
                doc["kakaoId"].toString(),
                doc["nickname"].toString(),
                doc["profileImgUrl"].toString())

            userInfo.value = user
        }
        return userInfo
    }

    fun getGroupInfoFromFirestore(groupId: String): LiveData<Group> {
        firestoreRepository.getGroupInfo(groupId).addSnapshotListener { doc, e ->
            if (e != null || doc == null) {
                Log.d(TAG, "Failed to get group info : ${e!!.message}")
                return@addSnapshotListener
            }
            currentGroupInfo.value = doc.toObject(Group::class.java)
        }
        return currentGroupInfo
    }


    // delete an user from firestore
    fun deleteUserFromFirestore(kakaoId: String) {
        firestoreRepository.deleteUser(kakaoId)
            .addOnSuccessListener {
                firestoreRepository.getUserGroups(kakaoId).addSnapshotListener { value, e ->
                    for (doc in value!!) {
                        firestoreRepository.deleteUserFromGroup(kakaoId, doc["groupId"] as String)
                            .addOnSuccessListener {
                                Log.d(TAG, "그룹에서 탈퇴한 사용자 삭제 완료")
                            }
                            .addOnFailureListener {
                                Log.e(TAG, "그룹에서 탈퇴한 사용자 삭제 실패: ${it.message}")
                            }
//                        Log.d(TAG, "groupName: ${doc["groupName"]}")
                    }
                }
            }.addOnFailureListener {
                Log.e(TAG, "Failed to delete User")
            }
    }

//    fun updateGroupOnFirestore(groupId: String, kakaoId: String, type: Int) {
//        firestoreRepository.updateGroup(groupId, kakaoId, type).addOnFailureListener {
//            Log.e(TAG, "Failed to update group")
//        }
//    }

    fun createNewGroupOnFirestore(group: Group) {
        firestoreRepository.createNewGroup(group)
            .addOnSuccessListener {
                Log.d(TAG, "New group created")
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to create new group")
            }
    }

    fun addTalkOnGroupInFirestore(groupId: String, talk: GroupTalk) {
        firestoreRepository.addTalk(groupId, talk)
            .addOnFailureListener {
                Log.e(TAG, "Failed to add talk")
            }
    }


}