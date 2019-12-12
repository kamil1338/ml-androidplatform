package com.github.kamil1338.classifying_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.classifying_ui.databinding.FragmentClassifyingBinding
import com.github.kamil1338.recording_app.core_ui.DataBindingFragment
import javax.inject.Inject


class ClassifyingFragment : DataBindingFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ClassifyingViewModel> { viewModelFactory }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentClassifyingBinding.inflate(inflater, container, false).also {
        lifecycle.addObserver(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.startClassifying()
        }
    }
}
