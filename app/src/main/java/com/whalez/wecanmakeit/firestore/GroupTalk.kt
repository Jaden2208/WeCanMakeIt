package com.whalez.wecanmakeit.firestore

data class GroupTalk(
    val timestamp: Long? = null,
    val userId: String = "",
    val message: String = ""
)