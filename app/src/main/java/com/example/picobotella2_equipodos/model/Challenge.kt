package com.example.picobotella2_equipodos.model

import com.google.firebase.firestore.DocumentId

data class Challenge(
    @DocumentId
    var id: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)