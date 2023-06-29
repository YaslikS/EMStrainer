package com.example.emstrainer.view

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.emstrainer.R
import com.example.emstrainer.adapters.ProfileAdapter
import com.example.emstrainer.databinding.ActivityFullSuitPowerBinding
import com.example.emstrainer.presenter.MainPresenter
import com.example.emstrainer.presenter.SaveStates
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuit
import com.example.emstrainer.viewModels.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_new_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FullSuitPowerActivity : AppCompatActivity() {
    private var binding: ActivityFullSuitPowerBinding? = null
    private var presenter = MainPresenter
    private var saveStates = SaveStates
    private var profileAdapter: ProfileAdapter? = null
    private var profileViewModel: ProfileViewModel? = null
    private var dialog: BottomSheetDialog? = null
    private var thirdByteCommand: Byte = 0
    private var data: LiveData<List<ProfileFullSuit>>? = null
    private val powerActivityTAG = "EMST_power_activity"
    private var saveProfileFlag = false
    private var radioID: Int = 0
    private var switchFm: Boolean = false
    private var switchAm: Boolean = false
    private var onePercentAlpha = 0.0f

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullSuitPowerBinding.inflate(LayoutInflater.from(this))
        val view = binding!!.root
        setContentView(binding!!.root)

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        profileAdapter = ProfileAdapter()
        data = profileViewModel!!.getAllProfiles()
        data!!.observe(this, Observer {
            profileAdapter!!.setContentList(it)
            profileAdapter!!.notifyDataSetChanged()
        })

        dialog = BottomSheetDialog(this)
        binding?.radioGroupFreq?.setOnCheckedChangeListener(mOnCheckedChangeListener)
        checkRedObserver()
        textChangedListeners()

    }

    private fun textChangedListeners() {
        binding?.power1ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power1ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power1ValueActPow?.setText("1")
                    if(binding?.power1ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power1ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power2ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power2ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power2ValueActPow?.setText("1")
                    if(binding?.power2ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power2ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power3ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power3ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power3ValueActPow?.setText("1")
                    if(binding?.power3ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power3ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power4ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power4ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power4ValueActPow?.setText("1")
                    if(binding?.power4ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power4ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power5ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power5ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power5ValueActPow?.setText("1")
                    if(binding?.power5ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power5ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power6ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power6ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power6ValueActPow?.setText("1")
                    if(binding?.power6ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power6ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power7ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power7ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power7ValueActPow?.setText("1")
                    if(binding?.power7ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power7ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power8ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power8ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power8ValueActPow?.setText("1")
                    if(binding?.power8ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power8ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power9ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power9ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power9ValueActPow?.setText("1")
                    if(binding?.power9ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power9ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power10ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power10ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power10ValueActPow?.setText("1")
                    if(binding?.power10ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power10ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power11ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power11ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power11ValueActPow?.setText("1")
                    if(binding?.power11ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power11ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })

        binding?.power12ValueActPow?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if(binding?.power12ValueActPow?.text.toString()
                            .toInt() < 1
                    ) binding?.power12ValueActPow?.setText("1")
                    if(binding?.power12ValueActPow?.text.toString()
                            .toInt() > saveStates.maxPower
                    ) binding?.power12ValueActPow?.setText(saveStates.maxPower.toString())
                } catch(e: Exception) {
                }
            }
        })
    }

    fun normalPower(view: View? = null) {
        try {
            if(saveStates.connectStatus) {
                var checkPowerValue = false
                CoroutineScope(Dispatchers.IO).launch {
                    launch(Dispatchers.Main) {
                        binding?.progressBarPower?.visibility = ProgressBar.VISIBLE
                        startPowerFABPowerFragTimer()
                    }
                    checkPowerValue = presenter.startPowerFullSuit(
                        binding?.power1ValueActPow?.text.toString().toInt(),
                        binding?.power2ValueActPow?.text.toString().toInt(),
                        binding?.power3ValueActPow?.text.toString().toInt(),
                        binding?.power4ValueActPow?.text.toString().toInt(),
                        binding?.power5ValueActPow?.text.toString().toInt(),
                        binding?.power6ValueActPow?.text.toString().toInt(),
                        binding?.power7ValueActPow?.text.toString().toInt(),
                        binding?.power8ValueActPow?.text.toString().toInt(),
                        binding?.power9ValueActPow?.text.toString().toInt(),
                        binding?.power10ValueActPow?.text.toString().toInt(),
                        binding?.power11ValueActPow?.text.toString().toInt(),
                        binding?.power12ValueActPow?.text.toString().toInt()
                    )
                    if(checkPowerValue) {
                        presenter.startModulationAndFrequencyFullSuit(thirdByteCommand) //saveStates.thirdByteCommand = thirdByteCommand
                    }
                    launch(Dispatchers.Main) {
                        if(!checkPowerValue) Toast.makeText(
                            this@FullSuitPowerActivity,
                            "Введенная мощность выше заданного значения!",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding?.progressBarPower?.visibility = ProgressBar.INVISIBLE
                    }
                }
            } else {
                val a1 = 1 / 0
            }
        } catch(e: Exception) {
            Toast.makeText(
                baseContext,
                "Ошибка при отправке мощности: проверьте подключение!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startPowerFABPowerFragTimer() {
        CoroutineScope(Dispatchers.IO).launch {
            launch(Dispatchers.Main) {
                binding?.startPowerFABPowerFrag?.isClickable = false
                binding?.startPowerFABPowerFrag?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.colorGrey))
                binding?.startPowerFABPowerFrag?.visibility = FloatingActionButton.INVISIBLE
            }
            delay(3000)
            launch(Dispatchers.Main) {
                binding?.startPowerFABPowerFrag?.isClickable = true
                binding?.startPowerFABPowerFrag?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.colorSecondary))
                binding?.startPowerFABPowerFrag?.visibility = FloatingActionButton.VISIBLE
            }
        }
    }

    private val mOnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { _, checkedId ->
        Log.e(
            powerActivityTAG,
            "$thirdByteCommand - start - mOnCheckedChangeListener - MainActivity - 3"
        )
        radioControl()
        when(checkedId) {
            R.id.radioFreq16 -> thirdByteCommand = (thirdByteCommand + 32).toByte()
            R.id.radioFreq32 -> thirdByteCommand = (thirdByteCommand + 16).toByte()
            R.id.radioFreq64 -> thirdByteCommand = (thirdByteCommand + 8).toByte()
            R.id.radioFreq128 -> thirdByteCommand = (thirdByteCommand + 4).toByte()
        }
        radioID = checkedId
        Log.e(
            powerActivityTAG, "$thirdByteCommand - end - OnCheckedChangeListener - MainActivity - 6"
        )
    }

    private fun radioControl() {
        Log.e(powerActivityTAG, "$thirdByteCommand - start - radioControl - MainActivity - 4")
        when(radioID) {
            R.id.radioFreq16 -> thirdByteCommand = (thirdByteCommand - 32).toByte()
            R.id.radioFreq32 -> thirdByteCommand = (thirdByteCommand - 16).toByte()
            R.id.radioFreq64 -> thirdByteCommand = (thirdByteCommand - 8).toByte()
            R.id.radioFreq128 -> thirdByteCommand = (thirdByteCommand - 4).toByte()
        }
        Log.e(powerActivityTAG, "$thirdByteCommand - end - radioControl - MainActivity - 5")
    }

    fun onModulationAm(view: View? = null) {
        if(binding?.switchAmModul?.isChecked == true) {
            thirdByteCommand = (thirdByteCommand + 0b1).toByte()
            switchAm = true
        } else {
            thirdByteCommand = (thirdByteCommand - 0b1).toByte()
            switchAm = false
        }
        Log.e(powerActivityTAG, "$thirdByteCommand - end - onModulationAm - MainActivity -")
    }

    fun onModulationFm(view: View? = null) {
        if(binding?.switchFmModul?.isChecked == true) {
            thirdByteCommand = (thirdByteCommand + 0b10).toByte()
            switchFm = true
        } else {
            thirdByteCommand = (thirdByteCommand - 0b10).toByte()
            switchFm = false
        }
        Log.e(powerActivityTAG, "$thirdByteCommand - end - onModulationFm - MainActivity -")
    }

    fun saveNewProfile(view: View) {
        if(dialog?.saveProfileET?.text!!.isEmpty() || checkExclamationMark()) {
            if(dialog?.saveProfileET?.text!!.isEmpty()) {
                Toast.makeText(
                    this, "Название профиля не должно быть пустым !", Toast.LENGTH_SHORT
                ).show()
            } else if(checkExclamationMark()) {
                Toast.makeText(
                    this, "Название профиля не должно содержать символа \"!\"", Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            dialog?.dismiss()
            val newProfile = binding?.radioGroupFreq?.checkedRadioButtonId?.let {
                ProfileFullSuit(
                    dialog?.saveProfileET?.text?.trim().toString(),
                    0,
                    it,
                    binding?.switchFmModul?.isChecked == true,
                    binding?.switchAmModul?.isChecked == true,
                    saveStates.changePower,
                    thirdByteCommand,
                    saveStates.maxPower,
                    binding?.power1ValueActPow?.text.toString().toInt(),
                    binding?.power2ValueActPow?.text.toString().toInt(),
                    binding?.power3ValueActPow?.text.toString().toInt(),
                    binding?.power4ValueActPow?.text.toString().toInt(),
                    binding?.power5ValueActPow?.text.toString().toInt(),
                    binding?.power6ValueActPow?.text.toString().toInt(),
                    binding?.power7ValueActPow?.text.toString().toInt(),
                    binding?.power8ValueActPow?.text.toString().toInt(),
                    binding?.power9ValueActPow?.text.toString().toInt(),
                    binding?.power10ValueActPow?.text.toString().toInt(),
                    binding?.power11ValueActPow?.text.toString().toInt(),
                    binding?.power12ValueActPow?.text.toString().toInt(),
                    0,
                    0,
                    false
                )
            }
            saveProfileFlag = true
            profileViewModel?.insertProfile(newProfile!!)
            dialog?.dismiss()
            finish()
        }
    }

    fun allCheckOn(view: View) {
        binding?.check1ActPow?.isChecked = true
        binding?.check2ActPow?.isChecked = true
        binding?.check3ActPow?.isChecked = true
        binding?.check4ActPow?.isChecked = true
        binding?.check5ActPow?.isChecked = true
        binding?.check6ActPow?.isChecked = true
        binding?.check7ActPow?.isChecked = true
        binding?.check8ActPow?.isChecked = true
        binding?.check9ActPow?.isChecked = true
        binding?.check10ActPow?.isChecked = true
        binding?.check11ActPow?.isChecked = true
        binding?.check12ActPow?.isChecked = true
        check1Power()
        check2Power()
        check3Power()
        check4Power()
        check5Power()
        check6Power()
        check7Power()
        check8Power()
        check9Power()
        check10Power()
        check11Power()
        check12Power()
    }

    fun allCheckOff(view: View) {
        binding?.check1ActPow?.isChecked = false
        binding?.check2ActPow?.isChecked = false
        binding?.check3ActPow?.isChecked = false
        binding?.check4ActPow?.isChecked = false
        binding?.check5ActPow?.isChecked = false
        binding?.check6ActPow?.isChecked = false
        binding?.check7ActPow?.isChecked = false
        binding?.check8ActPow?.isChecked = false
        binding?.check9ActPow?.isChecked = false
        binding?.check10ActPow?.isChecked = false
        binding?.check11ActPow?.isChecked = false
        binding?.check12ActPow?.isChecked = false
        check1Power()
        check2Power()
        check3Power()
        check4Power()
        check5Power()
        check6Power()
        check7Power()
        check8Power()
        check9Power()
        check10Power()
        check11Power()
        check12Power()
    }

    fun plusCheck(view: View) {
        if(binding?.check1ActPow?.isChecked == true && (binding?.power1ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power1ValueActPow?.setText(
            (binding?.power1ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check2ActPow?.isChecked == true && (binding?.power2ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power2ValueActPow?.setText(
            (binding?.power2ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check3ActPow?.isChecked == true && (binding?.power3ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power3ValueActPow?.setText(
            (binding?.power3ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check4ActPow?.isChecked == true && (binding?.power4ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power4ValueActPow?.setText(
            (binding?.power4ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check5ActPow?.isChecked == true && (binding?.power5ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power5ValueActPow?.setText(
            (binding?.power5ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check6ActPow?.isChecked == true && (binding?.power6ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power6ValueActPow?.setText(
            (binding?.power6ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check7ActPow?.isChecked == true && (binding?.power7ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power7ValueActPow?.setText(
            (binding?.power7ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check8ActPow?.isChecked == true && (binding?.power8ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power8ValueActPow?.setText(
            (binding?.power8ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check9ActPow?.isChecked == true && (binding?.power9ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power9ValueActPow?.setText(
            (binding?.power9ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check10ActPow?.isChecked == true && (binding?.power10ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power10ValueActPow?.setText(
            (binding?.power10ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check11ActPow?.isChecked == true && (binding?.power11ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power11ValueActPow?.setText(
            (binding?.power11ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(binding?.check12ActPow?.isChecked == true && (binding?.power12ValueActPow?.text.toString()
                .toInt() + saveStates.changePower <= saveStates.maxPower)
        ) binding?.power12ValueActPow?.setText(
            (binding?.power12ValueActPow?.text.toString()
                .toInt() + saveStates.changePower).toString()
        )

        if(saveStates.connectStatus) normalPower()
    }

    fun minusCheck(view: View) {
        if(binding?.check1ActPow?.isChecked == true && (binding?.power1ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power1ValueActPow?.setText(
            (binding?.power1ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check2ActPow?.isChecked == true && (binding?.power2ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power2ValueActPow?.setText(
            (binding?.power2ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check3ActPow?.isChecked == true && (binding?.power3ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power3ValueActPow?.setText(
            (binding?.power3ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check4ActPow?.isChecked == true && (binding?.power4ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power4ValueActPow?.setText(
            (binding?.power4ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check5ActPow?.isChecked == true && (binding?.power5ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power5ValueActPow?.setText(
            (binding?.power5ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check6ActPow?.isChecked == true && (binding?.power6ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power6ValueActPow?.setText(
            (binding?.power6ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check7ActPow?.isChecked == true && (binding?.power7ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power7ValueActPow?.setText(
            (binding?.power7ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check8ActPow?.isChecked == true && (binding?.power8ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power8ValueActPow?.setText(
            (binding?.power8ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check9ActPow?.isChecked == true && (binding?.power9ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power9ValueActPow?.setText(
            (binding?.power9ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check10ActPow?.isChecked == true && (binding?.power10ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power10ValueActPow?.setText(
            (binding?.power10ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check11ActPow?.isChecked == true && (binding?.power11ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power11ValueActPow?.setText(
            (binding?.power11ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(binding?.check12ActPow?.isChecked == true && (binding?.power12ValueActPow?.text.toString()
                .toInt() - saveStates.changePower > 0)
        ) binding?.power12ValueActPow?.setText(
            (binding?.power12ValueActPow?.text.toString()
                .toInt() - saveStates.changePower).toString()
        )

        if(saveStates.connectStatus) normalPower()
    }

    private fun checkRedObserver() {
        CoroutineScope(Dispatchers.IO).launch {
            while(true) {
                delay(800)
                launch(Dispatchers.Main) {
                    try {
                        if(binding?.power1ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power1ValueActPow?.text.toString() == ""
                        ) binding?.breastRedIV1?.alpha = binding?.power1ValueActPow?.text.toString()
                            .toFloat() * onePercentAlpha / 100
                        if(binding?.power2ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power2ValueActPow?.text.toString() == ""
                        ) binding?.pressRedIV2?.alpha = binding?.power2ValueActPow?.text.toString()
                            .toFloat() * onePercentAlpha / 100
                        if(binding?.power3ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power3ValueActPow?.text.toString() == ""
                        ) binding?.obliqueRedIV3?.alpha =
                            binding?.power3ValueActPow?.text.toString()
                                .toFloat() * onePercentAlpha / 100
                        if(binding?.power4ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power4ValueActPow?.text.toString() == ""
                        ) binding?.bicepsRedIV4?.alpha = binding?.power4ValueActPow?.text.toString()
                            .toFloat() * onePercentAlpha / 100
                        if(binding?.power5ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power5ValueActPow?.text.toString() == ""
                        ) binding?.tricepsRedIV5?.alpha =
                            binding?.power5ValueActPow?.text.toString()
                                .toFloat() * onePercentAlpha / 100
                        if(binding?.power6ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power6ValueActPow?.text.toString() == ""
                        ) binding?.trapezeRedIV6?.alpha =
                            binding?.power6ValueActPow?.text.toString()
                                .toFloat() * onePercentAlpha / 100
                        if(binding?.power7ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power7ValueActPow?.text.toString() == ""
                        ) binding?.backTopRedIV7?.alpha =
                            binding?.power7ValueActPow?.text.toString()
                                .toFloat() * onePercentAlpha / 100
                        if(binding?.power8ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power8ValueActPow?.text.toString() == ""
                        ) binding?.backBottomRedIV8?.alpha =
                            binding?.power8ValueActPow?.text.toString()
                                .toFloat() * onePercentAlpha / 100
                        if(binding?.power9ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power9ValueActPow?.text.toString() == ""
                        ) binding?.buttocksRedIV9?.alpha =
                            binding?.power9ValueActPow?.text.toString()
                                .toFloat() * onePercentAlpha / 100
                        if(binding?.power10ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power10ValueActPow?.text.toString() == ""
                        ) binding?.hipBicepsRedIV10?.alpha =
                            binding?.power10ValueActPow?.text.toString()
                                .toFloat() * onePercentAlpha / 100
                        if(binding?.power11ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power11ValueActPow?.text.toString() == ""
                        ) binding?.hipsRedIV11?.alpha = binding?.power11ValueActPow?.text.toString()
                            .toFloat() * onePercentAlpha / 100
                        if(binding?.power12ValueActPow?.text.toString()
                                .toInt() != 0 || binding?.power12ValueActPow?.text.toString() == ""
                        ) binding?.deltasRedIV12?.alpha =
                            binding?.power12ValueActPow?.text.toString()
                                .toFloat() * onePercentAlpha / 100
                    } catch(e: Exception) {
                        Log.e(powerActivityTAG, "-- checkObserver -- Exception -- ")
                    }
                }
            }
        }
    }

    fun goToNewProfile(view: View) {
        if(saveStates.powerActivityMode) {
            val view = layoutInflater.inflate(R.layout.fragment_new_profile, null)
            dialog?.apply {
                setContentView(view)
                show()
            }
        } else {
            saveStates.powerActivityMode = true
            updateProfile()
        }
    }

    fun deleteButtonPressed(view: View?) {
        if(!saveStates.powerActivityMode) {
            if(!saveProfileFlag) areYouSureDelete()
        } else {
            saveStates.powerActivityMode = true
            finish()
        }
    }

    fun deleteProfile(view: View?) {
        val updateData = profileViewModel!!.getAllProfiles().value
        for(item: ProfileFullSuit in updateData!!) if(item.name == data!!.value?.get(saveStates.position)?.name) {
            profileViewModel!!.deleteProfile(item)
            break
        }
        saveStates.powerActivityMode = true
        finish()
    }

    private fun updateProfile() {
        val updateData = profileViewModel!!.getAllProfiles().value
        for(item: ProfileFullSuit in updateData!!) if(item.name == data!!.value?.get(saveStates.position)?.name) {
            profileViewModel!!.deleteProfile(item)
            break
        }
        val updateProfile = ProfileFullSuit(
            data!!.value!![saveStates.position].name,
            0,
            binding?.radioGroupFreq?.checkedRadioButtonId,
            binding?.switchFmModul?.isChecked == true,
            binding?.switchAmModul?.isChecked == true,
            saveStates.changePower,
            thirdByteCommand,
            saveStates.maxPower,
            binding?.power1ValueActPow?.text.toString().toInt(),
            binding?.power2ValueActPow?.text.toString().toInt(),
            binding?.power3ValueActPow?.text.toString().toInt(),
            binding?.power4ValueActPow?.text.toString().toInt(),
            binding?.power5ValueActPow?.text.toString().toInt(),
            binding?.power6ValueActPow?.text.toString().toInt(),
            binding?.power7ValueActPow?.text.toString().toInt(),
            binding?.power8ValueActPow?.text.toString().toInt(),
            binding?.power9ValueActPow?.text.toString().toInt(),
            binding?.power10ValueActPow?.text.toString().toInt(),
            binding?.power11ValueActPow?.text.toString().toInt(),
            binding?.power12ValueActPow?.text.toString().toInt(),
            0,
            0,
            false
        )
        profileViewModel!!.insertProfile(updateProfile)
        finish()
    }

    private fun areYouSureDelete() {
        val view = layoutInflater.inflate(R.layout.fragment_delete_profile, null)
        dialog?.apply {
            setContentView(view)
            show()
        }
    }

    private fun areYouSureExiting() {
        val view = layoutInflater.inflate(R.layout.fragment_close_new_profile, null)
        dialog?.apply {
            setContentView(view)
            show()
        }
    }

    override fun onBackPressed() {
        if(saveStates.powerActivityMode) {
            if(!saveProfileFlag) areYouSureExiting()
        } else {
            saveStates.powerActivityMode = true
            finish()
        }
    }

    fun allStopPowerPowerActivityFullSuit(view: View) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                launch(Dispatchers.Main) {
                    binding?.progressBarPower?.visibility = ProgressBar.VISIBLE
                }
                presenter.allStopPowerCommandPresenterFullSuit()
                presenter.startModulationAndFrequencyFullSuit(0)
                launch(Dispatchers.Main) {
                    binding?.progressBarPower?.visibility = ProgressBar.INVISIBLE
                    binding?.power1ValueActPow?.setText("1")
                    binding?.power2ValueActPow?.setText("1")
                    binding?.power3ValueActPow?.setText("1")
                    binding?.power4ValueActPow?.setText("1")
                    binding?.power5ValueActPow?.setText("1")
                    binding?.power6ValueActPow?.setText("1")
                    binding?.power7ValueActPow?.setText("1")
                    binding?.power8ValueActPow?.setText("1")
                    binding?.power9ValueActPow?.setText("1")
                    binding?.power10ValueActPow?.setText("1")
                    binding?.power11ValueActPow?.setText("1")
                    binding?.power12ValueActPow?.setText("1")
                }
            }
        } catch(e: Exception) {
            Toast.makeText(
                this, "Ошибка при сбросе мощности: проверьте подключение!", Toast.LENGTH_LONG
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        onePercentAlpha = 100.0f / saveStates.maxPower.toFloat()

        if(saveStates.powerActivityMode) {
            binding?.powerActivityHeader?.text = "Новый профиль"
            binding?.maxPowerTVactivityPower?.text = "  Макс-ая мощность " + saveStates.maxPower
            binding?.deleteProfileButton?.visibility = ImageView.INVISIBLE
        } else {
            binding?.deleteProfileButton?.visibility = ImageView.VISIBLE

            Log.e(powerActivityTAG, "$thirdByteCommand - start - onResume - MainActivity - 1")
            val returnedData = profileViewModel!!.getAllProfiles()

            val profile = ProfileFullSuit(
                returnedData.value?.get(saveStates.position)!!.name,
                0,
                returnedData.value?.get(saveStates.position)!!.radioId,
                returnedData.value?.get(saveStates.position)!!.modulationFm,
                returnedData.value?.get(saveStates.position)!!.modulationAm,
                returnedData.value?.get(saveStates.position)!!.powerChange,
                returnedData.value?.get(saveStates.position)!!.thirdByteCommand,
                returnedData.value?.get(saveStates.position)!!.maxPower,
                returnedData.value?.get(saveStates.position)!!.power1,
                returnedData.value?.get(saveStates.position)!!.power2,
                returnedData.value?.get(saveStates.position)!!.power3,
                returnedData.value?.get(saveStates.position)!!.power4,
                returnedData.value?.get(saveStates.position)!!.power5,
                returnedData.value?.get(saveStates.position)!!.power6,
                returnedData.value?.get(saveStates.position)!!.power7,
                returnedData.value?.get(saveStates.position)!!.power8,
                returnedData.value?.get(saveStates.position)!!.power9,
                returnedData.value?.get(saveStates.position)!!.power10,
                returnedData.value?.get(saveStates.position)!!.power11,
                returnedData.value?.get(saveStates.position)!!.power12,
                0,
                0,
                false
            )

            binding?.powerActivityHeader?.text = profile.name
            binding?.switchAmModul?.isChecked = profile.modulationAm
            binding?.switchFmModul?.isChecked = profile.modulationFm
            binding?.maxPowerTVactivityPower?.text = profile.maxPower.toString()
            binding?.power1ValueActPow?.setText(profile.power1.toString())
            binding?.power2ValueActPow?.setText(profile.power2.toString())
            binding?.power3ValueActPow?.setText(profile.power3.toString())
            binding?.power4ValueActPow?.setText(profile.power4.toString())
            binding?.power5ValueActPow?.setText(profile.power5.toString())
            binding?.power6ValueActPow?.setText(profile.power6.toString())
            binding?.power7ValueActPow?.setText(profile.power7.toString())
            binding?.power8ValueActPow?.setText(profile.power8.toString())
            binding?.power9ValueActPow?.setText(profile.power9.toString())
            binding?.power10ValueActPow?.setText(profile.power10.toString())
            binding?.power11ValueActPow?.setText(profile.power11.toString())
            binding?.power12ValueActPow?.setText(profile.power12.toString())
            returnedData.value!![saveStates.position].radioId?.let {
                binding?.radioGroupFreq?.check(
                    it
                )
            }
            thirdByteCommand = profile.thirdByteCommand

            binding?.powerActivityHeader?.textSize = 30f
            binding?.maxPowerTVactivityPower?.text =
                "  Макс-ая мощность " + returnedData.value?.get(saveStates.position)!!.maxPower.toString()

            Log.e(powerActivityTAG, "$thirdByteCommand - end - onResume - MainActivity - 2")
        }
    }

    fun closePowerActivity(view: View) {
        if(saveStates.powerActivityMode) areYouSureExiting()
        else {
            finish()
            saveStates.powerActivityMode = true
        }
    }

    private fun checkExclamationMark(): Boolean =
        dialog?.saveProfileET?.text.toString().contains("!")

    fun closeSheetAndActivity(view: View) {
        dialog?.dismiss()
        saveStates.powerActivityMode = true
        finish()
    }

    fun closeSheet(view: View) {
        dialog?.dismiss()
    }

    fun check1Power(view: View? = null) {
        if(binding?.check1ActPow?.isChecked == true) binding?.breastBlueIV1?.visibility =
            ImageView.VISIBLE
        else binding?.breastBlueIV1?.visibility = ImageView.INVISIBLE
    }

    fun check2Power(view: View? = null) {
        if(binding?.check2ActPow?.isChecked == true) binding?.pressBlueIV2?.visibility =
            ImageView.VISIBLE
        else binding?.pressBlueIV2?.visibility = ImageView.INVISIBLE
    }

    fun check3Power(view: View? = null) {
        if(binding?.check3ActPow?.isChecked == true) binding?.obliqueBlueIV3?.visibility =
            ImageView.VISIBLE
        else binding?.obliqueBlueIV3?.visibility = ImageView.INVISIBLE
    }

    fun check4Power(view: View? = null) {
        if(binding?.check4ActPow?.isChecked == true) binding?.bicepsBlueIV4?.visibility =
            ImageView.VISIBLE
        else binding?.bicepsBlueIV4?.visibility = ImageView.INVISIBLE
    }

    fun check5Power(view: View? = null) {
        if(binding?.check5ActPow?.isChecked == true) binding?.tricepsBlueIV5?.visibility =
            ImageView.VISIBLE
        else binding?.tricepsBlueIV5?.visibility = ImageView.INVISIBLE
    }

    fun check6Power(view: View? = null) {
        if(binding?.check6ActPow?.isChecked == true) binding?.trapezeBlueIV6?.visibility =
            ImageView.VISIBLE
        else binding?.trapezeBlueIV6?.visibility = ImageView.INVISIBLE
    }

    fun check7Power(view: View? = null) {
        if(binding?.check7ActPow?.isChecked == true) binding?.backTopBlueIV7?.visibility =
            ImageView.VISIBLE
        else binding?.backTopBlueIV7?.visibility = ImageView.INVISIBLE
    }

    fun check8Power(view: View? = null) {
        if(binding?.check8ActPow?.isChecked == true) binding?.backBottomBlueIV8?.visibility =
            ImageView.VISIBLE
        else binding?.backBottomBlueIV8?.visibility = ImageView.INVISIBLE
    }

    fun check9Power(view: View? = null) {
        if(binding?.check9ActPow?.isChecked == true) binding?.buttocksBlueIV9?.visibility =
            ImageView.VISIBLE
        else binding?.buttocksBlueIV9?.visibility = ImageView.INVISIBLE
    }

    fun check10Power(view: View? = null) {
        if(binding?.check10ActPow?.isChecked == true) binding?.hipBicepsBlueIV10?.visibility =
            ImageView.VISIBLE
        else binding?.hipBicepsBlueIV10?.visibility = ImageView.INVISIBLE
    }

    fun check11Power(view: View? = null) {
        if(binding?.check11ActPow?.isChecked == true) binding?.hipsBlueIV11?.visibility =
            ImageView.VISIBLE
        else binding?.hipsBlueIV11?.visibility = ImageView.INVISIBLE
    }

    fun check12Power(view: View? = null) {
        if(binding?.check12ActPow?.isChecked == true) binding?.deltasBlueIV12?.visibility =
            ImageView.VISIBLE
        else binding?.deltasBlueIV12?.visibility = ImageView.INVISIBLE
    }
}