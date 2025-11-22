package com.techlad.mydairyfarm.models

data class InseminatedCowModel(
    var id: String = "",
    val cowName: String = "",
    val bullBreed: String = "",
    val pregnancyDuration: String = "",
    val inseminationDate: String = ""
)
