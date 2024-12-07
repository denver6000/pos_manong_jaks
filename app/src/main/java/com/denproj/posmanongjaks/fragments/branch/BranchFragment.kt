package com.denproj.posmanongjaks.fragments.branch

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.denproj.posmanongjaks.adapter.ItemsRecyclerViewAdapter
import com.denproj.posmanongjaks.databinding.FragmentBranchBinding
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.viewModel.BranchFragmentViewmodel
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class BranchFragment : Fragment() {
    lateinit var binding: FragmentBranchBinding;
    val viewmodel: BranchFragmentViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBranchBinding.inflate(inflater)

        binding.stocksList.setContent {
            BranchFragmentScreen(viewmodel)
        }

        return binding.root
    }
}

@Composable
fun LoadingDialog() {
    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchFragmentScreen(viewmodel: BranchFragmentViewmodel) {
    val stocksList = viewmodel.stocks.value
    val isRefreshing = remember { viewmodel.isRefreshing }
    val isLoading = viewmodel.isLoading.value
    if (isLoading) {
        LoadingDialog()
    } else {
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize().background(Color.White),
            isRefreshing = isRefreshing.value,
            onRefresh = {
                isRefreshing.value = true
                viewmodel.getStocks {
                    isRefreshing.value = false
                }
            }
        ) {
            StocksLazyColumn(stocksList)
        }
    }
}

@Composable
fun StocksLazyColumn(stocksList: List<Item>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(16.dp)) {

        items(stocksList.size) {
            StockCard(stocksList[it])
        }
    }
}

@Composable
fun StockCard(item: Item) {
    Card (
        modifier = Modifier.wrapContentSize().padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {

        Column (modifier = Modifier.wrapContentSize(Alignment.Center).padding(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth().wrapContentHeight(), contentAlignment = Alignment.Center) {
                Text(text = item.item_name!!, fontSize = 15.sp, modifier = Modifier.wrapContentWidth(), textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth().wrapContentHeight(), contentAlignment = Alignment.Center) {
                StockImage(item.item_name!!, item!!.item_image_path!!)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box (modifier = Modifier.fillMaxWidth().wrapContentHeight(), contentAlignment = Alignment.Center) {
                Text(text = "Stock: ${item.item_quantity} ${item.item_unit}", fontSize = 15.sp, modifier = Modifier.wrapContentWidth())
            }

        }
    }
}

@Composable
fun StockImage(imageName: String, imagePath: String) {
    val uri: MutableState<Uri?> = remember{ mutableStateOf(null) }
    LaunchedEffect (Unit) {
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        storage.getReference(imagePath).downloadUrl.addOnSuccessListener {
            uri.value = it
        }
    }
    AsyncImage(model = uri.value, contentDescription = "Image of item $imageName",
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop)
}
