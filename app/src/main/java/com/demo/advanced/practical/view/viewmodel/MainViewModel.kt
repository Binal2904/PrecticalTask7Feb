package com.demo.advanced.practical.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.advanced.practical.data.response.Product
import com.demo.advanced.practical.repository.MainRepository
import com.demo.advanced.practical.util.ApiResource
import com.demo.advanced.practical.util.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository, private val networkHelper: NetworkHelper
) : ViewModel() {

    private val mGetMutableActivityData = MutableLiveData<ApiResource<ArrayList<Product>>>()
    val mActivityDataList: LiveData<ApiResource<ArrayList<Product>>> get() = mGetMutableActivityData

    init {
        mGetProductData()
    }

    private fun mGetProductData() {
        viewModelScope.launch {
            mGetMutableActivityData.postValue(ApiResource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getProducts().let {
                    if (it.isSuccessful) {
                        mGetMutableActivityData.postValue(ApiResource.success(it.body()?.products))
                    } else mGetMutableActivityData.postValue(
                        ApiResource.error(
                            it.errorBody().toString(), null
                        )
                    )
                }
            } else {
                mGetMutableActivityData.postValue(
                    ApiResource.error(
                        "No Active Internet Connection", null
                    )
                )
            }
        }
    }
}