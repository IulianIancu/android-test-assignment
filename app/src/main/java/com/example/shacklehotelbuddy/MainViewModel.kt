package com.example.shacklehotelbuddy

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iulian.iancu.domain.GetNewHotelsUseCase
import com.iulian.iancu.domain.GetPreviousSearchesUseCase
import com.iulian.iancu.domain.HotelEntity
import com.iulian.iancu.domain.HotelQueryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNewHotelsUseCase: GetNewHotelsUseCase,
    private val getPreviousSearchesUseCase: GetPreviousSearchesUseCase
) : ViewModel() {

    private val _currentCheckInDate = MutableStateFlow("DD/MM/YYYY")
    val currentCheckInDate = _currentCheckInDate.asStateFlow()

    private val _currentCheckOutDate = MutableStateFlow("DD/MM/YYYY")
    val currentCheckOutDate = _currentCheckOutDate.asStateFlow()

    private val _currentAdultCount = MutableStateFlow("1")
    val currentAdultCount = _currentAdultCount.asStateFlow()

    private val _currentChildCount = MutableStateFlow("0")
    val currentChildCount = _currentChildCount.asStateFlow()

    private val _previousSearches = MutableStateFlow<List<HotelQueryEntity>>(emptyList())
    val previousSearches = _previousSearches.asStateFlow()

    private val _errorState = MutableLiveData<ErrorState>(ErrorState.NoError)
    val errorState: LiveData<ErrorState> get() = _errorState

    private val _searchCompleteState =
        MutableLiveData<SearchCompleteState>(SearchCompleteState.Incomplete)
    val searchCompleteState: LiveData<SearchCompleteState> get() = _searchCompleteState

    private val _hotelList = MutableStateFlow<List<HotelEntity>>(emptyList())
    val hotelList = _hotelList.asStateFlow()

    init {
        viewModelScope.launch {
            getPreviousSearchesUseCase().collect {
                _previousSearches.value = it
            }
        }
    }

    fun onMovedToNextScreen() {
        _searchCompleteState.value = SearchCompleteState.Incomplete
    }

    fun onNewCheckInDate(newDate: String) {
        _currentCheckInDate.value = newDate
    }

    fun onNewCheckOutDate(newDate: String) {
        _currentCheckOutDate.value = newDate
    }

    fun onAdultCountChanged(newCount: String) {
        _currentAdultCount.value = newCount
    }

    fun onChildCountChanged(newCount: String) {
        _currentChildCount.value = newCount
    }

    fun showOldSearch(oldSearch: HotelQueryEntity) {
        _hotelList.value = oldSearch.results
        _searchCompleteState.value = SearchCompleteState.Complete
    }

    fun trySearchingHotels() {
        //try to parse the dates as a date
        try {
            SimpleDateFormat(HotelQueryEntity.DATE_FORMAT, Locale.getDefault()).parse(
                _currentCheckInDate.value
            )
        } catch (e: ParseException) {
            _errorState.value = ErrorState.CheckInError
            return
        }

        try {
            SimpleDateFormat(HotelQueryEntity.DATE_FORMAT, Locale.getDefault()).parse(
                _currentCheckOutDate.value
            )
        } catch (e: ParseException) {
            _errorState.value = ErrorState.CheckOutError
            return
        }

        try {
            _currentAdultCount.value.toInt()
        } catch (e: NumberFormatException) {
            _errorState.value = ErrorState.AdultError
            return
        }
        try {
            _currentChildCount.value.toInt()
        } catch (e: NumberFormatException) {
            _errorState.value = ErrorState.ChildError
            return
        }

        //If we get to this point this means there are no errors
        viewModelScope.launch {
            try {
                _hotelList.value = getNewHotelsUseCase(
                    _currentCheckInDate.value,
                    _currentCheckOutDate.value,
                    _currentAdultCount.value.toInt(),
                    _currentChildCount.value.toInt()
                )
                _searchCompleteState.value = SearchCompleteState.Complete
            } catch (e: NetworkErrorException) {
                Timber.e(e)
                _errorState.value = ErrorState.NetworkError
            }

        }

    }
}

sealed class SearchCompleteState {
    object Complete : SearchCompleteState()
    object Incomplete : SearchCompleteState()
}

sealed class ErrorState {
    object NoError : ErrorState()
    object CheckInError : ErrorState()
    object CheckOutError : ErrorState()
    object AdultError : ErrorState()
    object ChildError : ErrorState()
    object NetworkError : ErrorState()
}