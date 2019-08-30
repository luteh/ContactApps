package com.luteh.contactapps.ui.listcontacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luteh.contactapps.R
import com.luteh.contactapps.ui.MyViewModelFactory
import com.luteh.contactapps.ui.listcontacts.adapter.ListContactsAdapter
import kotlinx.android.synthetic.main.list_contacts_activity.*
import kotlinx.android.synthetic.main.list_contacts_sheet.view.*
import org.jetbrains.anko.longToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class ListContactsActivity : AppCompatActivity(), KodeinAware, ListContactsNavigator {

    override val kodein by closestKodein()
    private val viewModelFactory: MyViewModelFactory by instance()

    private lateinit var viewModel: ListContactsViewModel

    private val listContactsAdapter = ListContactsAdapter()

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>
    private var mBottomSheetDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_contacts_activity)

        onInit()
    }

    private fun onInit() {
        mBottomSheetBehavior = BottomSheetBehavior.from(layout_list_contacts_bottom_sheet)

        initViewModel()
        observeData()
        setupRecyclerView()

        viewModel.getAllContacts()
        fab_list_contacts_add.setOnClickListener {
            showBottomSheetDialog()
        }
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

    private fun showBottomSheetDialog() {
        if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val view = LayoutInflater.from(this).inflate(R.layout.list_contacts_sheet, null)

        with(view) {
            btn_list_contacts_close_sheet.setOnClickListener {
                mBottomSheetDialog?.hide()
            }
        }

        mBottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetDialog?.apply {
            setContentView(view)
            window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setOnDismissListener {
                mBottomSheetDialog = null
            }
            show()
        }
    }

    override fun onSuccessSaveContact(message: String) {
        longToast(message)
    }
}
