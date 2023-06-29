package com.example.emstrainer.view.bottomNavigationView

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emstrainer.adapters.ProfileAdapter
import com.example.emstrainer.presenter.MainPresenter
import com.example.emstrainer.presenter.SaveStates
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuit
import com.example.emstrainer.recyclerAdapter.*
import com.example.emstrainer.viewModels.ProfileViewModel
import com.example.emstrainer.view.FullSuitPowerActivity
import com.google.android.material.snackbar.Snackbar
import com.example.emstrainer.databinding.FragmentProfileBinding
import kotlinx.coroutines.*

@SuppressLint("NotifyDataSetChanged")
class FullSuitProfileFragment : Fragment() {
    private var profileAdapter: ProfileAdapter? = null
    private var profileViewModel: ProfileViewModel? = null
    private var data: LiveData<List<ProfileFullSuit>>? = null
    private var saveStates = SaveStates
    private var presenter = MainPresenter
    var fragmentProfileBinding: FragmentProfileBinding? = null
    var binding: FragmentProfileBinding? = null
    private val profileFragmentTAG = "ProfileFragmentTAG"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        fragmentProfileBinding = binding
        binding!!.recyclerProfiles.adapter = profileAdapter
        binding!!.recyclerProfiles.layoutManager = LinearLayoutManager(requireContext())

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        profileAdapter = ProfileAdapter()
        data = profileViewModel!!.getAllProfiles()
        data!!.observe(viewLifecycleOwner, Observer {
            profileAdapter!!.setContentList(it)
            profileAdapter!!.notifyDataSetChanged()
            binding!!.recyclerProfiles.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding!!.recyclerProfiles.adapter = profileAdapter
        })

        binding!!.recyclerProfiles.addOnItemTouchListener(
            RecyclerItemClickListener(
                binding!!.recyclerProfiles,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        try {
                            Log.e(
                                profileFragmentTAG,
                                "addOnItemTouchListener - Try - presenter.startModulationAndFrequency"
                            )
                            data!!.value?.get(position)?.thirdByteCommand?.let {
                                presenter.startModulationAndFrequencyFullSuit(it)
                            }
                            //saveStates.thirdByteCommand = data?.value?.get(position)?.thirdByteCommand!!
                            saveStates.changePower = data!!.value?.get(position)?.powerChange!!
                            saveStates.maxPower = data!!.value?.get(position)?.maxPower!!
                            try {
                                Log.e(
                                    profileFragmentTAG,
                                    "addOnItemTouchListener - Try - presenter.startPower"
                                )
                                var checkPowerValue = false
                                CoroutineScope(Dispatchers.IO).launch {
                                    checkPowerValue = presenter.startPowerFullSuit(
                                        data!!.value?.get(position)?.power1!!,
                                        data!!.value?.get(position)?.power2!!,
                                        data!!.value?.get(position)?.power3!!,
                                        data!!.value?.get(position)?.power4!!,
                                        data!!.value?.get(position)?.power5!!,
                                        data!!.value?.get(position)?.power6!!,
                                        data!!.value?.get(position)?.power7!!,
                                        data!!.value?.get(position)?.power8!!,
                                        data!!.value?.get(position)?.power9!!,
                                        data!!.value?.get(position)?.power10!!,
                                        data!!.value?.get(position)?.power11!!,
                                        data!!.value?.get(position)?.power12!!
                                    )
                                    launch(Dispatchers.Main) {
                                        if(!checkPowerValue) Toast.makeText(
                                            context,
                                            "Введенная мощность выше заданного значения!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                /*
                                GlobalScope.launch {
                                    GlobalScope.async {
                                        checkPowerValue = presenter.startPowerFullSuit(
                                            data!!.value?.get(position)?.power1!!,
                                            data!!.value?.get(position)?.power2!!,
                                            data!!.value?.get(position)?.power3!!,
                                            data!!.value?.get(position)?.power4!!,
                                            data!!.value?.get(position)?.power5!!,
                                            data!!.value?.get(position)?.power6!!,
                                            data!!.value?.get(position)?.power7!!,
                                            data!!.value?.get(position)?.power8!!,
                                            data!!.value?.get(position)?.power9!!,
                                            data!!.value?.get(position)?.power10!!,
                                            data!!.value?.get(position)?.power11!!,
                                            data!!.value?.get(position)?.power12!!
                                        )
                                        launch(Dispatchers.Main) {
                                            if(!checkPowerValue) Toast.makeText(
                                                context,
                                                "Введенная мощность выше заданного значения!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                                */
                                Snackbar.make(
                                    binding!!.profilelayout,
                                    "Применен профиль ${data!!.value!![position].name}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } catch(e: Exception) {
                                Log.e(
                                    profileFragmentTAG,
                                    "addOnItemTouchListener - Exception - presenter.startPower"
                                )
                                Toast.makeText(
                                    context,
                                    "Ошибка применения профиля(мощность). Проверьте соединение с устройством!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch(e: Exception) {
                            Log.e(
                                profileFragmentTAG,
                                "addOnItemTouchListener - Exception - presenter.startModulationAndFrequency"
                            )
                            Toast.makeText(
                                context,
                                "Ошибка применения профиля. Проверьте соединение с устройством!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        )

        val swipe = object : RecyclerItemLeftSwipeListenener(
            context, binding!!.recyclerProfiles, 300
        ) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder, buffer: MutableList<SwipeButton>
            ) {
                context?.let {
                    SwipeButton(it,
                        "ИЗМЕНИТЬ",
                        35,
                        0,
                        Color.parseColor("#ECC60A"),
                        object : SwipeButtonClickListener {
                            override fun onClick(pos: Int) {
                                saveStates.position = pos
                                saveStates.powerActivityMode = false
                                //saveDataProfile()
                                val powerIntent =
                                    Intent(activity, FullSuitPowerActivity::class.java)
                                startActivity(powerIntent)
                            }
                        })
                }?.let { buffer.add(it) }
            }
        }

        return binding!!.root
    }

    fun swipeDeleteProfile(pos: Int) {
        Snackbar.make(
            binding!!.profilelayout, "Подтвердите уделение", Snackbar.LENGTH_LONG
        ).setAction("Удалить") {
            deleteProfile(pos)
        }.show()
        profileAdapter!!.notifyDataSetChanged()
    }

    private fun updateRecycler() {
        profileViewModel?.getAllProfiles()?.value?.let { profileAdapter?.setContentList(it) }
        profileAdapter?.notifyDataSetChanged()
    }

    private fun deleteProfile(pos: Int) {
        profileViewModel!!.deleteProfile(data!!.value!![pos])
        updateRecycler()
    }

    override fun onResume() {
        super.onResume()
        updateRecycler()
    }

    override fun onDestroyView() {
        fragmentProfileBinding = null
        super.onDestroyView()
    }
}