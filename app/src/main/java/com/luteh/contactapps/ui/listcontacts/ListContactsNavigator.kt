package com.luteh.contactapps.ui.listcontacts

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
interface ListContactsNavigator {
    fun onSuccessSaveContact(message: String)
    fun onErrorFirstNameEmpty()
    fun onErrorLastNameEmpty()
    fun onErrorAgeEmpty()
    fun onErrorDeleteContact(message: String)
    fun onSuccessDeleteContact(message: String)
    fun onSuccessEditContact(message: String)
    fun onErrorFirstNameLength()
    fun onErrorLastnameLength()
}