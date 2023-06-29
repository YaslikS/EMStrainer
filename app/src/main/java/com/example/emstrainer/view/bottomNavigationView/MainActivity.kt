package com.example.emstrainer.view.bottomNavigationView

import android.Manifest
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.emstrainer.R
import com.example.emstrainer.databinding.ActivityMainBinding
import com.example.emstrainer.presenter.MainPresenter
import com.example.emstrainer.presenter.SaveStates
import com.example.emstrainer.view.FullSuitPowerActivity
import com.example.emstrainer.view.fitFragmentsAndActivity.FitProgrammsActivity
import com.example.emstrainer.view.otherFragmentsAndActivity.DevicesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class MainActivity : FragmentActivity() {

    private var thirdByteCommand: Byte = 0
    private var radioIDFrag: Int = 0
    private var switchFmFrag: Boolean = false
    private var switchAmFrag: Boolean = false

    //private var profileViewModel: ProfileViewModel? = null
    private var saveStates = SaveStates
    private var presenter = MainPresenter
    private val mainActivityTAG = "EMSTmain"
    private val fullSuitprofileFragment = FullSuitProfileFragment()
    private val otherFragment = OtherFragment()
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null
    var binding: ActivityMainBinding? = null //private var window = this.getWindow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding!!.root)

        presenter.onBluetooth()
        binding?.batteryValue?.text = saveStates.batteryLevel.toString()
        connectionStatusObserver(true)
        checkBTPermissions()

        setBottomSheetBehavior()
        onCreateBottomSheet() //profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        openFragment(otherFragment, fullSuitprofileFragment)
        binding?.bottomNavigation?.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        ) //binding?.bottomNavigation?.menu?.findItem(R.id.ARBottom)?.isEnabled = false
    }

    private fun setBottomSheetBehavior() {
        val bottomSheetBehavior = binding?.bottomSheetFrameLayout
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetBehavior!!)
        binding?.bottomSheetFrameLayout?.layoutParams?.height = getWindowHeight() * 90 / 100

        (mBottomSheetBehavior as BottomSheetBehavior<*>).addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        mBottomSheetBehavior?.state =
                            BottomSheetBehavior.STATE_HIDDEN //supportActionBar?.show()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> { //supportActionBar?.hide()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        installValuePower() //supportActionBar?.hide()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> { //supportActionBar?.show()
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding?.layoutForDarkening?.alpha = (slideOffset * 0.8).toFloat()
            }
        })
    }

    private fun getWindowHeight(): Int {
        val outMetrics = DisplayMetrics()

        @Suppress("DEPRECATION") val display = this.windowManager.defaultDisplay
        @Suppress("DEPRECATION") display.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.profileBottom -> {
                    openFragment(otherFragment, fullSuitprofileFragment)
                    binding?.addProfileFABMain?.visibility = FloatingActionButton.VISIBLE
                    true
                }
                R.id.otherBottom -> {
                    binding?.addProfileFABMain?.visibility = FloatingActionButton.INVISIBLE
                    openFragment(fullSuitprofileFragment, otherFragment)
                    true
                }
                else -> false
            }
        }

    private fun connectionStatusObserver(state: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            while(state) {
                delay(1500)
                if(saveStates.connectStatus) launch(Dispatchers.Main) {
                    binding?.connectStatusCircleIV?.setImageResource(R.drawable.green_circle)
                    binding?.AllStopPowerFABMain?.visibility = FloatingActionButton.VISIBLE
                    binding?.batteryValue?.visibility = TextView.VISIBLE
                    binding?.imageViewBattery?.visibility = ImageView.VISIBLE
                    binding?.batteryValue?.text = saveStates.batteryLevel.toString()
                }
                else launch(Dispatchers.Main) {
                    binding?.connectStatusCircleIV?.setImageResource(R.drawable.red_circle)
                    binding?.batteryValue?.visibility = TextView.INVISIBLE
                    binding?.AllStopPowerFABMain?.visibility = FloatingActionButton.INVISIBLE
                    binding?.imageViewBattery?.visibility = ImageView.INVISIBLE
                    binding?.nameDeviceMainInMainActivity?.text = "Костюмы"
                    saveStates.nameSelectDevice = ""
                    saveStates.addressSelectDevice = ""
                }
            }
        }
    }

    fun allStopPowerMainActivityFullSuit(view: View) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                launch(Dispatchers.Main) {
                    binding?.connectStatusCircleIV?.visibility = ImageView.INVISIBLE
                }
                presenter.allStopPowerCommandPresenterFullSuit()
                presenter.startModulationAndFrequencyFullSuit(0) // по умолчанию
                launch(Dispatchers.Main) {
                    binding?.connectStatusCircleIV?.visibility = ImageView.VISIBLE
                }
            }
        } catch(e: Exception) {
            Toast.makeText(
                baseContext, "Ошибка при сбросе мощности: проверьте подключение!", Toast.LENGTH_LONG
            ).show()
        }
    }

    fun openPowerActivity(view: View) {
        val powerIntent = Intent(this, FullSuitPowerActivity::class.java)
        startActivity(powerIntent)
    }

    fun openDevicesActivity(view: View) {
        val deviceIntent = Intent(this, DevicesActivity::class.java)
        startActivity(deviceIntent)
    }

    fun openFitProgActivity(view: View) {
        if(!saveStates.connectStatus) Toast.makeText(this, "Подключите костюм!", Toast.LENGTH_SHORT)
            .show()
        val fitProgIntent = Intent(this, FitProgrammsActivity::class.java)
        startActivity(fitProgIntent)
    }

    private fun openFragment(oneFrag: Fragment, twoFrag: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.apply {
            replace(R.id.container, twoFrag)
            commit()
        }
    }

    fun openBottomSheet(view: View?) {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onBackPressed() {
        if(mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) mBottomSheetBehavior?.state =
            BottomSheetBehavior.STATE_HIDDEN
        else super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        connectionStatusObserver(true)
        if(saveStates.connectStatus) {
            binding?.connectStatusCircleIV?.setImageResource(R.drawable.green_circle)
            binding?.nameDeviceMainInMainActivity?.text = saveStates.nameSelectDevice
        } else {
            binding?.connectStatusCircleIV?.setImageResource(R.drawable.red_circle)
            binding?.nameDeviceMainInMainActivity?.text = "Костюмы"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(saveStates.connectStatus) presenter.apply {
            stopBroadcastCommandPresenterFullSuit()
            offConnect()
            saveStates.nameSelectDevice = ""
            saveStates.addressSelectDevice = ""
        }
    }

    private fun checkBTPermissions() {
        var permissionCheck = checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
        permissionCheck += checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")
        if(permissionCheck != 0) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1001
            ) //Any number
        }
    }

    //////// bottomSheet

    private fun onCreateBottomSheet() {
        plusButtonsListener()
        minusButtonsListener()
        selectAllListener()
        plusMinusAllListener()
        frequencyModulationListener()
        FABsListener()
        textChangedListeners()
    }

    private fun textChangedListeners() {
        binding?.layoutBottomSheet1?.editMuscule1Frag?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule1Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule1Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule1Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule1Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule2Frag?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule2Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule2Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule2Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule2Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule3Frag?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule3Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule3Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule3Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule3Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule4Frag?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule4Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule4Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule4Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule4Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule5Frag?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule5Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule5Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule5Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule5Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule6Frag?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule6Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule6Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule6Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule6Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule7Frag?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule7Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule7Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule7Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule7Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule8Frag?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule8Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule8Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule8Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule8Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule9Frag?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule9Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule9Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule9Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule9Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule10Frag?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule10Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule10Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule10Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule10Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule11Frag?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule11Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule11Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule11Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule11Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.layoutBottomSheet1?.editMuscule12Frag?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.layoutBottomSheet1?.editMuscule12Frag?.text.toString()
                            .toInt() < 1
                    ) binding?.layoutBottomSheet1?.editMuscule12Frag?.setText("1")
                    if(binding?.layoutBottomSheet1?.editMuscule12Frag?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.layoutBottomSheet1?.editMuscule12Frag?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })
    }

    private fun plusMinusAllListener() {
        binding?.layoutBottomSheet1?.plusAllFrag?.setOnClickListener(plusMinusAllOnClickListener)
        binding?.layoutBottomSheet1?.minusAllFrag?.setOnClickListener(plusMinusAllOnClickListener)
    }

    //All
    private var plusMinusAllOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.plusAllFrag -> {
                var checkState = false
                if(binding?.layoutBottomSheet1?.check1Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule1Frag)
                }
                if(binding?.layoutBottomSheet1?.check2Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule2Frag)
                }
                if(binding?.layoutBottomSheet1?.check3Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule3Frag)
                }
                if(binding?.layoutBottomSheet1?.check4Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule4Frag)
                }
                if(binding?.layoutBottomSheet1?.check5Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule5Frag)
                }
                if(binding?.layoutBottomSheet1?.check6Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule6Frag)
                }
                if(binding?.layoutBottomSheet1?.check7Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule7Frag)
                }
                if(binding?.layoutBottomSheet1?.check8Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule8Frag)
                }
                if(binding?.layoutBottomSheet1?.check9Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule9Frag)
                }
                if(binding?.layoutBottomSheet1?.check10Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule10Frag)
                }
                if(binding?.layoutBottomSheet1?.check11Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule11Frag)
                }
                if(binding?.layoutBottomSheet1?.check12Frag?.isChecked == true) {
                    checkState = true
                    plusFrag(binding?.layoutBottomSheet1?.editMuscule12Frag)
                }
                if(!checkState) Toast.makeText(
                    this, "Выберете каналы мощности!", Toast.LENGTH_LONG
                ).show()
            }
            R.id.minusAllFrag -> {
                var checkState = false
                if(binding?.layoutBottomSheet1?.check1Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule1Frag)
                }
                if(binding?.layoutBottomSheet1?.check2Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule2Frag)
                }
                if(binding?.layoutBottomSheet1?.check3Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule3Frag)
                }
                if(binding?.layoutBottomSheet1?.check4Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule4Frag)
                }
                if(binding?.layoutBottomSheet1?.check5Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule5Frag)
                }
                if(binding?.layoutBottomSheet1?.check6Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule6Frag)
                }
                if(binding?.layoutBottomSheet1?.check7Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule7Frag)
                }
                if(binding?.layoutBottomSheet1?.check8Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule8Frag)
                }
                if(binding?.layoutBottomSheet1?.check9Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule9Frag)
                }
                if(binding?.layoutBottomSheet1?.check10Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule10Frag)
                }
                if(binding?.layoutBottomSheet1?.check11Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule11Frag)
                }
                if(binding?.layoutBottomSheet1?.check12Frag?.isChecked == true) {
                    checkState = true
                    minusFrag(binding?.layoutBottomSheet1?.editMuscule12Frag)
                }
                if(!checkState) Toast.makeText(
                    this, "Выберете каналы мощности!", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun FABsListener() {
        binding?.layoutBottomSheet1?.AllStopPowerFABPowerFrag?.setOnClickListener(
            FABsOnClickListener
        )
        binding?.layoutBottomSheet1?.startPowerFABPowerFrag?.setOnClickListener(FABsOnClickListener)
        binding?.layoutBottomSheet1?.savePowerFABPowerFrag?.setOnClickListener(FABsOnClickListener)
    }

    private var FABsOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.AllStopPowerFABPowerFrag -> allStopPowerPowerFragmentFullSuit()
            R.id.startPowerFABPowerFrag -> normalPower() //R.id.savePowerFABPowerFrag -> goToNewProfile()
        }
    }

    /*
    private fun goToNewProfile() {
        Toast.makeText(
            this,
            "private fun goToNewProfile() \"!\"!",
            Toast.LENGTH_SHORT
        ).show()
        val view = layoutInflater.inflate(R.layout.fragment_new_profile, null)
        dialog?.apply {
            setContentView(view)
            show()
        }
        view!!.saveProfileButton.setOnClickListener(newProfileListener)
    }
    */

    private var newProfileListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) { //R.id.saveProfileButton -> saveNewProfile()
        }
    }

    /*
    private fun saveNewProfile(view: View? = null) {
        if(dialog!!.saveProfileET.text!!.isEmpty() || checkExclamationMark()) {
            Toast.makeText(
                this,
                "Название профиля не должно быть пустым и не содержать символа \"!\"!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            dialog!!.dismiss()
            val newProfile = ProfileFullSuit(
                dialog!!.saveProfileET.text!!.trim().toString(),
                binding?.layoutBottomSheet1?.radioGroupFreqFrag?.checkedRadioButtonId,
                switchFmModulFrag.isChecked,
                switchAmModulFrag.isChecked,
                saveStates.changePower,
                thirdByteCommand,
                saveStates.maxPower,
                editMuscule1Frag.text.toString().toInt(),
                editMuscule2Frag.text.toString().toInt(),
                editMuscule3Frag.text.toString().toInt(),
                editMuscule4Frag.text.toString().toInt(),
                editMuscule5Frag.text.toString().toInt(),
                editMuscule6Frag.text.toString().toInt(),
                editMuscule7Frag.text.toString().toInt(),
                editMuscule8Frag.text.toString().toInt(),
                editMuscule9Frag.text.toString().toInt(),
                editMuscule10Frag.text.toString().toInt(),
                editMuscule11Frag.text.toString().toInt(),
                editMuscule12Frag.text.toString().toInt(),
            )
            profileViewModel!!.insert(newProfile)
            //activity?.updateRecyclerInProfileFragment
            dialog!!.dismiss()
        }
    }
    */

    //private fun checkExclamationMark(): Boolean = dialog!!.saveProfileET.text.toString().contains("!")

    private fun allStopPowerPowerFragmentFullSuit() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                presenter.allStopPowerCommandPresenterFullSuit()
                presenter.startModulationAndFrequencyFullSuit(0)
                launch(Dispatchers.Main) {
                    apply {
                        binding?.layoutBottomSheet1?.editMuscule1Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule2Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule3Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule4Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule5Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule6Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule7Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule8Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule9Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule10Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule11Frag?.setText("1")
                        binding?.layoutBottomSheet1?.editMuscule12Frag?.setText("1")
                    }
                }
            }
        } catch(e: Exception) {
            Toast.makeText(
                this, "Ошибка при сбросе мощности: проверьте подключение!", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun normalPower() {
        try {
            if(saveStates.connectStatus) {
                var checkPowerValue = false
                CoroutineScope(Dispatchers.IO).launch {
                    launch(Dispatchers.Main) {
                        startPowerFABPowerFragTimer()
                    }
                    checkPowerValue = presenter.startPowerFullSuit(
                        binding?.layoutBottomSheet1?.editMuscule1Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule2Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule3Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule4Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule5Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule6Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule7Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule8Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule9Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule10Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule11Frag?.text.toString().toInt(),
                        binding?.layoutBottomSheet1?.editMuscule12Frag?.text.toString().toInt()
                    )
                    if(checkPowerValue) {
                        presenter.startModulationAndFrequencyFullSuit(thirdByteCommand) //saveStates.thirdByteCommand = thirdByteCommand
                    }
                    launch(Dispatchers.Main) {
                        if(!checkPowerValue) Toast.makeText(
                            this@MainActivity,
                            "Введенная мощность выше заданного значения!",
                            Toast.LENGTH_SHORT
                        ).show() //progressBarPower.visibility = ProgressBar.INVISIBLE
                    }
                }
            } else {
                val a1 = 1 / 0
            }
        } catch(e: Exception) {
            Toast.makeText(
                this, "Ошибка при отправке мощности: проверьте подключение!", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startPowerFABPowerFragTimer() {
        CoroutineScope(Dispatchers.IO).launch {
            launch(Dispatchers.Main) {
                binding?.layoutBottomSheet1?.startPowerFABPowerFrag?.isClickable = false
                binding?.layoutBottomSheet1?.startPowerFABPowerFrag?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.colorGrey))
                binding?.layoutBottomSheet1?.startPowerFABPowerFrag?.visibility =
                    FloatingActionButton.INVISIBLE
            }
            delay(3000)
            launch(Dispatchers.Main) {
                binding?.layoutBottomSheet1?.startPowerFABPowerFrag?.isClickable = true
                binding?.layoutBottomSheet1?.startPowerFABPowerFrag?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.colorSecondary))
                binding?.layoutBottomSheet1?.startPowerFABPowerFrag?.visibility =
                    FloatingActionButton.VISIBLE
            }
        }
    }

    private fun frequencyModulationListener() {
        binding?.layoutBottomSheet1?.radioGroupFreqFrag?.setOnCheckedChangeListener(
            mOnCheckedChangeListenerFrag
        )
        binding?.layoutBottomSheet1?.switchFmModulFrag?.setOnClickListener(
            frequencyModulationOnClickListener
        )
        binding?.layoutBottomSheet1?.switchAmModulFrag?.setOnClickListener(
            frequencyModulationOnClickListener
        )
    }

    private var frequencyModulationOnClickListener: View.OnClickListener =
        View.OnClickListener { v ->
            when(v.id) {
                R.id.switchAmModulFrag -> {
                    if(binding?.layoutBottomSheet1?.switchAmModulFrag?.isChecked == true) {
                        thirdByteCommand = (thirdByteCommand + 0b1).toByte()
                        switchAmFrag = true
                    } else {
                        thirdByteCommand = (thirdByteCommand - 0b1).toByte()
                        switchAmFrag = false
                    }
                }
                R.id.switchFmModulFrag -> {
                    if(binding?.layoutBottomSheet1?.switchFmModulFrag?.isChecked == true) {
                        thirdByteCommand = (thirdByteCommand + 0b10).toByte()
                        switchFmFrag = true
                    } else {
                        thirdByteCommand = (thirdByteCommand - 0b10).toByte()
                        switchFmFrag = false
                    }
                }
            }
        }

    //frequency, modulation
    private val mOnCheckedChangeListenerFrag = RadioGroup.OnCheckedChangeListener { _, checkedId ->
        radioControlFrag()
        when(checkedId) {
            R.id.radioFreq16Frag -> thirdByteCommand = (thirdByteCommand + 32).toByte()
            R.id.radioFreq32Frag -> thirdByteCommand = (thirdByteCommand + 16).toByte()
            R.id.radioFreq64Frag -> thirdByteCommand = (thirdByteCommand + 8).toByte()
            R.id.radioFreq128Frag -> thirdByteCommand = (thirdByteCommand + 4).toByte()
        }
        radioIDFrag = checkedId
    }

    private fun radioControlFrag() {
        when(radioIDFrag) {
            R.id.radioFreq16Frag -> thirdByteCommand = (thirdByteCommand - 32).toByte()
            R.id.radioFreq32Frag -> thirdByteCommand = (thirdByteCommand - 16).toByte()
            R.id.radioFreq64Frag -> thirdByteCommand = (thirdByteCommand - 8).toByte()
            R.id.radioFreq128Frag -> thirdByteCommand = (thirdByteCommand - 4).toByte()
        }
    }

    private var plusOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.plus1Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule1Frag)
            R.id.plus2Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule2Frag)
            R.id.plus3Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule3Frag)
            R.id.plus4Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule4Frag)
            R.id.plus5Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule5Frag)
            R.id.plus6Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule6Frag)
            R.id.plus7Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule7Frag)
            R.id.plus8Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule8Frag)
            R.id.plus9Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule9Frag)
            R.id.plus10Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule10Frag)
            R.id.plus11Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule11Frag)
            R.id.plus12Frag -> plusFrag(binding?.layoutBottomSheet1?.editMuscule12Frag)
        }
    }

    private fun plusFrag(editText: EditText? = null) {
        if(editText?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower
        ) editText?.setText((editText?.text.toString().toInt() + saveStates.changePower).toString())
    }

    private fun selectAllListener() {
        binding?.layoutBottomSheet1?.chooseAllButtonFrag?.setOnClickListener(
            selectAllOnClickListener
        )
        binding?.layoutBottomSheet1?.deleteAllButtonFrag?.setOnClickListener(
            selectAllOnClickListener
        )
    }

    private var selectAllOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.chooseAllButtonFrag -> {
                binding?.layoutBottomSheet1?.check1Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check2Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check3Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check4Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check5Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check6Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check7Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check8Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check9Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check10Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check11Frag?.isChecked = true
                binding?.layoutBottomSheet1?.check12Frag?.isChecked = true
            }
            R.id.deleteAllButtonFrag -> {
                binding?.layoutBottomSheet1?.check1Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check2Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check3Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check4Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check5Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check6Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check7Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check8Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check9Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check10Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check11Frag?.isChecked = false
                binding?.layoutBottomSheet1?.check12Frag?.isChecked = false
            }
        }
    }

    private fun minusButtonsListener() {
        binding?.layoutBottomSheet1?.minus1Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus2Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus3Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus4Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus5Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus6Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus7Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus8Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus9Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus10Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus11Frag?.setOnClickListener(minusOnClickListener)
        binding?.layoutBottomSheet1?.minus12Frag?.setOnClickListener(minusOnClickListener)
    }

    //minus
    private var minusOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.minus1Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule1Frag)
            R.id.minus2Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule2Frag)
            R.id.minus3Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule3Frag)
            R.id.minus4Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule4Frag)
            R.id.minus5Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule5Frag)
            R.id.minus6Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule6Frag)
            R.id.minus7Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule7Frag)
            R.id.minus8Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule8Frag)
            R.id.minus9Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule9Frag)
            R.id.minus10Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule10Frag)
            R.id.minus11Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule11Frag)
            R.id.minus12Frag -> minusFrag(binding?.layoutBottomSheet1?.editMuscule12Frag)
        }
    }

    private fun minusFrag(editText: EditText? = null) {
        if(editText?.text.toString()
                .toInt() - saveStates.changePower > 0
        ) editText?.setText((editText?.text.toString().toInt() - saveStates.changePower).toString())
    }

    private fun plusButtonsListener() {
        binding?.layoutBottomSheet1?.plus1Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus2Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus3Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus4Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus5Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus6Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus7Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus8Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus9Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus10Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus11Frag?.setOnClickListener(plusOnClickListener)
        binding?.layoutBottomSheet1?.plus12Frag?.setOnClickListener(plusOnClickListener)
    }

    private fun installValuePower() {
        binding?.layoutBottomSheet1?.maxPowerTVfragPower?.text =
            "  Макс-ая мощность " + saveStates.maxPower
        binding?.layoutBottomSheet1?.editMuscule1Frag?.setText(saveStates.power1.toString())
        binding?.layoutBottomSheet1?.editMuscule2Frag?.setText(saveStates.power2.toString())
        binding?.layoutBottomSheet1?.editMuscule3Frag?.setText(saveStates.power3.toString())
        binding?.layoutBottomSheet1?.editMuscule4Frag?.setText(saveStates.power4.toString())
        binding?.layoutBottomSheet1?.editMuscule5Frag?.setText(saveStates.power5.toString())
        binding?.layoutBottomSheet1?.editMuscule6Frag?.setText(saveStates.power6.toString())
        binding?.layoutBottomSheet1?.editMuscule7Frag?.setText(saveStates.power7.toString())
        binding?.layoutBottomSheet1?.editMuscule8Frag?.setText(saveStates.power8.toString())
        binding?.layoutBottomSheet1?.editMuscule9Frag?.setText(saveStates.power9.toString())
        binding?.layoutBottomSheet1?.editMuscule10Frag?.setText(saveStates.power10.toString())
        binding?.layoutBottomSheet1?.editMuscule11Frag?.setText(saveStates.power11.toString())
        binding?.layoutBottomSheet1?.editMuscule12Frag?.setText(saveStates.power12.toString())

        binding?.layoutBottomSheet1?.switchAmModulFrag?.isChecked = saveStates.switchAm
        binding?.layoutBottomSheet1?.switchFmModulFrag?.isChecked = saveStates.switchFm
        binding?.layoutBottomSheet1?.radioGroupFreqFrag?.check(saveStates.radioId)
    }

}