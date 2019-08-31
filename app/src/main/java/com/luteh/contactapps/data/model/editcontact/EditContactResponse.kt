package com.luteh.contactapps.data.model.editcontact


import com.google.gson.annotations.SerializedName

data class EditContactResponse(
    @SerializedName("message")
    val message: String, // Contact edited
    @SerializedName("data")
    val `data`: EditContactData
)