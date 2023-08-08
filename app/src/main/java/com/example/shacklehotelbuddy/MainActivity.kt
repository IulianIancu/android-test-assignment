@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shacklehotelbuddy

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shacklehotelbuddy.ui.theme.ShackleHotelBuddyTheme
import com.iulian.iancu.domain.HotelQueryEntity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShackleHotelBuddyTheme {
                MainScreen()
            }
        }
        viewModel.errorState.observe(this, ::onErrorStateChanged)
        viewModel.searchCompleteState.observe(this, ::onSearchCompleteStateChanged)
    }

    private fun onErrorStateChanged(state: ErrorState?) {
        val error = when (state) {
            ErrorState.AdultError -> getString(R.string.incorrect_adult)
            ErrorState.CheckInError -> getString(R.string.incorrect_check_in)
            ErrorState.NetworkError -> getString(R.string.network_error)
            ErrorState.CheckOutError -> getString(R.string.incorrect_check_out)
            ErrorState.ChildError -> getString(R.string.incorrect_child)
            else -> {
                return
            }
        }
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    private fun onSearchCompleteStateChanged(state: SearchCompleteState?) {
        if (state == SearchCompleteState.Complete) {
            viewModel.onMovedToNextScreen()
            val intent = Intent(this, HotelListActivity::class.java)
            startActivity(intent)
        }
    }

    @Composable
    fun MainScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.background),
                    contentScale = ContentScale.FillWidth
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.select_guests),
                    style = ShackleHotelBuddyTheme.typography.mainTitle,
                    textAlign = TextAlign.Center,
                    color = ShackleHotelBuddyTheme.colors.white,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                RowCheckIn()
                Divider()
                RowCheckOut()
                Divider()
                RowAdults()
                Divider()
                RowChildren()

                Text(
                    text = stringResource(id = R.string.recent_searches),
                    style = ShackleHotelBuddyTheme.typography.subtitle,
                    textAlign = TextAlign.Center,
                    color = ShackleHotelBuddyTheme.colors.white,
                    modifier = Modifier.padding(bottom = 16.dp, top = 24.dp)
                )

                val previousSearches = viewModel.previousSearches.collectAsState()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(previousSearches.value) {
                        RecentSearchItem(it)
                    }
                }

                Button(
                    onClick = { viewModel.trySearchingHotels() },
                    colors = ButtonDefaults.buttonColors(containerColor = ShackleHotelBuddyTheme.colors.teal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = stringResource(id = R.string.search))
                }
            }


        }
    }

    @Composable
    fun RecentSearchItem(hotelQueryEntity: HotelQueryEntity) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(ShackleHotelBuddyTheme.colors.white)
                .clickable { viewModel.showOldSearch(hotelQueryEntity) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.manage_history),
                tint = ShackleHotelBuddyTheme.colors.teal,
                contentDescription = stringResource(id = R.string.recent_search_icon),
                modifier = Modifier
                    .weight(0.1f)
                    .padding(8.dp)
                    .height(30.dp)
            )
            Divider(
                modifier = Modifier
                    .height(50.dp)
                    .width(1.dp)
            )
            Text(
                text = "${hotelQueryEntity.checkInDate} ${hotelQueryEntity.checkOutDate}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.3f)
                    .padding(top = 16.dp)
            )
            Text(
                text = "${hotelQueryEntity.nrAdults} " +
                        (if (hotelQueryEntity.nrAdults > 1) "adults" else "adult") +
                        " ${hotelQueryEntity.nrChildren} " +
                        if (hotelQueryEntity.nrChildren > 1) "children" else "child",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(0.3f)
                    .padding(top = 16.dp)
            )
        }
    }

    @Composable
    fun RowCheckIn() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp))
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.event_upcoming),
                    contentDescription = stringResource(id = R.string.check_in_date_icon)
                )
                Text(
                    text = stringResource(id = R.string.check_in_date),
                    style = ShackleHotelBuddyTheme.typography.bodyMedium,
                    color = ShackleHotelBuddyTheme.colors.grayText,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Divider(
                modifier = Modifier
                    .height(50.dp)
                    .width(1.dp)
            )

            val currentCheckInDate = viewModel.currentCheckInDate.collectAsState()
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this@MainActivity,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    viewModel.onNewCheckInDate("$dayOfMonth/${month + 1}/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            Text(
                text = currentCheckInDate.value,
                style = ShackleHotelBuddyTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = ShackleHotelBuddyTheme.colors.grayText,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(topEnd = 18.dp))
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .padding(8.dp)
                    .clickable {
                        datePickerDialog.show()
                    }
            )

        }
    }

    @Composable
    fun RowCheckOut() {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.event_available),
                    contentDescription = stringResource(id = R.string.check_out_date_icon)
                )
                Text(
                    text = stringResource(id = R.string.check_out_date),
                    style = ShackleHotelBuddyTheme.typography.bodyMedium,
                    color = ShackleHotelBuddyTheme.colors.grayText,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Divider(
                modifier = Modifier
                    .height(50.dp)
                    .width(1.dp)
            )

            val currentCheckOutDate = viewModel.currentCheckOutDate.collectAsState()
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this@MainActivity,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    viewModel.onNewCheckOutDate("$dayOfMonth/${month + 1}/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            Text(
                text = currentCheckOutDate.value,
                style = ShackleHotelBuddyTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = ShackleHotelBuddyTheme.colors.grayText,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .padding(8.dp)
                    .clickable {
                        datePickerDialog.show()
                    }
            )

        }
    }

    @Composable
    fun RowAdults() {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = stringResource(id = R.string.adults_icon)
                )
                Text(
                    text = stringResource(id = R.string.adults),
                    style = ShackleHotelBuddyTheme.typography.bodyMedium,
                    color = ShackleHotelBuddyTheme.colors.grayText,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Divider(
                modifier = Modifier
                    .height(50.dp)
                    .width(1.dp)
            )
            val currentAdultCount = viewModel.currentAdultCount.collectAsState()
            BasicTextField(
                value = currentAdultCount.value,
                onValueChange = { viewModel.onAdultCountChanged(it) },
                textStyle = ShackleHotelBuddyTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .padding(8.dp)
            )
        }
    }

    @Composable
    fun RowChildren() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(bottomStart = 18.dp))
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.supervisor_account),
                    contentDescription = stringResource(id = R.string.children_icon)
                )
                Text(
                    text = stringResource(id = R.string.children),
                    style = ShackleHotelBuddyTheme.typography.bodyMedium,
                    color = ShackleHotelBuddyTheme.colors.grayText,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Divider(
                modifier = Modifier
                    .height(50.dp)
                    .width(1.dp)
            )
            val currentChildCount = viewModel.currentChildCount.collectAsState()
            BasicTextField(
                value = currentChildCount.value,
                onValueChange = { viewModel.onChildCountChanged(it) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                textStyle = ShackleHotelBuddyTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(bottomEnd = 10.dp))
                    .height(50.dp)
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .padding(8.dp)
            )

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ShackleHotelBuddyTheme {
            MainScreen()
        }
    }
}



