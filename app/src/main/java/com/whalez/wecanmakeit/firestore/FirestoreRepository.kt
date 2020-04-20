package com.whalez.wecanmakeit.firestore

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.whalez.wecanmakeit.UserSessionManager

class FirestoreRepository {
    private val TAG = "kkk.FirestoreRepository"

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val userRef = firestoreDB.collection("users")
    private val groupRef = firestoreDB.collection("groups")

//    companion object {
//        const val TYPE_DELETE_ACCOUNT = 0
//        const val TYPE_GROUP_UPDATE = 1
//    }

    fun createNewUser(user: User): Task<Void> = userRef.document(user.kakaoId).set(user)

    fun getUserInfo(kakaoId: String): Task<DocumentSnapshot> = userRef.document(kakaoId).get()

    fun deleteUser(kakaoId: String): Task<Void> = userRef.document(kakaoId).delete()

    fun addUserInGroup(kakaoId: String, groupId: String): Task<Void> =
        groupRef.document(groupId).update("groupMembers", FieldValue.arrayUnion(kakaoId))

    fun deleteUserFromGroup(kakaoId: String, groupId: String): Task<Void> =
        groupRef.document(groupId).update("groupMembers", FieldValue.arrayRemove(kakaoId))

    fun getUserGroups(kakaoId: String): Query = groupRef.whereArrayContains("groupMembers", kakaoId)

    fun createNewGroup(group: Group): Task<Void> = groupRef.document(group.groupId).set(group)


}