package com.luteh.contactapps.ui.listcontacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding3.widget.textChanges
import com.luteh.contactapps.R
import com.luteh.contactapps.ui.MyViewModelFactory
import com.luteh.contactapps.ui.listcontacts.adapter.ListContactsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

    private val compositeDisposable = CompositeDisposable()

    private val listContactsAdapter = ListContactsAdapter()

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private lateinit var mBottomSheetView: View

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
            showBottomSheetDialog(BottomSheetType.ADD)
        }
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ListContactsViewModel::class.java)
        viewModel.mNavigator = this
    }

    private fun observeData() {
        viewModel.mIsLoading.observe(this, Observer {
            pb_list_contacts.visibility = if (it) VISIBLE else GONE
            rv_list_contacts.visibility = if (it) GONE else VISIBLE
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

    private fun showBottomSheetDialog(bottomSheetType: BottomSheetType) {
        if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        mBottomSheetView = LayoutInflater.from(this).inflate(R.layout.list_contacts_sheet, null)

        with(mBottomSheetView) {
            // listener to validate age format
            compositeDisposable.add(
                et_list_contacts_age_sheet.textChanges()
                    .observeOn(Schedulers.io())
                    .map { it.toString().trim() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it.startsWith("0")) {
                            til_list_contacts_age_sheet.error =
                                "Age format cannot start with zero (0)"
                            til_list_contacts_age_sheet.requestFocus()
                            et_list_contacts_age_sheet.setText("")
                        } else if (it.isNotEmpty()) {
                            til_list_contacts_age_sheet.error = null
                        }
                    }
            )

            if (bottomSheetType == BottomSheetType.EDIT) { // if bottom sheet type is EDIT contact
                tv_list_contacts_title_sheet.text = "Edit Contact"
                btn_list_contacts_delete_sheet.apply {
                    visibility = VISIBLE
                    setOnClickListener { }
                }
                btn_list_contacts_add_sheet.apply {
                    text = "Edit"
                    setOnClickListener { }
                }
            } else { // if bottom sheet type is ADD contact
                btn_list_contacts_add_sheet.setOnClickListener {
                    clearContactFormErrors()

                    val firstName = et_list_contacts_first_name_sheet.text.toString()
                    val lastName = et_list_contacts_last_name_sheet.text.toString()
                    val age = et_list_contacts_age_sheet.text.toString()

                    viewModel.submitContact(
                        firstName,
                        lastName,
                        age,
                        " ",
                        BottomSheetType.ADD
                    )
                }
            }

            btn_list_contacts_close_sheet.setOnClickListener {
                mBottomSheetDialog?.hide()
            }
        }

        mBottomSheetDialog = BottomSheetDialog(this)
        mBottomSheetDialog?.apply {
            setContentView(mBottomSheetView)
            window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setOnDismissListener {
                mBottomSheetDialog = null
            }
            show()
        }
    }

    private fun clearContactFormErrors() {
        with(mBottomSheetView) {
            til_list_contacts_first_name_sheet.error = null
            til_list_contacts_last_name_sheet.error = null
            til_list_contacts_age_sheet.error = null
        }
    }

    override fun onErrorFirstNameEmpty() {
        with(mBottomSheetView) {
            til_list_contacts_first_name_sheet.error = "First Name is required"
            til_list_contacts_first_name_sheet.requestFocus()
        }
    }

    override fun onErrorLastNameEmpty() {
        with(mBottomSheetView) {
            til_list_contacts_last_name_sheet.error = "Last name is required"
            til_list_contacts_last_name_sheet.requestFocus()
        }
    }

    override fun onErrorAgeEmpty() {
        with(mBottomSheetView) {
            til_list_contacts_age_sheet.error = "Age is required"
            til_list_contacts_age_sheet.requestFocus()
        }
    }

    override fun onSuccessSaveContact(message: String) {
        mBottomSheetDialog?.hide()
        viewModel.getAllContacts()
        longToast(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    enum class BottomSheetType {
        ADD, EDIT
    }
}
