package com.denproj.posmanongjaks.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denproj.posmanongjaks.adapter.AddOnCheckOutRecyclerViewAdapter
import com.denproj.posmanongjaks.adapter.ProductCheckOutRecyclerViewAdapter
import com.denproj.posmanongjaks.databinding.FragmentCheckOutDialogBinding
import com.denproj.posmanongjaks.model.Branch
import com.denproj.posmanongjaks.model.CompleteSaleInfo
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.model.ProductWrapper
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseSaleRepository.OnSaleStatus
import com.denproj.posmanongjaks.viewModel.CheckOutDialogViewmodel
import java.time.ZoneId
import java.time.ZonedDateTime

class CheckOutDialogFragment(
    private val branch: Branch,
    private val selectedItems: HashMap<Long, ProductWrapper>,
    private val selectedAddOns: HashMap<Item, Int>,
    private val onSaleFinished: OnSaleFinished
) :
    DialogFragment() {
    private var viewmodel: CheckOutDialogViewmodel? = null
    private var binding: FragmentCheckOutDialogBinding? = null
    private var productsAdapter: ProductCheckOutRecyclerViewAdapter? = null
    private var addOnsAdapter: AddOnCheckOutRecyclerViewAdapter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewmodel = ViewModelProvider(requireActivity()).get(
            CheckOutDialogViewmodel::class.java
        )
        val dialogBuilder = AlertDialog.Builder(requireContext())

        this.binding = FragmentCheckOutDialogBinding.inflate(
            layoutInflater
        )

        this.productsAdapter = ProductCheckOutRecyclerViewAdapter(
            selectedItems, ArrayList(selectedItems.keys)
        ) { total: Double? ->
            val sumTotal = addOnsAdapter!!.total + productsAdapter!!.totalPrice
            binding!!.totalAmount = sumTotal
        }

        this.addOnsAdapter = AddOnCheckOutRecyclerViewAdapter(
            selectedAddOns,
            OnTotalAmountChanged { total: Double? ->
                val sumTotal = addOnsAdapter!!.total + productsAdapter!!.totalPrice
                binding!!.totalAmount = sumTotal
            })

        binding!!.productsTobeSold.layoutManager = LinearLayoutManager(requireContext())
        binding!!.productsTobeSold.adapter = productsAdapter

        binding!!.selectedAddOns.layoutManager = LinearLayoutManager(requireContext())
        binding!!.selectedAddOns.adapter = addOnsAdapter

        dialogBuilder.setPositiveButton("Checkout") { dialogInterface: DialogInterface, i: Int ->
            if (addOnsAdapter!!.selectedAddOns.isEmpty() && productsAdapter!!.selectedProducts.isEmpty()) {
                Toast.makeText(requireContext(), "No Selected Items.", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
                return@setPositiveButton
            }
            val total = binding!!.totalAmount!!
            val paidAmountStr = binding!!.payedAmount
            if (paidAmountStr == null || paidAmountStr.isEmpty() || paidAmountStr.toDouble() < total) {
                Toast.makeText(requireContext(), "Invalid Paid Amount.", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            val paidAmount = binding!!.payedAmount!!.toFloat()
            val zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Manila"))

            val month = zonedDateTime.monthValue
            val year = zonedDateTime.year
            val dayOfMonth = zonedDateTime.dayOfMonth
            viewmodel!!.sell(
                branch.branch_id,
                productsAdapter!!.selectedProducts,
                addOnsAdapter!!.selectedAddOns,
                year,
                month,
                dayOfMonth,
                total,
                paidAmount.toDouble(),
                object : OnSaleStatus {
                    override fun success(completeSaleInfo: CompleteSaleInfo?) {
                        onSaleFinished.onSuccess(completeSaleInfo!!)
                    }

                    override fun failed(
                        fatalExceptions: Exception?,
                        itemNameAndError: HashMap<String?, String?>?
                    ) {
                        onSaleFinished.onFail(fatalExceptions ?: Exception(), itemNameAndError ?: HashMap())
                    }
                })
        }

        dialogBuilder.setNegativeButton(
            "Cancel"
        ) { dialogInterface: DialogInterface?, i: Int ->
            this.dismiss()
        }

        binding!!.totalAmount = productsAdapter!!.totalPrice
        dialogBuilder.setView(binding!!.root)

        return dialogBuilder.create()
    }


    fun interface OnTotalAmountChanged {
        fun onChanged(total: Double?)
    }

    interface OnSaleFinished {
        fun onSuccess(completeSaleInfo: CompleteSaleInfo)
        fun onFail(e: Exception, itemNameAndErrors: HashMap<String?, String?>)
    }
}