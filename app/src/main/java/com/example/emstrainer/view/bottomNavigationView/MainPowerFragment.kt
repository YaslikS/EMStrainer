package com.example.emstrainer.view.bottomNavigationView
/*
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.emstrainer.R
import com.example.emstrainer.mainPresenter.MainPresenter
import com.example.emstrainer.mainPresenter.SaveStates
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuit
import com.example.emstrainer.viewModels.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_main_power.*
import kotlinx.android.synthetic.main.fragment_main_power.view.*
import kotlinx.android.synthetic.main.fragment_new_profile.*
import kotlinx.android.synthetic.main.fragment_new_profile.view.*
import kotlinx.coroutines.*

class MainPowerFragment : BottomSheetDialogFragment() {
    private var saveStates = SaveStates
    private var presenter = MainPresenter
    private var dialog: BottomSheetDialog? = null
    private var viewFragPower: View? = null
    private var thirdByteCommand: Byte = 0
    private var radioIDFrag: Int = 0
    private val mainPowerFragmentTAG = "EMST_Main_Power_Fragment"
    private var switchFmFrag: Boolean = false
    private var switchAmFrag: Boolean = false
    private var profileViewModel: ProfileViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewFragPower = inflater.inflate(R.layout.fragment_main_power, container, false)
        viewFragPower!!.dismissBottomSheetPower.setOnClickListener { dismiss() }
        dialog = context?.let { BottomSheetDialog(it) }!!
        plusButtonsListener()
        minusButtonsListener()
        selectAllListener()
        plusMinusAllListener()
        frequencyModulationListener()
        FABsListener()
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewFragPower!!.editMuscule1Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule1Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule1Frag.setText("1")
                    if(viewFragPower!!.editMuscule1Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule1Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule2Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule2Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule2Frag.setText("1")
                    if(viewFragPower!!.editMuscule2Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule2Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule3Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule3Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule3Frag.setText("1")
                    if(viewFragPower!!.editMuscule3Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule3Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule4Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule4Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule4Frag.setText("1")
                    if(viewFragPower!!.editMuscule4Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule4Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule5Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule5Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule5Frag.setText("1")
                    if(viewFragPower!!.editMuscule5Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule5Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule6Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule6Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule6Frag.setText("1")
                    if(viewFragPower!!.editMuscule6Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule6Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule7Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule7Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule7Frag.setText("1")
                    if(viewFragPower!!.editMuscule7Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule7Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule8Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule8Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule8Frag.setText("1")
                    if(viewFragPower!!.editMuscule8Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule8Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule9Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule9Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule9Frag.setText("1")
                    if(viewFragPower!!.editMuscule9Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule9Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule10Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule10Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule10Frag.setText("1")
                    if(viewFragPower!!.editMuscule10Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule10Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule11Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule11Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule11Frag.setText("1")
                    if(viewFragPower!!.editMuscule11Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule11Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        viewFragPower!!.editMuscule12Frag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(viewFragPower!!.editMuscule12Frag.text.toString()
                            .toInt() < 1
                    ) viewFragPower!!.editMuscule12Frag.setText("1")
                    if(viewFragPower!!.editMuscule12Frag.text.toString()
                            .toInt() > saveStates.maxPower
                    ) viewFragPower!!.editMuscule12Frag.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        return viewFragPower
    }

    private fun saveNewProfile(view: View? = null) {
        if(dialog!!.saveProfileET.text!!.isEmpty() || checkExclamationMark()) {
            Toast.makeText(
                context,
                "Название профиля не должно быть пустым и не содержать символа \"!\"!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            dialog!!.dismiss()
            val newProfile = ProfileFullSuit(
                dialog!!.saveProfileET.text!!.trim().toString(),
                radioGroupFreqFrag.checkedRadioButtonId,
                viewFragPower!!.switchFmModulFrag.isChecked,
                viewFragPower!!.switchAmModulFrag.isChecked,
                saveStates.changePower,
                thirdByteCommand,
                saveStates.maxPower,
                viewFragPower!!.editMuscule1Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule2Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule3Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule4Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule5Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule6Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule7Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule8Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule9Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule10Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule11Frag.text.toString().toInt(),
                viewFragPower!!.editMuscule12Frag.text.toString().toInt(),
            )
            profileViewModel!!.insert(newProfile)
            //activity?.updateRecyclerInProfileFragment
            dialog!!.dismiss()
        }
    }

    //selectAll
    private var selectAllOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.chooseAllButtonFrag -> {
                check1Frag.isChecked = true
                check2Frag.isChecked = true
                check3Frag.isChecked = true
                check4Frag.isChecked = true
                check5Frag.isChecked = true
                check6Frag.isChecked = true
                check7Frag.isChecked = true
                check8Frag.isChecked = true
                check9Frag.isChecked = true
                check10Frag.isChecked = true
                check11Frag.isChecked = true
                check12Frag.isChecked = true
            }
            R.id.deleteAllButtonFrag -> {
                check1Frag.isChecked = false
                check2Frag.isChecked = false
                check3Frag.isChecked = false
                check4Frag.isChecked = false
                check5Frag.isChecked = false
                check6Frag.isChecked = false
                check7Frag.isChecked = false
                check8Frag.isChecked = false
                check9Frag.isChecked = false
                check10Frag.isChecked = false
                check11Frag.isChecked = false
                check12Frag.isChecked = false
            }
        }
    }

    private fun selectAllListener() {
        viewFragPower!!.chooseAllButtonFrag.setOnClickListener(selectAllOnClickListener)
        viewFragPower!!.deleteAllButtonFrag.setOnClickListener(selectAllOnClickListener)
    }

    //All
    private var plusMinusAllOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.plusAllFrag -> {
                var checkState = false
                if(check1Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule1Frag)
                }
                if(check2Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule2Frag)
                }
                if(check3Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule3Frag)
                }
                if(check4Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule4Frag)
                }
                if(check5Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule5Frag)
                }
                if(check6Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule6Frag)
                }
                if(check7Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule7Frag)
                }
                if(check8Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule8Frag)
                }
                if(check9Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule9Frag)
                }
                if(check10Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule10Frag)
                }
                if(check11Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule11Frag)
                }
                if(check12Frag.isChecked) {
                    checkState = true
                    plusFrag(editMuscule12Frag)
                }
                if(!checkState) Toast.makeText(
                    context, "Выберете каналы мощности!", Toast.LENGTH_LONG
                ).show()
            }
            R.id.minusAllFrag -> {
                var checkState = false
                if(check1Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule1Frag)
                }
                if(check2Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule2Frag)
                }
                if(check3Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule3Frag)
                }
                if(check4Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule4Frag)
                }
                if(check5Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule5Frag)
                }
                if(check6Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule6Frag)
                }
                if(check7Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule7Frag)
                }
                if(check8Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule8Frag)
                }
                if(check9Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule9Frag)
                }
                if(check10Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule10Frag)
                }
                if(check11Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule11Frag)
                }
                if(check12Frag.isChecked) {
                    checkState = true
                    minusFrag(editMuscule12Frag)
                }
                if(!checkState) Toast.makeText(
                    context, "Выберете каналы мощности!", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun plusMinusAllListener() {
        viewFragPower!!.plusAllFrag.setOnClickListener(plusMinusAllOnClickListener)
        viewFragPower!!.minusAllFrag.setOnClickListener(plusMinusAllOnClickListener)
    }

    //plus
    private var plusOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.plus1Frag -> plusFrag(editMuscule1Frag)
            R.id.plus2Frag -> plusFrag(editMuscule2Frag)
            R.id.plus3Frag -> plusFrag(editMuscule3Frag)
            R.id.plus4Frag -> plusFrag(editMuscule4Frag)
            R.id.plus5Frag -> plusFrag(editMuscule5Frag)
            R.id.plus6Frag -> plusFrag(editMuscule6Frag)
            R.id.plus7Frag -> plusFrag(editMuscule7Frag)
            R.id.plus8Frag -> plusFrag(editMuscule8Frag)
            R.id.plus9Frag -> plusFrag(editMuscule9Frag)
            R.id.plus10Frag -> plusFrag(editMuscule10Frag)
            R.id.plus11Frag -> plusFrag(editMuscule11Frag)
            R.id.plus12Frag -> plusFrag(editMuscule12Frag)
        }
    }

    private fun plusButtonsListener() {
        viewFragPower!!.plus1Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus2Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus3Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus4Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus5Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus6Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus7Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus8Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus9Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus10Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus11Frag.setOnClickListener(plusOnClickListener)
        viewFragPower!!.plus12Frag.setOnClickListener(plusOnClickListener)
    }

    private fun plusFrag(editText: EditText? = null) {
        if(editText?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower
        ) editText?.setText((editText?.text.toString().toInt() + saveStates.changePower).toString())
    }

    //minus
    private var minusOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.minus1Frag -> minusFrag(editMuscule1Frag)
            R.id.minus2Frag -> minusFrag(editMuscule2Frag)
            R.id.minus3Frag -> minusFrag(editMuscule3Frag)
            R.id.minus4Frag -> minusFrag(editMuscule4Frag)
            R.id.minus5Frag -> minusFrag(editMuscule5Frag)
            R.id.minus6Frag -> minusFrag(editMuscule6Frag)
            R.id.minus7Frag -> minusFrag(editMuscule7Frag)
            R.id.minus8Frag -> minusFrag(editMuscule8Frag)
            R.id.minus9Frag -> minusFrag(editMuscule9Frag)
            R.id.minus10Frag -> minusFrag(editMuscule10Frag)
            R.id.minus11Frag -> minusFrag(editMuscule11Frag)
            R.id.minus12Frag -> minusFrag(editMuscule12Frag)
        }
    }

    private fun minusButtonsListener() {
        viewFragPower!!.minus1Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus2Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus3Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus4Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus5Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus6Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus7Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus8Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus9Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus10Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus11Frag.setOnClickListener(minusOnClickListener)
        viewFragPower!!.minus12Frag.setOnClickListener(minusOnClickListener)
    }

    private fun minusFrag(editText: EditText? = null) {
        if(editText?.text.toString()
                .toInt() - saveStates.changePower > 0
        ) editText?.setText((editText?.text.toString().toInt() - saveStates.changePower).toString())
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
        Log.e(
            mainPowerFragmentTAG,
            "$thirdByteCommand - end - OnCheckedChangeListener - MainActivity - 3"
        )
    }

    private fun radioControlFrag() {
        Log.e(mainPowerFragmentTAG, "$thirdByteCommand - start - radioControl - MainActivity - 1")
        when(radioIDFrag) {
            R.id.radioFreq16Frag -> thirdByteCommand = (thirdByteCommand - 32).toByte()
            R.id.radioFreq32Frag -> thirdByteCommand = (thirdByteCommand - 16).toByte()
            R.id.radioFreq64Frag -> thirdByteCommand = (thirdByteCommand - 8).toByte()
            R.id.radioFreq128Frag -> thirdByteCommand = (thirdByteCommand - 4).toByte()
        }
        Log.e(mainPowerFragmentTAG, "$thirdByteCommand - end - radioControl - MainActivity - 2")
    }

    private var frequencyModulationOnClickListener: View.OnClickListener =
        View.OnClickListener { v ->
            when(v.id) {
                R.id.switchAmModulFrag -> {
                    if(switchAmModulFrag.isChecked) {
                        thirdByteCommand = (thirdByteCommand + 0b1).toByte()
                        switchAmFrag = true
                    } else {
                        thirdByteCommand = (thirdByteCommand - 0b1).toByte()
                        switchAmFrag = false
                    }
                }
                R.id.switchFmModulFrag -> {
                    if(switchFmModulFrag.isChecked) {
                        thirdByteCommand = (thirdByteCommand + 0b10).toByte()
                        switchFmFrag = true
                    } else {
                        thirdByteCommand = (thirdByteCommand - 0b10).toByte()
                        switchFmFrag = false
                    }
                }
            }
        }

    private fun frequencyModulationListener() {
        viewFragPower!!.radioGroupFreqFrag.setOnCheckedChangeListener(mOnCheckedChangeListenerFrag)
        viewFragPower!!.switchFmModulFrag.setOnClickListener(frequencyModulationOnClickListener)
        viewFragPower!!.switchAmModulFrag.setOnClickListener(frequencyModulationOnClickListener)
    }

    //FABs
    private var FABsOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.AllStopPowerFABPowerFrag -> allStopPowerPowerFragmentFullSuit()
            R.id.startPowerFABPowerFrag -> normalPower()
            R.id.savePowerFABPowerFrag -> goToNewProfile()
        }
    }

    private fun allStopPowerPowerFragmentFullSuit() {
        try {
            GlobalScope.launch {
                GlobalScope.async {
                    presenter.allStopPowerCommandPresenterFullSuit()
                    presenter.startModulationAndFrequencyFullSuit(0)
                    launch (Dispatchers.Main){
                        viewFragPower!!.apply {
                            editMuscule1Frag.setText("1")
                            editMuscule2Frag.setText("1")
                            editMuscule3Frag.setText("1")
                            editMuscule4Frag.setText("1")
                            editMuscule5Frag.setText("1")
                            editMuscule6Frag.setText("1")
                            editMuscule7Frag.setText("1")
                            editMuscule8Frag.setText("1")
                            editMuscule9Frag.setText("1")
                            editMuscule10Frag.setText("1")
                            editMuscule11Frag.setText("1")
                            editMuscule12Frag.setText("1")
                        }
                    }
                }
            }
        } catch(e: Exception) {
            Toast.makeText(
                context, "Ошибка при сбросе мощности: проверьте подключение!", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun FABsListener() {
        viewFragPower!!.AllStopPowerFABPowerFrag.setOnClickListener(FABsOnClickListener)
        viewFragPower!!.startPowerFABPowerFrag.setOnClickListener(FABsOnClickListener)
        viewFragPower!!.savePowerFABPowerFrag.setOnClickListener(FABsOnClickListener)
    }

    private fun normalPower() {
        try {
            if(saveStates.connectStatus) {
                var checkPowerValue = false
                GlobalScope.launch {
                    GlobalScope.async {
                        launch(Dispatchers.Main){
                            startPowerFABPowerFragTimer()
                        }
                        checkPowerValue = presenter.startPowerFullSuit(
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
                            editMuscule12Frag.text.toString().toInt()
                        )
                        if(checkPowerValue) {
                            presenter.startModulationAndFrequencyFullSuit(thirdByteCommand)
                            //saveStates.thirdByteCommand = thirdByteCommand
                        }
                        launch(Dispatchers.Main) {
                            if(!checkPowerValue) Toast.makeText(
                                context,
                                "Введенная мощность выше заданного значения!",
                                Toast.LENGTH_SHORT
                            ).show()
                            //progressBarPower.visibility = ProgressBar.INVISIBLE
                        }
                    }
                }
            } else {
                val a1 = 1 / 0
            }
        } catch(e: Exception) {
            Toast.makeText(
                context, "Ошибка при отправке мощности: проверьте подключение!", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startPowerFABPowerFragTimer(){
        GlobalScope.launch {
            GlobalScope.async {
                launch(Dispatchers.Main){
                    viewFragPower!!.startPowerFABPowerFrag.isClickable = false
                    viewFragPower!!.startPowerFABPowerFrag.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorGrey))
                    viewFragPower!!.startPowerFABPowerFrag.visibility = FloatingActionButton.INVISIBLE
                }
                delay(3000)
                launch(Dispatchers.Main){
                    viewFragPower!!.startPowerFABPowerFrag.isClickable = true
                    viewFragPower!!.startPowerFABPowerFrag.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorSecondary))
                    viewFragPower!!.startPowerFABPowerFrag.visibility = FloatingActionButton.VISIBLE
                }
            }
        }
    }

    //fragment
    override fun onResume() {
        super.onResume()
        installValuePower()
    }

    private fun installValuePower(){
        viewFragPower!!.maxPowerTVfragPower.text = "  Макс-ая мощность " + saveStates.maxPower
        viewFragPower!!.editMuscule1Frag.setText(saveStates.power1.toString())
        viewFragPower!!.editMuscule2Frag.setText(saveStates.power2.toString())
        viewFragPower!!.editMuscule3Frag.setText(saveStates.power3.toString())
        viewFragPower!!.editMuscule4Frag.setText(saveStates.power4.toString())
        viewFragPower!!.editMuscule5Frag.setText(saveStates.power5.toString())
        viewFragPower!!.editMuscule6Frag.setText(saveStates.power6.toString())
        viewFragPower!!.editMuscule7Frag.setText(saveStates.power7.toString())
        viewFragPower!!.editMuscule8Frag.setText(saveStates.power8.toString())
        viewFragPower!!.editMuscule9Frag.setText(saveStates.power9.toString())
        viewFragPower!!.editMuscule10Frag.setText(saveStates.power10.toString())
        viewFragPower!!.editMuscule11Frag.setText(saveStates.power11.toString())
        viewFragPower!!.editMuscule12Frag.setText(saveStates.power12.toString())

        viewFragPower!!.switchAmModulFrag.isChecked = saveStates.switchAm
        viewFragPower!!.switchFmModulFrag.isChecked = saveStates.switchFm
        viewFragPower!!.radioGroupFreqFrag.check(saveStates.radioId)
    }

    private var newProfileListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.id) {
            R.id.saveProfileButton -> saveNewProfile()
        }
    }

    private fun goToNewProfile() {
        val view = layoutInflater.inflate(R.layout.fragment_new_profile, null)
        dialog!!.apply {
            setContentView(view)
            show()
        }
        view!!.saveProfileButton.setOnClickListener(newProfileListener)
    }

    private fun checkExclamationMark(): Boolean =
        dialog!!.saveProfileET.text.toString().contains("!")

    interface BottomSheetListener {
        fun onOptionClick(text: String)
    }
}
*/