package com.luteh.contactapps.ui.listcontacts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.luteh.contactapps.common.base.BaseViewModel
import com.luteh.contactapps.data.MyRepository
import com.luteh.contactapps.data.model.getallcontacts.GetAllContactsData
import com.luteh.contactapps.data.model.savecontact.SaveContactRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
@Suppress("UnstableApiUsage")
class ListContactsViewModel(private val myRepository: MyRepository) :
    BaseViewModel<ListContactsNavigator>() {

    private val TAG = "ListContactsViewModel"

    val allContactsData: MutableLiveData<List<GetAllContactsData>> = MutableLiveData()

    fun getAllContacts() {
        compositeDisposable.add(
            myRepository.getAllContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mIsLoading.value = true }
                .doOnTerminate { mIsLoading.value = false }
                .subscribe({ response ->
                    allContactsData.value = response.data
                }, { throwable ->
                    Log.e(TAG, "getAllContacts: $throwable")
                })
        )
    }

    fun saveContact(firstName: String, lastName: String, age: Int, photo: String) {
        compositeDisposable.add(
            myRepository.saveContact(SaveContactRequest(firstName, lastName, age, photo))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mIsLoading.value = true }
                .doOnTerminate { mIsLoading.value = false }
                .subscribe({ response ->
                    mNavigator?.onSuccessSaveContact(response.message)
                }, { throwable ->
                    Log.e(TAG, "saveContact: $throwable")
                })
        )
    }
}