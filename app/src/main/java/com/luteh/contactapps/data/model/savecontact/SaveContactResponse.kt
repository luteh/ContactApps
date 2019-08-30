package com.luteh.contactapps.data.model.savecontact


import com.google.gson.annotations.SerializedName

data class SaveContactResponse(
    @SerializedName("message")
    val message: String // contact saved
)