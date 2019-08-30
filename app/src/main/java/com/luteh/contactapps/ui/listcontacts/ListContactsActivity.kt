package com.luteh.contactapps.ui.listcontacts

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.luteh.contactapps.R
import com.luteh.contactapps.ui.MyViewModelFactory
import com.luteh.contactapps.ui.listcontacts.adapter.ListContactsAdapter
import kotlinx.android.synthetic.main.list_contacts_activity.*
import org.jetbrains.anko.longToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class ListContactsActivity : AppCompatActivity(), KodeinAware, ListContactsNavigator {

    override val kodein by closestKodein()
    private val viewModelFactory: MyViewModelFactory by instance()

    private lateinit var viewModel: ListContactsViewModel

    private val listContactsAdapter = ListContactsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_contacts_activity)

        onInit()
    }

    private fun onInit() {
        initViewModel()
        observeData()
        setupRecyclerView()

        viewModel.getAllContacts()
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ListContactsViewModel::class.java)
        viewModel.mNavigator = this
    }

    private fun observeData() {
        viewModel.mIsLoading.observe(this, Observer {
            pb_list_contacts.visibility = if (it) View.VISIBLE else View.GONE
            rv_list_contacts.visibility = if (it) View.GONE else View.VISIBLE
        })

        viewModel.allContactsData.observe(this, Observer {
            listContactsAdapter.setDataSources(it)
        })
    }

    private fun setupRecyclerView() {
        rv_list_contacts.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
            adapter = listContactsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 && fab_list_contacts_add.isShown)
                        fab_list_contacts_add.hide()
                    else
                        fab_list_contacts_add.show()
                }
            })
        }
    }

    override fun onSuccessSaveContact(message: String) {
        longToast(message)
    }
}
