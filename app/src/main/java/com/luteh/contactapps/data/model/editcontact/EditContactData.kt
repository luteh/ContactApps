package com.luteh.contactapps.data.model.editcontact


import com.google.gson.annotations.SerializedName

data class EditContactData(
    @SerializedName("firstName")
    val firstName: String, // Bilbohong
    @SerializedName("lastName")
    val lastName: String, // Baggins
    @SerializedName("age")
    val age: Int, // 1
    @SerializedName("photo")
    val photo: String, // http://vignette1.wikia.nocookie.net/lotr/images/6/68/Bilbo_baggins.jpg/revision/latest?cb=20130202022550
    @SerializedName("id")
    val id: String // f5ab1670-cb85-11e9-b57d-55c22df998ee
)