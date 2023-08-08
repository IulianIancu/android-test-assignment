@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shacklehotelbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shacklehotelbuddy.ui.theme.ShackleHotelBuddyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotelListActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShackleHotelBuddyTheme {
                HotelList()
            }
        }
    }

    @Composable
    fun HotelList() {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.search_results))
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            finish()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = stringResource(id = R.string.go_back)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }) { values ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(values)
            ) {
                items(5) {
                    HotelItem()
                }
            }
        }
    }

    @Composable
    fun HotelItem() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AsyncImage(
                model = "https://upload.wikimedia.org/wikipedia/commons/1/15/Cat_August_2010-4.jpg",
                placeholder = painterResource(id = R.drawable.background),
                contentDescription = stringResource(id = R.string.hotel_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Beverly Hotel",
                        style = ShackleHotelBuddyTheme.typography.bodyMedium,
                        color = ShackleHotelBuddyTheme.colors.black
                    )
                    Text(
                        text = "Amsterdam, Netherlands",
                        style = ShackleHotelBuddyTheme.typography.bodyMedium,
                        color = ShackleHotelBuddyTheme.colors.grayText
                    )
                    Text(
                        text = "£100 night",
                        style = ShackleHotelBuddyTheme.typography.bodyMedium,
                        color = ShackleHotelBuddyTheme.colors.black
                    )
                }
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = stringResource(id = R.string.hotel_rating)
                    )
                    Text(
                        text = "4.5",
                        style = ShackleHotelBuddyTheme.typography.bodyMedium,
                        color = ShackleHotelBuddyTheme.colors.black
                    )
                }
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun HotelListPreview() {
        ShackleHotelBuddyTheme {
            HotelList()
        }
    }

}


