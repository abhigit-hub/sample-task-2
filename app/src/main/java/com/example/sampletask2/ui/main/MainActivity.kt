package com.example.sampletask2.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.sampletask2.MyApp
import com.example.sampletask2.R
import com.example.sampletask2.databinding.ActivityMainBinding
import com.example.sampletask2.di.component.DaggerActivityComponent
import com.example.sampletask2.di.module.ActivityModule
import com.example.sampletask2.ui.main.detail.DetailFragment
import com.example.sampletask2.ui.main.home.HomeFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var viewModel: MainSharedViewModel

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        setupObservers()
        setupView()
        viewModel.onViewCreated()
    }

    private fun injectDependencies() {
        DaggerActivityComponent
            .builder()
            .applicationComponent((application as MyApp).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }

    private fun setupView() {
        binding.viewModel = viewModel
        addChildFragment()
    }

    private fun addChildFragment() {
        supportFragmentManager.findFragmentByTag(HomeFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, HomeFragment.newInstance(), HomeFragment.TAG)
            .commitAllowingStateLoss()
    }

    private fun setupObservers() {
        viewModel.getSelectedStory().observe(this, Observer {
            it?.let { showDetailFragment() }
        })
    }

    private fun showDetailFragment() {
        DetailFragment.getInstance().show(supportFragmentManager, DetailFragment.TAG)
    }
}