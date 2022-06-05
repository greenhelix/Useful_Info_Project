package com.greenhelix.module.howtomapapi.ui.network

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.greenhelix.module.howtomapapi.R
import com.greenhelix.module.howtomapapi.databinding.FragmentConnectBinding
import com.greenhelix.module.howtomapapi.databinding.FragmentNaverMapBinding
import com.greenhelix.module.howtomapapi.network.*
import com.greenhelix.module.howtomapapi.ui.home.MapViewModel

class ConnectFragment : Fragment() {

    private lateinit var connectViewModel: ConnectViewModel
    private var _binding : FragmentConnectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectBinding.inflate(inflater, container, false)
        val root : View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPrefill.setOnClickListener {
            binding.edittextServerUri.setText(MQTT_SERVER_URI)
            binding.edittextClientId.setText(MQTT_CLIENT_ID)
            binding.edittextUsername.setText(MQTT_USERNAME)
            binding.edittextPassword.setText(MQTT_PWD)
        }

        binding.buttonClean.setOnClickListener {
            binding.edittextServerUri.setText("")
            binding.edittextClientId.setText("")
            binding.edittextUsername.setText("")
            binding.edittextPassword.setText("")
        }

        binding.buttonConnect.setOnClickListener {
            val serverURIFromEditText   = binding.edittextServerUri.text.toString()
            val clientIDFromEditText    = binding.edittextClientId.text.toString()
            val usernameFromEditText    = binding.edittextUsername.text.toString()
            val pwdFromEditText         = binding.edittextPassword.text.toString()

            val mqttCredentialsBundle = bundleOf(MQTT_SERVER_URI_KEY    to serverURIFromEditText,
                MQTT_CLIENT_ID_KEY     to clientIDFromEditText,
                MQTT_USERNAME_KEY      to usernameFromEditText,
                MQTT_PWD_KEY           to pwdFromEditText)

            findNavController().navigate(R.id.action_navigation_connect_to_navigation_client, mqttCredentialsBundle)
        }
    }
}