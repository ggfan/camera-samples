package com.example.android.camerax.advanced.fragments

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.camerax.advanced.R
import com.android.example.camerax.advanced.databinding.FragmentSelectorBinding
import com.example.android.camera.utils.GenericListAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [SelectorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectorFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _selectorViewBinding:FragmentSelectorBinding? = null
    private val selectorViewBinding get() = _selectorViewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _selectorViewBinding = FragmentSelectorBinding.inflate(inflater,container,false)
        return selectorViewBinding.root
    }

    // to be updated later with array of (string, action)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectorStrings = listOf ("TFLite","Barcode Scanner")
        // create the adapter to QualitySelector RecyclerView
        selectorViewBinding.sampleSelector.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GenericListAdapter(
                selectorStrings,
                itemLayoutId = android.R.layout.simple_list_item_1
            ) { holderView, qcString, position ->

                holderView.findViewById<TextView>(android.R.id.text1)?. apply {
                    text = qcString
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.txWhite))
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                }

                holderView.setOnClickListener {
                    Navigation.findNavController(requireActivity(), R.id.fragment_container)
                        .navigate(SelectorFragmentDirections.actionSelectorToTflite())
                }
            }
        }
    }
}