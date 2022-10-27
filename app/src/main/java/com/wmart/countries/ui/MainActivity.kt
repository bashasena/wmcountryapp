package com.wmart.countries.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wmart.countries.theme.ErrorhandlingTheme
import com.wmart.countries.R
import com.wmart.countries.model.Country
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ErrorhandlingTheme {
                val mainViewModel = hiltViewModel<MainViewModel>()
                val context = LocalContext.current
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state by mainViewModel.countryState.collectAsState()
                    mainViewModel.getCountryList()


                    state.let { state ->
                        when (state) {
                            CountryUIState.START -> {
                            }
                            CountryUIState.LOADING -> {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    CircularProgressIndicator(color = colorResource(id = R.color.purple_700))
                                }
                            }
                            is CountryUIState.FAILURE -> {
                                val message = state.message
                                LaunchedEffect(key1 = message) {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }

                            CountryUIState.SUCCESS -> {
                                CountryList(countryList = mainViewModel.countryListResponse)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CountryList(countryList: List<Country>) {
    var selectedIndex by remember { mutableStateOf(-1) }
    LazyColumn {

        itemsIndexed(items = countryList) { index, item ->
            CountryItem(country = item, index, selectedIndex) { i ->
                selectedIndex = i
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CountryItem() {
    val countryItem = Country(
        "United States of America",
        "Washington, D.C",
        "US",
        "US"
    )
    CountryItem(country = countryItem, 0, 0) { i ->
    }
}

@Composable
fun CountryItem(country: Country, index: Int, selectedIndex: Int, onClick: (Int) -> Unit) {

    val backgroundColor =
        if (index == selectedIndex) MaterialTheme.colors.primary else MaterialTheme.colors.background
    Card(
        modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth()
            .clickable { onClick(index) }
            .height(110.dp), shape = RoundedCornerShape(8.dp), elevation = 4.dp
    ) {
        Surface(color = backgroundColor) {

            Row(
                Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight()
                        .weight(0.8f)
                ) {
                    Text(
                        text = country.name,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = country.region,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .background(
                                Color.Magenta
                            )
                            .padding(4.dp)
                    )
                    Text(
                        text = "Capital:${country.capital}",
                        style = MaterialTheme.typography.body1,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Code:${country.code}",
                        style = MaterialTheme.typography.body1,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
        }
    }

}
