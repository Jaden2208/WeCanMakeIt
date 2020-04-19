package com.whalez.wecanmakeit.firestore

data class Group(
    val groupId: String = "",
    val groupName: String = "",
    val groupType: String = "",
    val groupMembers: List<String> = emptyList()
)