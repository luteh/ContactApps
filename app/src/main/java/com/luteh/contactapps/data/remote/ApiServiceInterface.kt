package com.luteh.contactapps.data.remote

import com.luteh.contactapps.common.utils.Config
import com.luteh.contactapps.data.model.getallcontacts.GetAllContactsResponse
import com.luteh.contactapps.data.model.savecontact.SaveContactRequest
import com.luteh.contactapps.data.model.savecontact.SaveContactResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
interface ApiServiceInterface {

    @GET(ApiEndPoint.ENDPOINT_CONTACT)
    fun getAllContacts(): Single<GetAllContactsResponse>

    @POST(ApiEndPoint.ENDPOINT_CONTACT)
    fun saveContact(
        @Body saveContactRequest: SaveContactRequest
    ): Single<SaveContactResponse>

    companion object {
        operator fun invoke(): ApiServiceInterface {
            return Retrofit.Builder()
                .baseUrl(Config.baseApiUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServiceInterface::class.java)
        }
    }
}