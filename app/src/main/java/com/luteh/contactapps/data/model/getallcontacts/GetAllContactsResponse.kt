package com.luteh.contactapps.data.model.getallcontacts


import com.google.gson.annotations.SerializedName

data class GetAllContactsResponse(
    @SerializedName("message")
    val message: String, // Get contacts
    @SerializedName("data")
    val `data`: List<GetAllContactsData>
)