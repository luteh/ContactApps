package com.luteh.contactapps.ui.listcontacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.luteh.contactapps.data.model.getallcontacts.GetAllContactsData
import com.luteh.contactapps.ui.MyViewModelFactory
import com.luteh.contactapps.ui.listcontacts.adapter.ListContactsAdapter
import io.reactivex.Single
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

    private val TAG = "ListContactsActivity"

    override val kodein by closestKodein()
    private val viewModelFactory: MyViewModelFactory by instance()

    private lateinit var viewModel: ListContactsViewModel

    private val compositeDisposable = CompositeDisposable()

    private val listContactsAdapter = ListContactsAdapter {
        showBottomSheetDialog(BottomSheetType.EDIT, it)
    }

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

    @SuppressLint("DefaultLocale")
    private fun observeData() {
        viewModel.mIsLoading.observe(this, Observer {
            pb_list_contacts.visibility = if (it) VISIBLE else GONE
            rv_list_contacts.visibility = if (it) GONE else VISIBLE
        })

        viewModel.allContactsData.observe(this, Observer { listData ->
            compositeDisposable.add(
                Single.fromCallable { listData.sortedWith(compareBy { "${it.firstName.toUpperCase()} ${it.lastName.toUpperCase()}" }) }
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        listContactsAdapter.setDataSources(it)
                    }, { throwable ->
                        Log.e(TAG, "observeData: $throwable")
                    })
            )
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

    //region Bottom sheet dialog
    private fun showBottomSheetDialog(
        bottomSheetType: BottomSheetType,
        data: GetAllContactsData? = null
    ) {
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
                                getString(R.string.label_message_error_age_format)
                            til_list_contacts_age_sheet.requestFocus()
                            et_list_contacts_age_sheet.setText("")
                        } else if (it.isNotEmpty()) {
                            til_list_contacts_age_sheet.error = null
                        }
                    }
            )

            if (bottomSheetType == BottomSheetType.EDIT) { // if bottom sheet type is EDIT contact
                tv_list_contacts_title_sheet.text = getString(R.string.title_edit_contact)
                data?.let {
                    et_list_contacts_first_name_sheet.setText(it.firstName)
                    et_list_contacts_last_name_sheet.setText(it.lastName)
                    et_list_contacts_age_sheet.setText(it.age.toString())

                    btn_list_contacts_delete_sheet.apply {
                        visibility = VISIBLE
                        setOnClickListener { _ ->
                            viewModel.deleteContact(it.id)
                        }
                    }
                    btn_list_contacts_add_sheet.apply {
                        text = getString(R.string.label_edit)
                        setOnClickListener { _ ->

                            viewModel.submitContact(
                                firstName,
                                lastName,
                                age,
                                " ",
                                BottomSheetType.EDIT,
                                it.id
                            )
                        }
                    }
                }
            } else { // if bottom sheet type is ADD contact
                btn_list_contacts_add_sheet.setOnClickListener {
                    clearContactFormErrors()

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

    private val firstName: String
        get() = mBottomSheetView.et_list_contacts_first_name_sheet.text.toString()

    private val lastName: String
        get() = mBottomSheetView.et_list_contacts_last_name_sheet.text.toString()

    private val age: String
        get() = mBottomSheetView.et_list_contacts_age_sheet.text.toString()

    private fun clearContactFormErrors() {
        with(mBottomSheetView) {
            til_list_contacts_first_name_sheet.error = null
            til_list_contacts_last_name_sheet.error = null
            til_list_contacts_age_sheet.error = null
        }
    }

    override fun onErrorFirstNameEmpty() {
        with(mBottomSheetView) {
            til_list_contacts_first_name_sheet.error =
                getString(R.string.label_message_error_first_name_required)
            til_list_contacts_first_name_sheet.requestFocus()
        }
    }

    override fun onErrorLastNameEmpty() {
        with(mBottomSheetView) {
            til_list_contacts_last_name_sheet.error =
                getString(R.string.label_message_error_last_name_required)
            til_list_contacts_last_name_sheet.requestFocus()
        }
    }

    override fun onErrorAgeEmpty() {
        with(mBottomSheetView) {
            til_list_contacts_age_sheet.error = getString(R.string.label_message_error_age_required)
            til_list_contacts_age_sheet.requestFocus()
        }
    }
    //endregion

    override fun onSuccessSaveContact(message: String) {
        onSuccessCommonBehavior(message)
    }

    override fun onSuccessEditContact(message: String) {
        onSuccessCommonBehavior(message)
    }

    override fun onSuccessDeleteContact(message: String) {
        onSuccessCommonBehavior(message)
    }

    private fun onSuccessCommonBehavior(message: String) {
        mBottomSheetDialog?.hide()
        viewModel.getAllContacts()
        longToast(message)
    }

    override fun onErrorDeleteContact(message: String) {
        mBottomSheetDialog?.hide()
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
