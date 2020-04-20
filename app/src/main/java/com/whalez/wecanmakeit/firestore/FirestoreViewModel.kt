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

class FirestoreViewModel: ViewModel() {

    private val TAG = "kkk.FirestoreViewModel"
    private val firestoreRepository = FirestoreRepository()
    var userGroups: MutableLiveData<List<Group>> = MutableLiveData()
//    var savedAddresses: MutableLiveData<List<AddressItem>> = MutableLiveData()

    // save user to firebase
    fun saveUserToFirestore(user: User) {
        firestoreRepository.createNewUser(user).addOnFailureListener {
            Log.e(TAG, "Failed to create user!")
        }
    }

    fun getUserGroupListFromFirestore(kakaoId: String): LiveData<List<Group>> {
        firestoreRepository.getUserGroups(kakaoId).addSnapshotListener { value, e ->
            if(e != null) {
                Log.d(TAG, "listened failed : ${e.message}")
                return@addSnapshotListener
            }
            val groupList: MutableList<Group> = mutableListOf()
            for(doc in value!!){
                val groupItem = doc.toObject(Group::class.java)
                groupList.add(groupItem)
            }
            userGroups.value = groupList
        }
        return userGroups
    }


    // delete an user from firestore
    fun deleteUserFromFirestore(kakaoId: String) {
        firestoreRepository.deleteUser(kakaoId)
            .addOnSuccessListener {
                firestoreRepository.getUserGroups(kakaoId).addSnapshotListener { value, e ->
                    for(doc in value!!){
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
}