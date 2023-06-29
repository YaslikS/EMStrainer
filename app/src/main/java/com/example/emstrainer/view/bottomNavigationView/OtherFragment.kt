package com.example.emstrainer.view.bottomNavigationView

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.emstrainer.R
//import com.example.emstrainer.view.otherFragmentsAndActivity.DevicesActivity
import com.example.emstrainer.view.otherFragmentsAndActivity.SettingsFragment
import com.example.emstrainer.databinding.FragmentOtherBinding
import com.example.emstrainer.view.otherFragmentsAndActivity.DevicesActivity

class OtherFragment : Fragment() {
    private val settingsFragment = SettingsFragment()
    var fragmentOtherBinding: FragmentOtherBinding? = null
    var binding: FragmentOtherBinding? = null
    private val otherFragmentTAG = "OtherFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtherBinding.inflate(inflater, container, false)
        fragmentOtherBinding = binding

        return binding!!.root
    }

    override fun onStart() {
        super.onStart()

        binding!!.buttonSettings.setOnClickListener {
            openFragment(settingsFragment)
        }
        binding!!.buttonDevices.setOnClickListener {
            val devicesIntent = Intent(activity, DevicesActivity::class.java)
            startActivity(devicesIntent)
        }
    }

    private fun openFragment(twoFrag: Fragment) {
        val fragmentManager: FragmentManager? = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, twoFrag)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onDestroyView() {
        fragmentOtherBinding = null
        super.onDestroyView()
    }
}