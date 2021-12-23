/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camerax.advanced.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.camerax.advanced.R
import com.android.example.camerax.advanced.databinding.FragmentPermissionBinding
import com.example.android.camera.utils.GenericListAdapter

private var PERMISSIONS_REQUIRED = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO)

/**
 * This [Fragment] requests permissions and
 */
class PermissionFragment : Fragment() {
    private var _permissionViewBinding:FragmentPermissionBinding? = null
    private val permissionViewBiding get() = _permissionViewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // add the storage access permission request for Android 9 and below.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val permissionList = PERMISSIONS_REQUIRED.toMutableList()
            // TODO: confirm not using this later, or add it into the manifest file
            // permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            PERMISSIONS_REQUIRED = permissionList.toTypedArray()
        }

        if (!hasPermissions(requireContext())) {
            // Request camera-related permissions
            activityResultLauncher.launch(PERMISSIONS_REQUIRED)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _permissionViewBinding = FragmentPermissionBinding.inflate(inflater, container, false)
        permissionViewBiding.sampleSelector.apply {
            layoutManager = LinearLayoutManager(requireContext())
            isEnabled = hasPermissions(requireContext())
            adapter = GenericListAdapter(
                sampleCollection,
                itemLayoutId = R.layout.single_item_textview
            ) { holderView, dataItem, position ->
                holderView
                    .findViewById<TextView>(R.id.fragmentSelection)
                    ?.text = dataItem.first

                holderView.setOnClickListener {
                    Navigation
                      .findNavController(requireActivity(), R.id.fragment_container)
                      .navigate(dataItem.second)
                }
            }
        }
        return permissionViewBiding.root
    }
    companion object {
        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        /**
         * List of supported fragments in this sample, expand this list when new fragments are
         * added.
         */
        val sampleCollection = listOf (
            Pair("TFLite",PermissionFragmentDirections.actionPermissionToTflite()),
            Pair("Barcode Scanner", PermissionFragmentDirections.actionPermissionToTflite())
        )
    }
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PERMISSIONS_REQUIRED && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
}