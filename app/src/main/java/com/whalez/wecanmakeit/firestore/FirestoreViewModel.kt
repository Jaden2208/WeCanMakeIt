package com.whalez.wecanmakeit.firestore

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.whalez.wecanmakeit.shortToast
import androidx.lifecycle.MutableLiveData

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

    // get realtime updates from firebase regarding saved addresses
//    fun getUserInfoFromFirestore(kakaoId: String): LiveData<List<User>> {
//        firestoreRepository.getUserInfo(kakaoId)
//            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
//                if (e != null) {
//                    Log.w(TAG, "Listen failed.", e)
//                    savedAddresses.value = null
//                    return@EventListener
//                }
//
//                var savedAddressList: MutableList<AddressItem> = mutableListOf()
//                for (doc in value!!) {
//                    var addressItem = doc.toObject(AddressItem::class.java)
//                    savedAddressList.add(addressItem)
//                }
//                savedAddresses.value = savedAddressList
//            })
//
//        return savedAddresses
//    }

    fun getUserGroupListFromFirestore(kakaoId: String): LiveData<List<Group>> {
        firestoreRepository.getUserInfo(kakaoId)
            .addOnSuccessListener { it ->
                val userGroupsId = it.data!!["group"] as List<String>
                Log.d(TAG, "userGroupsId: ${userGroupsId[0]}")
                val groupList: MutableList<Group> = mutableListOf()
                for(groupId in userGroupsId){
//                    Log.d(TAG, groupId)
                    firestoreRepository.getGroupInfo(groupId).addOnSuccessListener { doc ->
                        val group = doc.toObject(Group::class.java)!!
                        Log.d(TAG, group.groupId)
                        groupList.add(group)
                    }

                }
                userGroups.value = groupList
            }
            .addOnFailureListener {
                Log.e(TAG, "get user's group list failed")
            }
        return userGroups
    }


    // delete an user from firestore
    fun deleteUserFromFirestore(kakaoId: String) {
        firestoreRepository.deleteUser(kakaoId).addOnFailureListener {
            Log.e(TAG, "Failed to delete User")
        }
    }

    fun updateGroupOnFirestore(groupId: String, kakaoId: String, type: Int) {
        firestoreRepository.updateGroup(groupId, kakaoId, type).addOnFailureListener {
            Log.e(TAG, "Failed to update group")
        }
    }

    fun createNewGroupOnFirestore(kakaoId: String, group: Group) {
        firestoreRepository.createNewGroup(group)
            .addOnSuccessListener {
                Log.d(TAG, "New group created")
                firestoreRepository.updateUserGroup(kakaoId, group.groupId!!).addOnFailureListener {
                    Log.e(TAG, "Failed to update user group")
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to create new group")
            }
    }
}