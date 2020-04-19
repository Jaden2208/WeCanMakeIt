package com.whalez.wecanmakeit.firestore

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.whalez.wecanmakeit.UserSessionManager

class FirestoreRepository {
    private val TAG = "kkk.FirestoreRepository"

    //  userSessionManager 써야되면 쓰자.
    //    private val kakaoId = UserSessionManager(context).currentID!!
    private val firestoreDB = FirebaseFirestore.getInstance()
    private val userRef = firestoreDB.collection("users")
    private val groupRef = firestoreDB.collection("groups")

    companion object {
        const val TYPE_DELETE_ACCOUNT = 0
        const val TYPE_GROUP_UPDATE = 1
    }

    fun createNewUser(user: User): Task<Void> = userRef.document(user.kakaoId).set(user)

    fun getUserInfo(kakaoId: String): Task<DocumentSnapshot> = userRef.document(kakaoId).get()

    fun deleteUser(kakaoId: String): Task<Void> = userRef.document(kakaoId).delete()

    fun updateUserGroup(kakaoId: String, groupId: String): Task<Void>
            = userRef.document(kakaoId).update("group", FieldValue.arrayUnion(groupId))

    fun updateGroup(groupId: String, kakaoId: String, type: Int): Task<Void> {
        val doc = groupRef.document(groupId)
        return if(type == TYPE_DELETE_ACCOUNT) {
            doc.update("group_members", FieldValue.arrayRemove(kakaoId))
        } else {
            doc.update("group_members", FieldValue.arrayUnion(kakaoId))
        }
    }

    fun getGroupInfo(groupId: String): Task<DocumentSnapshot> = groupRef.document(groupId).get()

    fun createNewGroup(group: Group): Task<Void>
            = groupRef.document(group.groupId).set(group)


}