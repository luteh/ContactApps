package com.luteh.contactapps.data

import com.luteh.contactapps.data.model.getallcontacts.GetAllContactsResponse
import com.luteh.contactapps.data.model.savecontact.SaveContactRequest
import com.luteh.contactapps.data.model.savecontact.SaveContactResponse
import com.luteh.contactapps.data.remote.ApiServiceInterface
import io.reactivex.Single

class ContactAppsRepositoryImpl(private val apiServiceInterface: ApiServiceInterface) :
    ContactAppsRepository {

    override fun getAllContacts(): Single<GetAllContactsResponse> =
        apiServiceInterface.getAllContacts()

    override fun saveContact(saveContactRequest: SaveContactRequest): Single<SaveContactResponse> =
        apiServiceInterface.saveContact(saveContactRequest)
}