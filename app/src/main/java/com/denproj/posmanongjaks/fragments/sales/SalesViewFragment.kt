package com.denproj.posmanongjaks.fragments.sales

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denproj.posmanongjaks.R
import com.denproj.posmanongjaks.adapter.AddOnRecyclerViewAdapter
import com.denproj.posmanongjaks.adapter.ProductsRecyclerViewAdapter
import com.denproj.posmanongjaks.databinding.FragmentSalesViewBinding
import com.denproj.posmanongjaks.dialog.CheckOutDialogFragment
import com.denproj.posmanongjaks.dialog.LoadingDialog
import com.denproj.posmanongjaks.model.Branch
import com.denproj.posmanongjaks.model.CompleteSaleInfo
import com.denproj.posmanongjaks.model.SaleItem
import com.denproj.posmanongjaks.model.SaleProduct
import com.denproj.posmanongjaks.session.Session
import com.denproj.posmanongjaks.session.SessionSimple
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel
import com.denproj.posmanongjaks.viewModel.SalesFragmentViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.function.Consumer

@AndroidEntryPoint
class SalesViewFragment : Fragment() {
    var productsRecyclerViewAdapter: ProductsRecyclerViewAdapter? = null

    lateinit var binding: FragmentSalesViewBinding
    var addOnRecyclerViewAdapter: AddOnRecyclerViewAdapter? = null

    val homeActivityViewmodel: HomeActivityViewmodel by activityViewModels<HomeActivityViewmodel>()
    val viewmodel: SalesFragmentViewmodel by viewModels()

    var loadingDialog: LoadingDialog = LoadingDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesViewBinding.inflate(inflater)



        setupAdapters()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                this@SalesViewFragment.homeActivityViewmodel.sessionStateFlow.collect {

                    if (it == null) {
                        return@collect
                    }

                    val branchId = it.branchId

                    setupAddOnRcv()
                    observeAddons()

                    setupProductRcv(branchId)
                    observeProducts()

                    setupViews(it)
                }
            }
        }

        viewmodel.errors.observe(viewLifecycleOwner) { exception ->
            Toast.makeText(requireContext(), exception?.toString() ?: "Something went wrong.", Toast.LENGTH_SHORT).show()
        }

        return binding!!.root
    }

    fun setupViews(session: SessionSimple) {
        binding!!.checkOutItems.setOnClickListener { view: View? ->
            showDialog()
            val branch = Branch(session.branchId!!, session.branchName, "Contact", "Manager", "Branch")
            CheckOutDialogFragment(
                branch,
                productsRecyclerViewAdapter!!.selectedProducts,
                addOnRecyclerViewAdapter!!.itemsMap,
                object : CheckOutDialogFragment.OnSaleFinished {
                    override fun onSuccess(completeSaleInfo: CompleteSaleInfo) {
                        hideDialog()
                        val builder = AlertDialog.Builder(requireContext())

                        builder.setTitle("Change: " + completeSaleInfo.sale.change).setMessage("Print a receipt")

                        builder.setPositiveButton("Print") { dialogInterface: DialogInterface?, i: Int ->
                            val navController =
                                findNavController(requireActivity(), R.id.homeFragmentContainerView)
                            navController.navigate(
                                SalesViewFragmentDirections.actionSalesViewToPrintActivity(
                                    formatReceipt(
                                        completeSaleInfo,
                                        branch, session.userId
                                    )
                                )
                            )
                        }

                        builder.setNegativeButton(
                            "Close"
                        ) { dialogInterface: DialogInterface, i: Int ->
                            dialogInterface.dismiss()
                        }

                        builder.create().show()

                        clearOrders()
                    }

                    override fun onFail(
                        e: Exception,
                        itemNameAndErrors: HashMap<String?, String?>
                    ) {
                        hideDialog()
                        itemNameAndErrors.forEach { (itemName: String?, errorMsg: String?) ->
                            showErrorDialog(itemName, errorMsg)
                        }
                    }

                }).show(childFragmentManager, "")
        }
    }

    override fun onResume() {
        showDialog()
        super.onResume()
    }

    fun observeProducts() {
        viewmodel.products.observe(viewLifecycleOwner) { products ->
            productsRecyclerViewAdapter!!.refreshAdapter(products ?: emptyList())
        }
    }

    fun observeAddons() {
        viewmodel.items.observe(viewLifecycleOwner) {items ->
            addOnRecyclerViewAdapter!!.refreshAdapter(items ?: emptyList())
            hideDialog()
        }
    }

    fun setupProductRcv(branchId: String?) {
        this.productsRecyclerViewAdapter = ProductsRecyclerViewAdapter(
            branchId,
            childFragmentManager
        )
        binding!!.menuRcv.adapter = productsRecyclerViewAdapter
    }

    fun setupAddOnRcv() {
        this.addOnRecyclerViewAdapter = AddOnRecyclerViewAdapter()
        binding!!.addOnList.adapter = addOnRecyclerViewAdapter
    }


    fun setupAdapters() {
        binding!!.menuRcv.layoutManager = LinearLayoutManager(requireContext())
        val manager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding!!.addOnList.layoutManager = manager
    }

    fun showDialog() {
        if (loadingDialog.isVisible) {
            hideDialog()
        }
        this.loadingDialog = LoadingDialog()
        loadingDialog.show(childFragmentManager, "")
    }

    fun hideDialog() {
        loadingDialog.dismiss()
    }

    fun clearOrders() {
        productsRecyclerViewAdapter!!.clearSelectedProducts()
        addOnRecyclerViewAdapter!!.clearSelectedItems()
    }

    fun formatReceipt(completeSaleInfo: CompleteSaleInfo, branch: Branch, uid: String?): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("[C]<font size='big'>Manong Jak's</font>\n")
        stringBuilder.append("[C]<font size='big'>Burger</font>")
        stringBuilder.append("\n[C]").append(branch.branch_name).append("\n")
            .append("[L]<b>Products</b>\n")

        completeSaleInfo.saleProducts.forEach(Consumer { saleProduct: SaleProduct ->
            stringBuilder.append("[L]").append(saleProduct.name).append("[R]x")
                .append(saleProduct.amount).append("\n")
        })

        stringBuilder.append("[L]<b>Add Ons</b>\n")
        completeSaleInfo.saleItems.forEach(Consumer { saleItem: SaleItem ->
            stringBuilder.append("[L]").append(saleItem.itemName).append("[R]x")
                .append(saleItem.amount).append("\n")
        })

        stringBuilder.append("[L]\n")
        stringBuilder.append("[L]Total:").append("[R]").append(completeSaleInfo.sale.total)
            .append(" Pesos\n")
        stringBuilder.append("[L]Change:").append("[R]").append(completeSaleInfo.sale.change)
            .append(" Pesos\n")
        stringBuilder.append("[L]Sale Id:").append("[R]").append(completeSaleInfo.sale.saleId)
            .append("\n")
        stringBuilder.append("[L]Sold By:").append("[R]UID: ").append(uid).append("\n")
        stringBuilder.append("[C]App Generated Receipt\n")
        return stringBuilder.toString()
    }

    fun showErrorDialog(title: String?, message: String?) {
        val errorDialog = AlertDialog.Builder(requireContext())
        errorDialog
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                "Okay"
            ) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
        errorDialog.create().show()
    }
}