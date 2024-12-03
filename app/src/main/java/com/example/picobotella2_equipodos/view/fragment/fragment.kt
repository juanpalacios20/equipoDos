package com.example.picobotella2_equipodos.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.FragmentRateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateFragment : Fragment() {

    private lateinit var binding: FragmentRateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout usando DataBinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rate, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webview.loadUrl("https://play.google.com/store/apps/details?id=com.nequi.MobileApp")
    }
}
