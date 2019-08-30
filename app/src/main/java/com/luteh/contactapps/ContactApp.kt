package com.luteh.contactapps

import android.app.Application
import com.luteh.contactapps.data.remote.ApiServiceInterface
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
class ContactApp : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@ContactApp))

        bind() from singleton { ApiServiceInterface() }

    }
}