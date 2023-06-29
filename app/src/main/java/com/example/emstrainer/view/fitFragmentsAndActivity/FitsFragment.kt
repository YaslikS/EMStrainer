package com.example.emstrainer.view.fitFragmentsAndActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emstrainer.R
import com.example.emstrainer.adapters.FitProfileAdapter
import com.example.emstrainer.databinding.FragmentFitsBinding
import com.example.emstrainer.presenter.SaveStates
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuit
import com.example.emstrainer.recyclerAdapter.RecyclerItemClickListener
import com.example.emstrainer.viewModels.ProfileViewModel

@SuppressLint("NotifyDataSetChanged")
class FitsFragment : Fragment() {
    private var fragmentFitsBinding: FragmentFitsBinding? = null
    var binding: FragmentFitsBinding? = null
    private var saveStates = SaveStates
    private val FitFragmentTAG = "FitFragment"
    private var profileAdapter: FitProfileAdapter? = null
    private var profileViewModel: ProfileViewModel? = null
    private var data: LiveData<List<ProfileFullSuit>>? = null
    private var arFragment = ArFragment()
    private var arHelmFragment = ArHelmFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFitsBinding.inflate(inflater, container, false)
        fragmentFitsBinding = binding

        binding!!.recyclerProfilesFits.adapter = profileAdapter
        binding!!.recyclerProfilesFits.layoutManager = LinearLayoutManager(requireContext())
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        profileAdapter = FitProfileAdapter()

        when(saveStates.fitLevel) {
            1 -> {
                data = profileViewModel?.getEasyProfiles()
                if(data?.value?.isEmpty()!!) generateEasyFitPrograms()
                (activity as FitProgrammsActivity?)?.binding?.powerActivityHeader?.text =
                    "Начинающий"
            }
            2 -> {
                data = profileViewModel?.getMediumProfiles()
                if(data?.value?.isEmpty()!!) generateMediumFitPrograms()
                (activity as FitProgrammsActivity?)?.binding?.powerActivityHeader?.text =
                    "Любительский"
            }
            3 -> {
                data = profileViewModel?.getHardProfiles()
                if(data?.value?.isEmpty()!!) generateHardFitPrograms()
                (activity as FitProgrammsActivity?)?.binding?.powerActivityHeader?.text =
                    "Профессионал"
            }
        }

        binding?.recyclerProfilesFits?.addOnItemTouchListener(
            RecyclerItemClickListener(binding?.recyclerProfilesFits!!,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        try {
                            saveStates.selectedArAnimation =
                                data?.value?.get(position)?.animationNumber!!
                            saveStates.fitMode = true
                            saveStates.selectedFit = position
                            if(saveStates.arMode) openFragment(arFragment)
                            else openFragment(arHelmFragment)
                        } catch(e: Exception) {
                            Log.d(
                                FitFragmentTAG,
                                "-!-!-!-!-!- recyclerDevices.addOnItemTouchListener - Exception -!-!-!-!-!-"
                            )
                        }
                    }
                })
        )

        binding?.startARFullmodeButton?.setOnClickListener {
            saveStates.fitMode = false
            openFragment(arFragment)
        }

        if(!saveStates.arMode) binding?.startARFullmodeButton?.visibility = Button.INVISIBLE
        else binding?.startARFullmodeButton?.visibility = Button.VISIBLE

        data!!.observe(viewLifecycleOwner, Observer {
            profileAdapter?.setContentList(it)
            profileAdapter?.notifyDataSetChanged()
            binding!!.recyclerProfilesFits.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )
            binding?.recyclerProfilesFits?.adapter = profileAdapter
        })
        return binding?.root!!
    }

    private fun openFragment(twoFrag: Fragment) {
        val fragmentManager: FragmentManager? = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.containerFitActivity, twoFrag)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun generateEasyFitPrograms() {
        var newProfile = ProfileFullSuit(
            "Приседания",
            1,
            0,
            false,
            false,
            1,
            0,
            30,
            1,
            1,
            1,
            1,
            1,
            1,
            20,
            20,
            20,
            20,
            20,
            1,
            15,
            0,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Круги руками в разные стороны",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            20,
            20,
            1,
            20,
            1,
            1,
            1,
            1,
            20,
            20,
            1,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Махи руками в обе стороны",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            20,
            20,
            1,
            20,
            1,
            1,
            1,
            1,
            20,
            14,
            2,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Наклоны головы к плечам и прижатие их руками на каждую сторону",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            20,
            20,
            20,
            1,
            1,
            1,
            1,
            1,
            20,
            10,
            3,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Прижимания коленей к груди и стойка на другой ноге",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            20,
            20,
            20,
            1,
            20,
            4,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Кручение таза",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            20,
            1,
            1,
            1,
            1,
            20,
            20,
            20,
            1,
            1,
            1,
            10,
            5,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Лежа велосипед ногами",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            10,
            10,
            10,
            10,
            1,
            10,
            10,
            10,
            10,
            10,
            1,
            10,
            6,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Альпинист",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            15,
            1,
            20,
            20,
            20,
            1,
            1,
            1,
            1,
            15,
            15,
            15,
            20,
            7,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Бег на месте",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            20,
            8,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Пресс лежа",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            20,
            20,
            20,
            1,
            15,
            9,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Мельница на каждую сторону",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            10,
            10,
            10,
            10,
            10,
            1,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            13,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Вращение рук в локтях на каждую сторону",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            20,
            20,
            20,
            20,
            20,
            1,
            20,
            20,
            1,
            1,
            1,
            20,
            10,
            14,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Эмбрион",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            20,
            20,
            20,
            1,
            1,
            20,
            20,
            20,
            1,
            1,
            1,
            1,
            30,
            19,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Скручивания в пояснице лежа на боку на каждую сторону",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            13,
            13,
            13,
            13,
            13,
            13,
            13,
            13,
            13,
            10,
            20,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Выпады в каждую сторону",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            20,
            20,
            20,
            1,
            1,
            15,
            15,
            15,
            15,
            15,
            15,
            1,
            10,
            11,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Наклоны с руками за головой на каждую сторону",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            15,
            15,
            15,
            15,
            15,
            1,
            1,
            1,
            15,
            15,
            15,
            15,
            10,
            16,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Cтойка на лопатках",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            15,
            15,
            15,
            15,
            15,
            1,
            20,
            22,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Поза змеи",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            20,
            20,
            20,
            1,
            1,
            20,
            20,
            20,
            20,
            1,
            1,
            1,
            30,
            30,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Боковые наклоны к стопам на каждую сторону",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            20,
            20,
            20,
            1,
            1,
            20,
            20,
            20,
            20,
            1,
            1,
            1,
            10,
            12,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Отжимания с упором на колени",
            1,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            15,
            15,
            1,
            1,
            1,
            1,
            1,
            1,
            15,
            10,
            17,
            true
        )
        profileViewModel?.insertProfile(newProfile)

        profileViewModel?.getEasyProfiles()?.value?.let { profileAdapter?.setContentList(it) }
        profileAdapter?.notifyDataSetChanged()
    }

    private fun generateMediumFitPrograms() {
        var newProfile = ProfileFullSuit(
            "Стойка в присяде",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            35,
            35,
            35,
            35,
            35,
            35,
            1,
            20,
            21,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Вынос бедра из выпада на каждую ногу",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            35,
            35,
            35,
            1,
            1,
            35,
            35,
            35,
            35,
            30,
            23,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Вытягивание тела в горизонтальную плоскость в наклоне на каждую ногу",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            35,
            35,
            35,
            35,
            35,
            35,
            1,
            40,
            26,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Упражнение «дерево» на каждой ноге",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            35,
            35,
            35,
            35,
            35,
            35,
            35,
            35,
            1,
            30,
            27,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Подъем руки и ноги на четвереньках на каждую сторону",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            35,
            35,
            1,
            1,
            1,
            35,
            35,
            35,
            35,
            30,
            28,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Поза змеи",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            35,
            35,
            35,
            1,
            1,
            35,
            35,
            35,
            1,
            1,
            1,
            1,
            45,
            30,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Удержание тела у стены",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            35,
            35,
            35,
            35,
            35,
            1,
            30,
            34,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Прыжки вбок",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            35,
            35,
            35,
            1,
            14,
            39,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Удар ногой",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            35,
            35,
            35,
            35,
            35,
            1,
            30,
            38,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Cтойка на лопатках",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            35,
            35,
            35,
            25,
            25,
            1,
            30,
            22,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Расстяжка четырехглавой мышцы на каждую ногу",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            35,
            35,
            1,
            1,
            1,
            35,
            35,
            35,
            35,
            20,
            18,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Отжимания с упором на колени",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            35,
            35,
            1,
            1,
            1,
            1,
            1,
            1,
            35,
            15,
            17,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Боковые наклоны к стопам на каждую сторону",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            35,
            35,
            35,
            35,
            35,
            1,
            1,
            1,
            1,
            40,
            12,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Выпады в сторону",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            35,
            35,
            35,
            1,
            1,
            35,
            35,
            35,
            25,
            25,
            25,
            1,
            30,
            33,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Уголок – руки вперед",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            35,
            35,
            35,
            35,
            35,
            1,
            1,
            1,
            1,
            1,
            1,
            35,
            30,
            10,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Бег на месте",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            35,
            35,
            35,
            35,
            1,
            30,
            8,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Лежа велосипед ногами",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            35,
            35,
            1,
            1,
            1,
            35,
            35,
            35,
            35,
            35,
            35,
            1,
            20,
            6,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Приседания",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            35,
            35,
            35,
            1,
            25,
            0,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Альпинист",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            35,
            35,
            35,
            1,
            1,
            1,
            1,
            1,
            35,
            35,
            35,
            1,
            30,
            7,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Вращение рук в локтях",
            2,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            35,
            35,
            1,
            1,
            1,
            1,
            1,
            1,
            35,
            15,
            14,
            true
        )
        profileViewModel?.insertProfile(newProfile)

        profileViewModel?.getMediumProfiles()?.value?.let { profileAdapter?.setContentList(it) }
        profileAdapter?.notifyDataSetChanged()
    }

    private fun generateHardFitPrograms() {
        var newProfile = ProfileFullSuit(
            "Шаги вбок в планке в каждую сторону",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            48,
            48,
            1,
            48,
            48,
            1,
            1,
            1,
            48,
            10,
            15,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Ягодичный мостик на одной ноге на каждую ногу",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            1,
            1,
            48,
            48,
            48,
            48,
            48,
            48,
            1,
            16,
            24,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Уголок – руки в стороны",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            48,
            48,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            40,
            25,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Приседания на одной ноге",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            48,
            48,
            48,
            1,
            20,
            29,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Боковая планка на каждую на руку",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            1,
            1,
            1,
            48,
            48,
            48,
            1,
            1,
            1,
            40,
            31,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Планка с попеременным подъемом рук над головой на каждую руку",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            1,
            1,
            48,
            20,
            32,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Отжимания с хлопком по плечу на каждую на руку",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            48,
            48,
            1,
            1,
            1,
            1,
            1,
            1,
            48,
            20,
            33,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Наклоны тела назад сидя на коленях",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            1,
            1,
            48,
            48,
            48,
            48,
            48,
            48,
            1,
            20,
            35,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Подъемы ноги с упором второй в лежачем положении на каждую ногу",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            1,
            1,
            48,
            48,
            48,
            48,
            48,
            48,
            1,
            20,
            36,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Подъем ног и тела в лежачем положении на боку на каждый бок",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            20,
            37,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Прыжки вбок",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            48,
            48,
            48,
            1,
            14,
            39,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Перенос веса с одного колена другое",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            48,
            48,
            48,
            1,
            18,
            40,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Шаги к рукам в положении полулежа на каждую руку",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            1,
            1,
            1,
            1,
            1,
            48,
            48,
            48,
            1,
            30,
            41,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Удержание тела у стены",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            1,
            1,
            1,
            48,
            48,
            48,
            48,
            48,
            1,
            45,
            34,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Поза змеи",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            1,
            1,
            1,
            48,
            48,
            1,
            1,
            1,
            1,
            60,
            30,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Подъем руки и ноги на четвереньках на каждую сторону",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            48,
            48,
            1,
            1,
            1,
            48,
            48,
            48,
            1,
            40,
            28,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Стойка в присяде",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            48,
            48,
            1,
            1,
            1,
            48,
            48,
            48,
            48,
            45,
            21,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Вынос бедра из выпада на каждую ногу",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            1,
            1,
            1,
            48,
            48,
            1,
            1,
            1,
            48,
            48,
            48,
            48,
            40,
            23,
            true
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Вытягивание тела в горизонтальную плоскость в наклоне на каждой ноге",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            1,
            1,
            48,
            48,
            48,
            48,
            48,
            48,
            1,
            60,
            26,
            false
        )
        profileViewModel?.insertProfile(newProfile)
        newProfile = ProfileFullSuit(
            "Упражнение «дерево» на каждой ноге",
            3,
            0,
            false,
            false,
            1,
            0,
            125,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            48,
            50,
            27,
            false
        )
        profileViewModel?.insertProfile(newProfile)

        profileViewModel?.getHardProfiles()?.value?.let { profileAdapter?.setContentList(it) }
        profileAdapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        binding?.recyclerProfilesFits?.scrollToPosition(0)
        (activity as FitProgrammsActivity).binding?.topCL?.visibility = ConstraintLayout.VISIBLE
        (activity as FitProgrammsActivity).binding?.topCLblack?.visibility = ConstraintLayout.INVISIBLE
        (activity as FitProgrammsActivity).binding?.view2?.visibility = View.VISIBLE
        super.onResume()
    }

    override fun onDestroyView() {
        fragmentFitsBinding = null
        super.onDestroyView()
    }
}