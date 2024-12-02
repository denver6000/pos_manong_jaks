package com.denproj.posmanongjaks.fragments.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.Gravity
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.wavechart.model.AxisPosition
import com.denproj.posmanongjaks.databinding.FragmentSalesHistoryBinding
import com.denproj.posmanongjaks.model.Sale
import com.denproj.posmanongjaks.session.SessionSimple
import com.denproj.posmanongjaks.util.NetworkStatusMonitor
import com.denproj.posmanongjaks.viewModel.SaleHistoryViewModel
import com.google.android.play.core.integrity.x
import dagger.hilt.android.AndroidEntryPoint

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
        .axisLabelAngle(20f)
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
            paddingBetweenBars = 20.dp,
            barWidth = 20.dp
        ),
        showXAxis = true,
        showYAxis = true,
        horizontalExtraSpace = 10.dp
    )



    BarChart(
        modifier = Modifier
            .height(300.dp),
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
    if (session != null) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box (modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
                Graph(statistics.value)
            }
            Box (modifier = Modifier.fillMaxSize()) {
                LazySalesListColumn(sales)
            }
        }
    }
}

@Composable
fun LazySalesListColumn(sales: List<Sale>) {
    val total = sales.sumOf { it.total }
    LazyColumn (modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 64.dp, top = 64.dp)) {
        items (sales) {
            SaleCard(it)
        }
    }
}

@Composable
fun SaleCard(sale: Sale) {
    Card (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(text = sale.saleId.toString(), modifier = Modifier.padding(16.dp))
    }
}