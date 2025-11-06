package com.techlad.mydairyfarm.models

data class CowModel(
    var cowId:String = "",
    val cowName: String = "",
    val motherName: String = "",
    val dateOfBirth: String = "",
    val status: String = "",
    val breed: String = "",
    val tagNumber: String = "",
    val imageUrl: String = ""
)
