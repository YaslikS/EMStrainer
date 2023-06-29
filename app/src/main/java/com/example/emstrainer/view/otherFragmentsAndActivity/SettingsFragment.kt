package com.example.emstrainer.view.otherFragmentsAndActivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.emstrainer.R
import com.example.emstrainer.databinding.FragmentSettingsBinding
import com.example.emstrainer.presenter.SaveStates
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    private var saveStates = SaveStates
    var fragmentSettingsBinding: FragmentSettingsBinding? = null
    var binding: FragmentSettingsBinding? = null
    private val settingsFragmentTAG = "SettingsFragment"
    private val logFragment = LogFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        fragmentSettingsBinding = binding

        binding!!.settingsHeader.setOnClickListener { activity?.onBackPressed() }
        binding!!.TVlog.setOnClickListener { openFragment(logFragment) }
        binding!!.LLlog.setOnClickListener { openFragment(logFragment) }
        binding!!.minusChangeSettings.setOnClickListener {
            if((binding!!.editMusculeAll.text.toString().toInt() - 1) > 0) {
                saveStates.changePower = binding!!.editMusculeAll.text.toString().toInt() - 1
                installEditMusculeAll()
            }
        }
        binding!!.plusChangeSettings.setOnClickListener {
            if((binding!!.editMusculeAll.text.toString().toInt() + 1) <= saveStates.maxPower) {
                saveStates.changePower = binding!!.editMusculeAll.text.toString().toInt() + 1
                installEditMusculeAll()
            }
        }
        binding!!.SCArMode.isChecked = saveStates.arMode
        binding!!.SCArMode.setOnCheckedChangeListener { buttonView, isChecked ->
            saveStates.arMode = SCArMode.isChecked
        }

        binding?.editMusculeAll?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.editMusculeAll?.text.toString()
                            .toInt() < 1
                    ) binding?.editMusculeAll?.setText("1")
                    if(binding?.editMusculeAll?.text.toString()
                            .toInt() > 20
                    ) binding?.editMusculeAll?.setText("20")
                } catch(e: Exception) {
                }
            }
        })

        binding?.limitPowerET?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.limitPowerET?.text.toString().toInt() < 5
                    ) binding?.limitPowerET?.setText("5")
                    if(binding?.limitPowerET?.text.toString().toInt() > 125
                    ) binding?.limitPowerET?.setText("125")
                } catch(e: Exception) {
                }
            }
        })

        return binding!!.root
    }

    fun plusChange(view: View) {
        saveStates.changePower = binding!!.editMusculeAll.text.toString().toInt() + 1
        installEditMusculeAll()
    }

    fun minusChange(view: View) {
        if((binding!!.editMusculeAll.text.toString().toInt() - 1) > 0) {
            saveStates.changePower = binding!!.editMusculeAll.text.toString().toInt() - 1
            installEditMusculeAll()
        }
    }

    override fun onPause() {
        saveStates.maxPower = binding!!.limitPowerET.text.toString().toInt()
        saveStates.changePower = binding!!.editMusculeAll.text.toString().toInt()
        activity?.topShadowMain?.visibility = View.VISIBLE
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding!!.limitPowerET.setText(saveStates.maxPower.toString())
        installEditMusculeAll()
        activity?.topShadowMain?.visibility = View.INVISIBLE
    }

    private fun installEditMusculeAll() {
        binding!!.editMusculeAll.setText(saveStates.changePower.toString())
    }

    private fun openFragment(twoFrag: Fragment) {
        val fragmentManager: FragmentManager? = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, twoFrag)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onDestroyView() {
        fragmentSettingsBinding = null
        super.onDestroyView()
    }
}