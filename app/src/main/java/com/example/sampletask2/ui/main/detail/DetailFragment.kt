package com.example.sampletask2.ui.main.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.sampletask2.MyApp
import com.example.sampletask2.R
import com.example.sampletask2.databinding.FragmentDetailBinding
import com.example.sampletask2.di.component.DaggerFragmentComponent
import com.example.sampletask2.di.module.FragmentModule
import com.example.sampletask2.ui.main.MainSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class DetailFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "DetailFragment"

        private var INSTANCE: DetailFragment? = null

        private fun newInstance(): DetailFragment {
            val args = Bundle()
            val fragment = DetailFragment()
            fragment.arguments = args
            INSTANCE = fragment
            return fragment
        }

        fun getInstance(): DetailFragment {
            if (INSTANCE == null)
                INSTANCE = newInstance()
            return INSTANCE as DetailFragment
        }
    }

    @Inject
    lateinit var viewModel: MainSharedViewModel

    lateinit var binding: FragmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        setUpObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding.viewModel = viewModel
    }

    private fun injectDependencies() {
        DaggerFragmentComponent.builder()
            .applicationComponent((context?.applicationContext as MyApp).applicationComponent)
            .fragmentModule(FragmentModule(this))
            .build()
            .inject(this)
    }

    private fun setUpObservers() {}
}