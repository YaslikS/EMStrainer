package com.example.emstrainer.view.fitFragmentsAndActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.emstrainer.R
import com.example.emstrainer.databinding.ActivityFitProgrammsBinding
import com.example.emstrainer.presenter.MainPresenter
import com.example.emstrainer.presenter.SaveStates
import com.example.emstrainer.viewModels.ProfileViewModel

class FitProgrammsActivity : AppCompatActivity() {
    var binding: ActivityFitProgrammsBinding? = null
    private val FitProgrammsActivityTAG = "EMSTfitAct"
    private var saveStates = SaveStates
    private var presenter = MainPresenter
    private var profileViewModel: ProfileViewModel? = null
    private val chooseFitFragment = ChooseFitFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFitProgrammsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding!!.root)
        openFragment(chooseFitFragment)
    }

    private fun openFragment(frag: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.apply {
            replace(R.id.containerFitActivity, frag)
            commit()
        }
    }

    fun backTopButton(view: View) {
        onBackPressed()
    }
}