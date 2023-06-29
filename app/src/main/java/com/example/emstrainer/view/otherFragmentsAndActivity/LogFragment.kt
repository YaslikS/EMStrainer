package com.example.emstrainer.view.otherFragmentsAndActivity

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.emstrainer.databinding.FragmentLogBinding
import com.example.emstrainer.presenter.SaveStates
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LogFragment : Fragment() {
    private var saveStates = SaveStates
    var fragmentLogBinding: FragmentLogBinding? = null
    var binding: FragmentLogBinding? = null
    private val logFragmentTAG = "LogFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogBinding.inflate(inflater, container, false)
        fragmentLogBinding = binding

        binding!!.logHeader.setOnClickListener { activity?.onBackPressed() }
        activity?.topShadowMain?.visibility = View.INVISIBLE
        logObserver()

        return binding!!.root
    }

    private fun logObserver() {
        CoroutineScope(Dispatchers.IO).launch {
            while(true) {
                delay(1000)
                launch(Dispatchers.Main) {
                    binding!!.textViewMultiLineLOG.movementMethod = ScrollingMovementMethod()
                    binding!!.textViewMultiLineLOG.text =
                        saveStates.LOG //viewFragLOG!!.scrollViewLogFrag.fullScroll(ScrollView.FOCUS_DOWN)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.topShadowMain?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        fragmentLogBinding = null
        super.onDestroyView()
    }
}