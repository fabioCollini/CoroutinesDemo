package it.codingjam.coroutines.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <reified VM : ViewModel> viewModel(activity: androidx.fragment.app.FragmentActivity, crossinline factory: () -> VM): Lazy<VM> {
    return lazy {
        ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return factory() as T
            }
        }).get(VM::class.java)
    }
}