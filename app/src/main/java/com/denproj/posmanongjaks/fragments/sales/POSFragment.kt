package com.denproj.posmanongjaks.fragments.sales

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.denproj.posmanongjaks.databinding.FragmentPOSBinding
import com.denproj.posmanongjaks.model.Item
import com.denproj.posmanongjaks.model.Product
import com.denproj.posmanongjaks.viewModel.POSFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class POSFragment : Fragment() {

    private lateinit var binding: FragmentPOSBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPOSBinding.inflate(inflater, container, false)
        binding.composeView.setContent {
            POSScreen()
        }
        return binding.root
    }
}

@Composable
@Preview
fun POSScreen() {
//    val productsList = viewmodel.productList.collectAsState()
//    val addOnsList = viewmodel.addOnsList.collectAsState()
    Box (modifier = Modifier.fillMaxSize()) {
        Box (modifier = Modifier.fillMaxSize().background(Color.Blue)) {
            ProductList()
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box (modifier = Modifier.wrapContentHeight().fillMaxWidth().background(Color.White)) {
            AddOnList()
        }
    }
}

@Composable
@Preview
fun ProductList(productList: List<Product> = createDummyProducts()) {
    LazyColumn {
        items(productList.size) {
            ProductCard(productList[it])
        }
    }

}


@Composable
@Preview
fun AddOnList(addOnsList: List<Item> = createDummyItems()) {
    LazyRow {
        items (addOnsList.size) {
            AddOnCard(addOnsList[it])
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(modifier = Modifier.padding(8.dp).fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box {
                Text(text = product.product_name!!)
            }

            Box {

            }

            Box {

            }
        }
    }
}

@Composable
fun AddOnCard (addOn: Item) {
    Card (modifier = Modifier.padding(8.dp).height(300.dp).fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box {
                Text(text = addOn.item_name!!)
            }
        }
    }
}

fun createDummyProducts(): List<Product> {
    return listOf(
        Product(
            product_id = 12342,
            product_name = "Product 1",
            product_image_path = "/images/product1.jpg",
            product_price = 10.99f,
            product_category = "Category A",
            recipes = HashMap()
        ),
        Product(
            product_id = 2L,
            product_name = "Product 2",
            product_image_path = "/images/product2.jpg",
            product_price = 15.49f,
            product_category = "Category B",
            recipes = HashMap()
        ),
        Product(
            product_id = 3L,
            product_name = "Product 3",
            product_image_path = "/images/product3.jpg",
            product_price = 7.25f,
            product_category = "Category A",
            recipes = HashMap()
        ),
        Product(
            product_id = 4L,
            product_name = "Product 4",
            product_image_path = "/images/product4.jpg",
            product_price = 20.00f,
            product_category = "Category C",
            recipes = HashMap()
        ),
        Product(
            product_id = 5L,
            product_name = "Product 5",
            product_image_path = "/images/product5.jpg",
            product_price = 5.75f,
            product_category = "Category B",
            recipes = HashMap()
        )
    )
}

fun createDummyItems(): List<Item> {
    return listOf(
        Item(
            item_image_path = "/images/item1.jpg",
            item_id = 1,
            item_name = "Item 1",
            item_category = "Category A",
            item_quantity = 5.0,
            item_unit = "kg",
            item_price = 20.0,
            ads_on = true
        ),
        Item(
            item_image_path = "/images/item2.jpg",
            item_id = 2,
            item_name = "Item 2",
            item_category = "Category B",
            item_quantity = 2.5,
            item_unit = "liters",
            item_price = 15.5,
            ads_on = false
        ),
        Item(
            item_image_path = "/images/item3.jpg",
            item_id = 3,
            item_name = "Item 3",
            item_category = "Category A",
            item_quantity = 10.0,
            item_unit = "pcs",
            item_price = 5.0,
            ads_on = true
        ),
        Item(
            item_image_path = "/images/item4.jpg",
            item_id = 4,
            item_name = "Item 4",
            item_category = "Category C",
            item_quantity = 1.0,
            item_unit = "kg",
            item_price = 50.0,
            ads_on = false
        ),
        Item(
            item_image_path = "/images/item5.jpg",
            item_id = 5,
            item_name = "Item 5",
            item_category = "Category B",
            item_quantity = 3.0,
            item_unit = "pack",
            item_price = 12.5,
            ads_on = true
        )
    )
}
