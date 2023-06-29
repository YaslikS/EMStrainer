package com.example.emstrainer.view.fitFragmentsAndActivity

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.emstrainer.R
import com.example.emstrainer.databinding.FragmentArBinding
import com.example.emstrainer.presenter.MainPresenter
import com.example.emstrainer.presenter.SaveStates
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuit
import com.example.emstrainer.viewModels.ProfileViewModel
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.SkeletonNode
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.coroutines.*

class ArFragment : Fragment() {
    private var fragmentArBinding: FragmentArBinding? = null
    var binding: FragmentArBinding? = null
    private val FitFragmentTAG = "ArFragment"
    private var arFragment: ArFragment? = null
    private var model: Uri? = null
    private var renderable: ModelRenderable? = null
    private var animator: ModelAnimator? = null
    private var saveStates = SaveStates
    private var profileViewModel: ProfileViewModel? = null
    private var data: LiveData<List<ProfileFullSuit>>? = null
    private var timer: CountDownTimer? = null
    private var presenter = MainPresenter
    private var numberFit = 0
    private var flagPlaceModel = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArBinding.inflate(inflater, container, false)
        fragmentArBinding = binding
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        arFragment = (childFragmentManager.findFragmentById(R.id.arFragment) as ArFragment?)!!
        model = Uri.parse("man.sfb")
        arFragment?.setOnTapArPlaneListener { hitResult, plane, _ ->
            if(!flagPlaceModel) {
                if(plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                    return@setOnTapArPlaneListener
                }
                val anchor = hitResult.createAnchor()
                placeObject(arFragment!!, anchor, model)
            }
        }

        when(saveStates.fitLevel) {
            1 -> data = profileViewModel?.getEasyProfiles()
            2 -> data = profileViewModel?.getMediumProfiles()
            3 -> data = profileViewModel?.getHardProfiles()
        }

        if(saveStates.fitMode) {
            binding?.TVNameFit?.text = data?.value?.get(saveStates.selectedFit)?.name
            binding?.TVNameFit1?.text = data?.value?.get(saveStates.selectedFit)?.name
            binding?.TVTimes?.text = data?.value?.get(saveStates.selectedFit)?.times.toString()
            if(data?.value?.get(saveStates.selectedFit)?.typeTimes != true) binding?.TVTypeTimes?.text = "секунд"
            else binding?.TVTypeTimes?.text = "раз"
            binding?.pushWnenEndButton?.text = "Закончить"
            binding?.missNowButton?.text = "Закончить"
        } else {
            binding?.TVNameFit?.text = "Выберете место, куда расположить тренера"
            binding?.TVNameFit1?.text = ""
            binding?.TVTimes?.text = ""
            binding?.TVTypeTimes?.text = ""
        }

        binding?.IVpause?.setOnClickListener { pauseAnimate() }
        binding?.missNowButton?.setOnClickListener { binding?.pushWnenEndButton?.performClick() }
        binding?.pushWnenEndButton?.setOnClickListener {
            if(saveStates.fitMode) activity?.onBackPressed()
            else loopFits()
        }
        binding?.SeekBar?.setOnTouchListener { view, motionEvent -> true }
        return binding?.root
    }

    private fun performingFit() {
        if(saveStates.fitMode) {
            // режим единичной тренировки
            if(!data?.value?.get(saveStates.selectedFit)?.typeTimes!!) {
                binding?.SeekBar?.visibility = SeekBar.VISIBLE
                binding?.TVRemainedTime?.visibility = TextView.VISIBLE
                binding?.SeekBar?.max = data?.value?.get(saveStates.selectedFit)?.times!!
                binding?.CLbottom?.visibility = ConstraintLayout.VISIBLE
                binding?.missNowButton?.visibility = Button.INVISIBLE
                timer =
                    object : CountDownTimer((data?.value?.get(saveStates.selectedFit)?.times!! * 1000).toLong(), 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            binding?.TVRemainedTime?.text =
                                (millisUntilFinished / 1000).toInt().toString()
                            binding?.SeekBar?.progress = (millisUntilFinished / 1000).toInt()
                        }

                        override fun onFinish() {
                            binding?.pushWnenEndButton?.visibility = Button.VISIBLE
                            binding?.SeekBar?.visibility = SeekBar.INVISIBLE
                            binding?.TVRemainedTime?.visibility = TextView.INVISIBLE
                            binding?.missNowButton?.visibility = Button.INVISIBLE
                            stopDevice()
                        }
                    }
                (timer as CountDownTimer).start()
            } else {
                binding?.pushWnenEndButton?.visibility = Button.VISIBLE
                binding?.missNowButton?.visibility = Button.INVISIBLE
            }
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    presenter.startPowerFullSuit(
                        data!!.value?.get(saveStates.selectedFit)?.power1!!,
                        data!!.value?.get(saveStates.selectedFit)?.power2!!,
                        data!!.value?.get(saveStates.selectedFit)?.power3!!,
                        data!!.value?.get(saveStates.selectedFit)?.power4!!,
                        data!!.value?.get(saveStates.selectedFit)?.power5!!,
                        data!!.value?.get(saveStates.selectedFit)?.power6!!,
                        data!!.value?.get(saveStates.selectedFit)?.power7!!,
                        data!!.value?.get(saveStates.selectedFit)?.power8!!,
                        data!!.value?.get(saveStates.selectedFit)?.power9!!,
                        data!!.value?.get(saveStates.selectedFit)?.power10!!,
                        data!!.value?.get(saveStates.selectedFit)?.power11!!,
                        data!!.value?.get(saveStates.selectedFit)?.power12!!
                    )
                }
            } catch(e: Exception){
                Log.e(FitFragmentTAG, "addOnItemTouchListener - Exception - presenter.startPower")
            }
        } else {
            // режим полной тренировки
            binding?.pushWnenEndButton?.visibility = Button.INVISIBLE
            binding?.TVNameFit?.text = data?.value?.get(numberFit)?.name
            binding?.TVNameFit1?.text = data?.value?.get(numberFit)?.name
            binding?.TVTimes?.text = data?.value?.get(numberFit)?.times.toString()
            if(!data?.value?.get(numberFit)?.typeTimes!!) binding?.TVTypeTimes?.text = "секунд"
            else binding?.TVTypeTimes?.text = "раз"

            if(timer != null) (timer as CountDownTimer).cancel()
            if(!data?.value?.get(numberFit)?.typeTimes!!) {
                binding?.SeekBar?.visibility = SeekBar.VISIBLE
                binding?.TVRemainedTime?.visibility = TextView.VISIBLE
                binding?.SeekBar?.max = data?.value!![numberFit].times
                binding?.CLbottom?.visibility = ConstraintLayout.VISIBLE
                binding?.missNowButton?.visibility = Button.VISIBLE
                timer = object : CountDownTimer(
                    data?.value?.get(numberFit)?.times?.times(1000)?.toLong()!!,
                    1000
                ) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding?.TVRemainedTime?.text =
                            (millisUntilFinished.toInt() / 1000).toString()
                        binding?.SeekBar?.progress = millisUntilFinished.toInt() / 1000
                    }

                    override fun onFinish() {
                        binding?.pushWnenEndButton?.visibility = Button.VISIBLE
                        binding?.SeekBar?.visibility = SeekBar.INVISIBLE
                        binding?.TVRemainedTime?.visibility = TextView.INVISIBLE
                        binding?.missNowButton?.visibility = Button.INVISIBLE
                        stopDevice()
                    }
                }
                (timer as CountDownTimer).start()
            } else {
                binding?.pushWnenEndButton?.visibility = Button.VISIBLE
                binding?.missNowButton?.visibility = Button.INVISIBLE
                binding?.SeekBar?.visibility = SeekBar.INVISIBLE
                binding?.TVRemainedTime?.visibility = TextView.INVISIBLE
            }
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    presenter.startPowerFullSuit(
                        data!!.value?.get(numberFit)?.power1!!,
                        data!!.value?.get(numberFit)?.power2!!,
                        data!!.value?.get(numberFit)?.power3!!,
                        data!!.value?.get(numberFit)?.power4!!,
                        data!!.value?.get(numberFit)?.power5!!,
                        data!!.value?.get(numberFit)?.power6!!,
                        data!!.value?.get(numberFit)?.power7!!,
                        data!!.value?.get(numberFit)?.power8!!,
                        data!!.value?.get(numberFit)?.power9!!,
                        data!!.value?.get(numberFit)?.power10!!,
                        data!!.value?.get(numberFit)?.power11!!,
                        data!!.value?.get(numberFit)?.power12!!
                    )
                }
            } catch(e: Exception){
                Log.e(FitFragmentTAG, "addOnItemTouchListener - Exception - presenter.startPower")
            }
            ++numberFit
        }
    }

    private fun loopFits() {
        // режим полной тренировки
        if(numberFit < data?.value?.size!!) {
            binding?.pushWnenEndButton?.visibility = Button.INVISIBLE
            binding?.TVNameFit?.text = data?.value?.get(numberFit)?.name
            binding?.TVNameFit1?.text = data?.value?.get(numberFit)?.name
            binding?.TVTimes?.text = data?.value?.get(numberFit)?.times.toString()
            if(!data?.value?.get(numberFit)?.typeTimes!!) binding?.TVTypeTimes?.text = "секунд"
            else binding?.TVTypeTimes?.text = "раз"

            if(timer != null) (timer as CountDownTimer).cancel()
            if(!data?.value?.get(numberFit)?.typeTimes!!) {
                binding?.SeekBar?.visibility = SeekBar.VISIBLE
                binding?.TVRemainedTime?.visibility = TextView.VISIBLE
                binding?.SeekBar?.max = data?.value!![numberFit].times
                binding?.CLbottom?.visibility = ConstraintLayout.VISIBLE
                binding?.missNowButton?.visibility = Button.VISIBLE
                timer = object : CountDownTimer(
                    data?.value?.get(numberFit)?.times?.times(1000)?.toLong()!!, 1000
                ) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding?.TVRemainedTime?.text =
                            (millisUntilFinished.toInt() / 1000).toString()
                        binding?.SeekBar?.progress = millisUntilFinished.toInt() / 1000
                    }

                    override fun onFinish() {
                        binding?.pushWnenEndButton?.visibility = Button.VISIBLE
                        binding?.SeekBar?.visibility = SeekBar.INVISIBLE
                        binding?.TVRemainedTime?.visibility = TextView.INVISIBLE
                        binding?.missNowButton?.visibility = Button.INVISIBLE
                        stopDevice()
                    }
                }
                (timer as CountDownTimer).start()
            } else {
                binding?.pushWnenEndButton?.visibility = Button.VISIBLE
                binding?.missNowButton?.visibility = Button.INVISIBLE
                binding?.SeekBar?.visibility = SeekBar.INVISIBLE
                binding?.TVRemainedTime?.visibility = TextView.INVISIBLE
            }
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    presenter.startPowerFullSuit(
                        data!!.value?.get(numberFit)?.power1!!,
                        data!!.value?.get(numberFit)?.power2!!,
                        data!!.value?.get(numberFit)?.power3!!,
                        data!!.value?.get(numberFit)?.power4!!,
                        data!!.value?.get(numberFit)?.power5!!,
                        data!!.value?.get(numberFit)?.power6!!,
                        data!!.value?.get(numberFit)?.power7!!,
                        data!!.value?.get(numberFit)?.power8!!,
                        data!!.value?.get(numberFit)?.power9!!,
                        data!!.value?.get(numberFit)?.power10!!,
                        data!!.value?.get(numberFit)?.power11!!,
                        data!!.value?.get(numberFit)?.power12!!
                    )
                }
            } catch(e: Exception){
                Log.e(FitFragmentTAG, "addOnItemTouchListener - Exception - presenter.startPower")
            }
            animateModel(data?.value?.get(numberFit)?.animationNumber!!)
            ++numberFit
            if(numberFit + 1 > data?.value?.size!!) {
                binding?.pushWnenEndButton?.text = "Закончить"
                binding?.missNowButton?.text = "Закончить"
            }
        } else {
            Toast.makeText(activity, "Вы сделали все упражнения!", Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        }
    }

    private fun stopDevice(){
        CoroutineScope(Dispatchers.IO).launch {
            presenter.allStopPowerCommandPresenterFullSuit()
            presenter.startModulationAndFrequencyFullSuit(0)
        }
    }

    private fun animateModel(number: Int) {
        animator?.let { it ->
            if(it.isRunning) it.end()
        }

        renderable?.let { modelRenderable ->
            val animData = modelRenderable.getAnimationData(number)
            animator = ModelAnimator(animData, modelRenderable)
            animator?.start()
            animator?.repeatCount = 999999
        }
    }

    private fun pauseAnimate() {
        if(!animator?.isPaused!!) {
            animator?.pause()
        }
        else {
            animator?.resume()
        }
    }

    private fun placeObject(arFragment: ArFragment, anchor: Anchor?, model: Uri?) {
        ModelRenderable.builder().setSource(arFragment.context, model).build().thenAccept {
            renderable = it
            addToScane(arFragment, anchor, it)
            flagPlaceModel = true
            if(saveStates.fitMode) animateModel(saveStates.selectedArAnimation)
            else animateModel(data?.value?.get(numberFit)?.animationNumber!!)
            performingFit()
        }.exceptionally {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage(it.message).setTitle("Error")
            val dialog = builder.create()
            dialog.show()
            return@exceptionally null
        }
    }

    private fun addToScane(arFragment: ArFragment, anchor: Anchor?, it: ModelRenderable?) {
        val anchorNode = AnchorNode(anchor)
        val skeletonNode = SkeletonNode()
        skeletonNode.renderable = renderable
        val node = TransformableNode(arFragment.transformationSystem)
        node.addChild(skeletonNode)
        node.setParent(anchorNode)
        node.scaleController.maxScale = 1.0f
        node.scaleController.minScale = 0.9999f
        node.localRotation = Quaternion.axisAngle(Vector3(0f, 1.5f, 0f), 90f)
        arFragment.arSceneView.scene.addChild(anchorNode)
    }

    override fun onDestroyView() {
        stopDevice()
        if(timer != null) (timer as CountDownTimer).cancel()
        numberFit = 0
        fragmentArBinding = null
        flagPlaceModel = false
        super.onDestroyView()
    }
}