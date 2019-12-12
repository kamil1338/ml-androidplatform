package com.github.kamil1338.recording_app.collecting_ui.get_started

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.kamil1338.recording_app.collecting_ui.R
import com.github.kamil1338.recording_app.collecting_ui.databinding.FragmentGetStartedBinding
import com.github.kamil1338.recording_app.core_ui.DataBindingFragment
import kotlinx.android.synthetic.main.fragment_get_started.*
import javax.inject.Inject


class CollectingGetStartedFragment : DataBindingFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<CollectingGetStartedViewModel> { viewModelFactory }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentGetStartedBinding.inflate(inflater, container, false).also {
        lifecycle.addObserver(viewModel)
        it.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collect_button.setOnClickListener {
            findNavController().navigate(R.id.action_collectingGetStartedFragment_to_prepareCollectingFragment)
        }
    }
}