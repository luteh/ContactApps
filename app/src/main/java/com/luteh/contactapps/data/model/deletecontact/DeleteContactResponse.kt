package com.luteh.contactapps.data.model.deletecontact


import com.google.gson.annotations.SerializedName

data class DeleteContactResponse(
    @SerializedName("message")
    val message: String // contact deleted
)