package com.luteh.contactapps.data.model.savecontact


import com.google.gson.annotations.SerializedName

data class SaveContactRequest(
    @SerializedName("firstName")
    val firstName: String, // Bilbohong
    @SerializedName("lastName")
    val lastName: String, // Baggins
    @SerializedName("age")
    val age: String, // "111"
    @SerializedName("photo")
    val photo: String // http://vignette1.wikia.nocookie.net/lotr/images/6/68/Bilbo_baggins.jpg/revision/latest?cb=20130202022550
) {

    fun isValidContact(): Int {
        return when {
            firstName.isEmpty() -> 0
            firstName.length < 3 -> 1
            lastName.isEmpty() -> 2
            lastName.length < 3 -> 3
            age.isEmpty() -> 4
            else -> -1
        }
    }
}