package com.denproj.posmanongjaks.fragments.stock_report

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.denproj.posmanongjaks.databinding.FragmentStockReportBinding
import com.denproj.posmanongjaks.model.ItemStockRecord
import com.denproj.posmanongjaks.viewModel.StockReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class StockReportFragment : Fragment() {


    private lateinit var binding: FragmentStockReportBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStockReportBinding.inflate(layoutInflater)
        binding.stocksList.setContent {
           StockReportFragmentScreen()
        }
        return binding.root
    }
}

@Composable
fun StockReportFragmentScreen(stockReportViewModel: StockReportViewModel = viewModel()) {
    val stockReportList = stockReportViewModel.state.value
    Column(modifier = Modifier.fillMaxSize()) {
        Row (modifier = Modifier.fillMaxWidth().wrapContentHeight()){

        }
        StockReportLazyColumn(stockReportList)
    }
}

@Composable
fun StockReportLazyColumn (stockReportList: List<ItemStockRecord>) {
    LazyColumn (modifier = Modifier.background(Color.White)) {

        items (stockReportList.size) { index ->
            StockReportCard(stockReportList[index])
        }

    }
}

@Composable
fun StockReportCard (itemStockRecord: ItemStockRecord) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = itemStockRecord.imageUri.toString(),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center

            ) {
                Text(
                    text = "${itemStockRecord.item_id}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Starting Stock: ${itemStockRecord.stock_at_start}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "Current Stock: ${itemStockRecord.stock_at_start - itemStockRecord.total_reduced_stock_amount}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun TimePickerButton(label: String, time: String, onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val currentTimeParts = time.split(":").map { it.toInt() }
    Button(modifier = Modifier.fillMaxWidth(), onClick = {
        val timePickerDialog = TimePickerDialog(context, {_, hour, minute ->
            val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
            onTimeSelected(formattedTime)
        }, currentTimeParts[0], currentTimeParts[1], true)
        timePickerDialog.show()
    }) {
        Text(text = label)
    }
}