package com.luteh.contactapps.data

import com.luteh.contactapps.data.model.deletecontact.DeleteContactResponse
import com.luteh.contactapps.data.model.editcontact.EditContactResponse
import com.luteh.contactapps.data.model.getallcontacts.GetAllContactsResponse
import com.luteh.contactapps.data.model.savecontact.SaveContactRequest
import com.luteh.contactapps.data.model.savecontact.SaveContactResponse
import io.reactivex.Single

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
interface MyRepository {

    fun getAllContacts(): Single<GetAllContactsResponse>
    fun saveContact(saveContactRequest: SaveContactRequest): Single<SaveContactResponse>
    fun editContact(id: String, saveContactRequest: SaveContactRequest): Single<EditContactResponse>
    fun deleteContact(id: String):Single<DeleteContactResponse>
}