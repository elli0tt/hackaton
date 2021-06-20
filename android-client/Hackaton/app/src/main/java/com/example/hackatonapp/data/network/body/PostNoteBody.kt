package com.example.hackatonapp.data.network.body

data class PostNoteBody(
    val id: Int,
    val dateTime: Long,
    val sys: Int,
    val dia: Int,
    val pulse: Int? = null,
    val activity: String,
    val comment: String
)
