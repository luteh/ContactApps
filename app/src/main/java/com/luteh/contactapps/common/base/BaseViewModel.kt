package com.luteh.contactapps.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
class BaseViewModel<N> : ViewModel() {

    var mIsLoading = MutableLiveData<Boolean>()

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    var mNavigator: N? = null
        get() = WeakReference(field).get()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}