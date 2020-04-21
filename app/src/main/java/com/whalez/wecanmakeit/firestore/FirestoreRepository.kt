package com.whalez.wecanmakeit.firestore

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.gson.Gson
import com.whalez.wecanmakeit.UserSessionManager

class FirestoreRepository {
    private val TAG = "kkk.FirestoreRepository"

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val userRef = firestoreDB.collection("users")
    private val groupRef = firestoreDB.collection("groups")

    fun createNewUser(user: User): Task<Void> = userRef.document(user.kakaoId).set(user)

    fun getUserInfo(kakaoId: String): DocumentReference = userRef.document(kakaoId)

    fun deleteUser(kakaoId: String): Task<Void> = userRef.document(kakaoId).delete()

    fun deleteUserFromGroup(kakaoId: String, groupId: String): Task<Void> =
        groupRef.document(groupId).update("groupMembers", FieldValue.arrayRemove(kakaoId))

    fun getUserGroups(kakaoId: String): Query = groupRef.whereArrayContains("groupMembers", kakaoId)

    fun createNewGroup(group: Group): Task<Void> = groupRef.document(group.groupId).set(group)

    fun getGroupInfo(groupId: String): DocumentReference = groupRef.document(groupId)

    fun addTalk(groupId: String, talk: GroupTalk): Task<Void> {
        val groupTalk = GroupTalk(talk.timestamp, talk.userId, talk.message)
        val groupTalkJson = Gson().toJson(groupTalk)
        return groupRef.document(groupId).update("groupTalks", FieldValue.arrayUnion(groupTalkJson))
    }


}