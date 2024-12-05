@file:OptIn(ExperimentalMaterial3Api::class)

package com.denproj.posmanongjaks.fragments.sales

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.isNotNull
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import coil.compose.AsyncImage
import com.denproj.posmanongjaks.databinding.FragmentSalesHistoryBinding
import com.denproj.posmanongjaks.model.ItemNameAndAmountToReduce
import com.denproj.posmanongjaks.model.Sale
import com.denproj.posmanongjaks.model.SaleItem
import com.denproj.posmanongjaks.model.SaleProduct
import com.denproj.posmanongjaks.session.SessionSimple
import com.denproj.posmanongjaks.util.NetworkStatusMonitor
import com.denproj.posmanongjaks.util.OnDataReceived
import com.denproj.posmanongjaks.util.TimeUtil
import com.denproj.posmanongjaks.viewModel.SaleHistoryViewModel
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.lang.Exception
import kotlin.jvm.Throws

@AndroidEntryPoint
class SalesHistoryFragment : Fragment() {
    val viewModel: SaleHistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSalesHistoryBinding = FragmentSalesHistoryBinding.inflate(layoutInflater)


        binding.salesComposeView.setContent {
            SalesHistoryScreen(viewModel)
        }

        return binding.root
    }
}

@Composable
fun Graph(map: HashMap<String, Int>) {
    if(map.isEmpty()) {
        return
    }
    val chartData: ArrayList<BarData> = ArrayList()
    val sortedData = map.entries.sortedBy { it.value }

    var counter = 0

    sortedData.forEach { it ->
        val barChartData = BarData(point = Point(y = it.value.toFloat(), x = counter.toFloat()))
        chartData.add(barChartData)
        counter++
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(chartData.size - 1)
        .bottomPadding(40.dp)
        .axisLabelAngle(40f)
        .startDrawPadding(45.dp)
        .labelData { index ->
            sortedData[index].key
        }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(chartData.size - 1)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { it -> sortedData[it].value.toString() }
        .build()

    val barChartData = BarChartData(
        chartData = chartData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color.Transparent,
        barStyle = BarStyle(
            paddingBetweenBars = 50.dp,
            barWidth = 20.dp
        ),
        showXAxis = true,
        showYAxis = true,
        horizontalExtraSpace = 10.dp
    )
    BarChart(
        modifier = Modifier
            .height(300.dp).background(Color.White),
        barChartData = barChartData,
    )
}

@Preview
@Composable
fun SalesHistoryScreen (
    viewModel: SaleHistoryViewModel = viewModel(),
    networkStatusMonitor: NetworkStatusMonitor = NetworkStatusMonitor()) {
    val session: SessionSimple? = viewModel.session.collectAsState().value
    val sales: List<Sale> = viewModel.sales.collectAsState().value
    val statistics = viewModel.stats.collectAsState()
    var isRefreshing = viewModel.isLoading

    if (session != null) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White))
        {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(modifier = Modifier.padding(10.dp), fontSize = 20.sp, fontFamily = FontFamily.SansSerif, text = "Sales As Of ${TimeUtil.getCurrentDateTitleFormat()}")
                Box (modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
                    Graph(statistics.value)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
                    PullToRefreshBox(
                        modifier = Modifier.fillMaxSize().background(Color.White),
                        isRefreshing = isRefreshing.value,
                        onRefresh = {
                            isRefreshing.value = true
                            viewModel.getStatistics {
                                isRefreshing.value = false
                            }})
                    {
                        LazySalesListColumn(sales)
                    }
                }
            }
        }
    }
}

@Composable
fun LazySalesListColumn(sales: List<Sale>) {
    val total = sales.sumOf { it.total }
    LazyColumn (modifier = Modifier
        .fillMaxSize()) {
        items (sales) {
            SaleCard(it)
        }
    }
}


@Composable
fun SaleCard(sale: Sale) {
    var openDialog by remember { mutableStateOf(false) }
    Card (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White),
        onClick = {
            openDialog = true
        }
    ) {
        Column (modifier = Modifier.padding(16.dp)) {
            Text(text = "SaleID: ${sale.saleId.toString()}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Total: ${sale.total}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Paid: ${sale.paidAmount}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Change: ${sale.change}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Date: ${sale.date}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (openDialog) {
        Dialog(
            onDismissRequest = {openDialog = false}
        ) {
            Surface (
                modifier = Modifier.wrapContentHeight().padding(10.dp),
                color = Color.White,
                shape = RoundedCornerShape(12.dp)) {
                SoldProductsAndAddOnsList(saleId = sale.saleId!!, viewModel())
            }
        }
    }
}

@Composable
fun SoldProductsAndAddOnsList(saleId: Int, viewModel: SaleHistoryViewModel) {
    val saleProducts = remember { mutableStateOf<List<SaleProduct>>(emptyList()) }
    val saleItems = remember { mutableStateOf<List<SaleItem>>(emptyList()) }

    LaunchedEffect(saleId) {
        viewModel.getSalesProduct(saleId, object : OnDataReceived<List<SaleProduct>> {
            override fun onSuccess(result: List<SaleProduct>?) {
                saleProducts.value = result ?: emptyList()
            }

            override fun onFail(e: Exception?) {

            }
        })
        viewModel.getSalesItem(saleId, object : OnDataReceived<List<SaleItem>> {
            override fun onSuccess(result: List<SaleItem>?) {
                saleItems.value = result ?: emptyList()
            }

            override fun onFail(e: Exception?) {

            }
        })
    }

    Column (modifier = Modifier.padding(13.dp)) {
        if (saleProducts.value.isNotEmpty()) {
            Box {
                SoldProducts(saleProducts.value)
            }

            Spacer(modifier = Modifier.height(10.dp))
        }


        if (saleItems.value.isNotEmpty()) {
            Box {
                SoldAddOns(saleItems.value)
            }
        }
    }
}

@Composable
fun SoldAddOns(saleItems: List<SaleItem>) {
    Column (modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
        Text(text = "Add Ons: ", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items (saleItems.size) {
                SoldCard(saleItems[it].itemName!!, saleItems[it].amount, saleItems[it].imagePath)
            }
        }
    }
}

@Composable
fun SoldProducts(saleProducts: List<SaleProduct>) {
    Column (modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
        Text(text = "Products: ", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(saleProducts.size) {
                SoldCard(saleProducts[it].name!!, saleProducts[it].amount, saleProducts[it].imagePath)
            }
        }
    }
}

@Composable
fun SoldCard(name: String, amount: Int, image_path: String?) {
    Row {
        Text(text = name, style = MaterialTheme.typography.bodyLarge)
        Text(text = " x ", style = MaterialTheme.typography.bodyLarge)
        Text(text = amount.toString(), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ImageLoader(name: String, path: String?) {
    val uri: MutableState<Uri?> = remember { mutableStateOf(null) }
    LaunchedEffect (name+path) {
        if (path != null) {
            val storage = FirebaseStorage.getInstance()
            storage.getReference(path).downloadUrl.addOnSuccessListener {
                uri.value = it
            }
        }
    }
    AsyncImage(
        model = uri.value,
        contentDescription = "image of $name",
        modifier = Modifier.size(50.dp),
        contentScale = ContentScale.FillBounds
    )
}