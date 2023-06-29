package com.example.emstrainer.view.fitFragmentsAndActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.emstrainer.R
import com.example.emstrainer.databinding.FragmentChooseFitBinding
import com.example.emstrainer.presenter.SaveStates

class ChooseFitFragment : Fragment() {
    private var saveStates = SaveStates
    var fragmentChooseFitBinding: FragmentChooseFitBinding? = null
    var binding: FragmentChooseFitBinding? = null
    private val fitFragment = FitsFragment()
    private val ChooseFitFragmentTAG = "ChooseFitFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View { // Inflate the layout for this fragment
        binding = FragmentChooseFitBinding.inflate(inflater, container, false)
        fragmentChooseFitBinding = binding

        binding!!.cardViewEasy.setOnClickListener {
            saveStates.fitLevel = 1
            openFragment(fitFragment)
        }
        binding!!.cardViewMedium.setOnClickListener {
            saveStates.fitLevel = 2
            openFragment(fitFragment)
        }
        binding!!.cardViewHard.setOnClickListener {
            saveStates.fitLevel = 3
            openFragment(fitFragment)
        }
        (activity as FitProgrammsActivity?)?.binding!!.powerActivityHeader.text = "AR-тренировки"

        return binding!!.root
    }

    private fun openFragment(twoFrag: Fragment) {
        val fragmentManager: FragmentManager? = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.containerFitActivity, twoFrag)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onDestroyView() {
        fragmentChooseFitBinding = null
        super.onDestroyView()
    }
}