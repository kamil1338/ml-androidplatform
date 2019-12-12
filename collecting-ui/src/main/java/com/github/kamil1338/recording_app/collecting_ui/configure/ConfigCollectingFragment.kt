package com.github.kamil1338.recording_app.collecting_ui.configure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.github.kamil1338.recording_app.collecting_ui.collecting.CollectingViewModel
import com.github.kamil1338.recording_app.collecting_ui.databinding.FragmentConfigCollectingBinding
import com.github.kamil1338.recording_app.core_ui.DataBindingFragment
import kotlinx.android.synthetic.main.fragment_config_collecting.*
import javax.inject.Inject

class ConfigCollectingFragment : DataBindingFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val collectingViewModel by viewModels<CollectingViewModel> { viewModelFactory }
    private val configCollectingViewModel by viewModels<ConfigCollectingViewModel> { viewModelFactory }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentConfigCollectingBinding.inflate(inflater, container, false).also {
        lifecycle.addObserver(collectingViewModel)
        it.viewModel = configCollectingViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start_collecting_button.setOnClickListener {
            collectingViewModel.startCollectingData(configCollectingViewModel.toUseCaseConfig())
        }
    }
}