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

    fun submitContact(
        firstName: String,
        lastName: String,
        age: String,
        photo: String,
        bottomSheetType: ListContactsActivity.BottomSheetType,
        id: String = ""
    ) {
        val saveContactRequest = SaveContactRequest(firstName, lastName, age, photo)
        when (saveContactRequest.isValidContact()) {
            0 -> mNavigator?.onErrorFirstNameEmpty()
            1 -> mNavigator?.onErrorLastNameEmpty()
            2 -> mNavigator?.onErrorAgeEmpty()
            -1 -> {
                if (bottomSheetType == ListContactsActivity.BottomSheetType.ADD)
                    saveContact(saveContactRequest)
                else
                    editContact(id, saveContactRequest)
            }
        }
    }

    private fun editContact(id: String, saveContactRequest: SaveContactRequest) {
        compositeDisposable.add(
            myRepository.editContact(id, saveContactRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mIsLoading.value = true }
                .doOnTerminate { mIsLoading.value = false }
                .subscribe({ response ->
                    mNavigator?.onSuccessEditContact(response.message)
                }, { throwable ->
                    Log.e(TAG, "editContact: $throwable")
                })
        )
    }

    private fun saveContact(saveContactRequest: SaveContactRequest) {
        compositeDisposable.add(
            myRepository.saveContact(saveContactRequest)
                .subscribeOn(Schedulers.io())
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

    fun deleteContact(id: String) {
        compositeDisposable.add(
            myRepository.deleteContact(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mIsLoading.value = true }
                .doOnTerminate { mIsLoading.value = false }
                .subscribe({ response ->
                    mNavigator?.onSuccessDeleteContact(response.message)
                }, { throwable ->
                    Log.e(TAG, "deleteContact: $throwable")
                    mNavigator?.onErrorDeleteContact("Something went wrong")
                })
        )
    }
}