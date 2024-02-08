package com.demo.advanced.practical.view.activity

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.advanced.practical.data.response.Product
import com.demo.advanced.practical.databinding.ActivityMainBinding
import com.demo.advanced.practical.enum.ApiStatus
import com.demo.advanced.practical.view.adapter.ProductAdapter
import com.demo.advanced.practical.view.base.BaseActivity
import com.demo.advanced.practical.view.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val mainViewModel: MainViewModel by viewModels()
    lateinit var productAdapter: ProductAdapter
    private val productArrayList: ArrayList<Product> = arrayListOf()
    override fun setBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mManageToolbar(binding.includeLayToolbar, "Products", false)
        binding.includeLayToolbar.imgBack.visibility = View.GONE

        binding.includeLayToolbar.ivMap.setOnClickListener {
            startActivity(Intent(this, MapsViewActivity::class.java))
        }
        binding.edSearchProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                productAdapter.filter(s.toString())
            }
        })
        initAdapter()
        mSetUpObserver()
    }

    private fun initAdapter() {
        productAdapter = ProductAdapter(this, productArrayList)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProducts.layoutManager = layoutManager
        binding.rvProducts.adapter = productAdapter
    }


    private fun mSetUpObserver() {
        mainViewModel.mActivityDataList.observe(this) { it ->
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    it.data?.let {
                        binding.progressBar.visibility = View.GONE
                        productArrayList.addAll(it)
                        productAdapter.notifyDataSetChanged()
                        productAdapter.filter(null)
                    }
                }

                ApiStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
//                    messageSnackBar("Loading please wait...", binding.root)
                }

                ApiStatus.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    messageSnackBar(it.message!!, binding.root)
                }
            }
        }
    }
}