package com.luteh.contactapps.data.model.savecontact


import com.google.gson.annotations.SerializedName

data class SaveContactRequest(
    @SerializedName("firstName")
    val firstName: String, // Bilbohong
    @SerializedName("lastName")
    val lastName: String, // Baggins
    @SerializedName("age")
    val age: Int, // 111
    @SerializedName("photo")
    val photo: String // http://vignette1.wikia.nocookie.net/lotr/images/6/68/Bilbo_baggins.jpg/revision/latest?cb=20130202022550
)