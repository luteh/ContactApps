package com.luteh.contactapps.data

import com.luteh.contactapps.data.model.getallcontacts.GetAllContactsResponse
import com.luteh.contactapps.data.model.savecontact.SaveContactRequest
import com.luteh.contactapps.data.model.savecontact.SaveContactResponse
import com.luteh.contactapps.data.remote.ApiServiceInterface
import io.reactivex.Single

class MyRepositoryImpl(private val apiServiceInterface: ApiServiceInterface) :
    MyRepository {

    override fun getAllContacts(): Single<GetAllContactsResponse> =
        apiServiceInterface.getAllContacts()

    override fun saveContact(saveContactRequest: SaveContactRequest): Single<SaveContactResponse> =
        apiServiceInterface.saveContact(
            saveContactRequest
        )
}