package com.luteh.contactapps

import android.app.Application
import com.luteh.contactapps.data.MyRepository
import com.luteh.contactapps.data.MyRepositoryImpl
import com.luteh.contactapps.data.remote.ApiServiceInterface
import com.luteh.contactapps.ui.MyViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
class ContactApp : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@ContactApp))

        bind() from singleton { ApiServiceInterface() }
        bind<MyRepository>() with singleton { MyRepositoryImpl(instance()) }

        bind() from provider { MyViewModelFactory(instance()) }
    }
}