package com.example.shacklehotelbuddy

import androidx.lifecycle.ViewModel
import com.iulian.iancu.domain.GetNewHotelsUseCase
import com.iulian.iancu.domain.GetPreviousSearchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNewHotelsUseCase: GetNewHotelsUseCase,
    private val getPreviousSearchesUseCase: GetPreviousSearchesUseCase
) : ViewModel() {

}