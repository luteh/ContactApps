package com.luteh.contactapps.data.model.getallcontacts


import com.google.gson.annotations.SerializedName

data class GetAllContactsData(
    @SerializedName("id")
    val id: String, // 2b355ab0-cb06-11e9-bc5d-0323cb3589a9
    @SerializedName("firstName")
    val firstName: String, // Bilbohong
    @SerializedName("lastName")
    val lastName: String, // Baggins
    @SerializedName("age")
    val age: Int, // 111
    @SerializedName("photo")
    val photo: String // http://vignette1.wikia.nocookie.net/lotr/images/6/68/Bilbo_baggins.jpg/revision/latest?cb=20130202022550
)