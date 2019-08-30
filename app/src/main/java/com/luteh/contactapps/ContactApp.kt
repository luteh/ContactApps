package com.luteh.contactapps

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

/**
 * Created by Luthfan Maftuh on 8/30/2019.
 * Email luthfanmaftuh@gmail.com
 */
class ContactApp : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@ContactApp))
    }
}