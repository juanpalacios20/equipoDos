package com.example.picobotella2_equipodos.view.fragment

import android.content.Context
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
    private var onRateButtonClickedListener: OnRateButtonClickedListener? = null

    interface OnRateButtonClickedListener {
        fun onRateButtonClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRateButtonClickedListener) {
            onRateButtonClickedListener = context
        } else {
            throw RuntimeException("$context must implement OnRateButtonClickedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onRateButtonClickedListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webview.loadUrl("https://play.google.com/store/apps/details?id=com.nequi.MobileApp")
    }
}

