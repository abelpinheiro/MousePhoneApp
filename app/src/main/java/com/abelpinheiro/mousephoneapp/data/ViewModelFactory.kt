package com.abelpinheiro.mousephoneapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abelpinheiro.mousephoneapp.ui.home.HomeViewModel
import com.abelpinheiro.mousephoneapp.ui.trackpad.TrackpadViewModel

class ViewModelFactory(
    private val repository: ConnectionRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(TrackpadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrackpadViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}