package com.denproj.posmanongjaks.fragments.branch

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBranchBinding.inflate(inflater)
        binding.stocksList.setContent {
            BranchFragmentScreen()
        }

        return binding.root
    }
}

@Composable
fun BranchFragmentScreen(viewmodel: BranchFragmentViewmodel = viewModel()) {
    val stocksList = viewmodel.stocks.value
    StocksLazyColumn(stocksList)
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
                StockImage(item.item_name!!, item.image_url!!)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box (modifier = Modifier.fillMaxWidth().wrapContentHeight(), contentAlignment = Alignment.Center) {
                Text(text = "Stock: ${item.item_quantity} ${item.item_unit}", fontSize = 15.sp, modifier = Modifier.wrapContentWidth())
            }

        }
    }
}

@Composable
fun StockImage(imageName: String, imageUri: Uri) {
    AsyncImage(model = imageUri, contentDescription = "Image of item $imageName",
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop)
}
