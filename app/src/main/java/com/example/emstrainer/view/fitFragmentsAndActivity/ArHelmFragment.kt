package com.example.emstrainer.view.fitFragmentsAndActivity

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.PixelCopy
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.emstrainer.R
import com.example.emstrainer.databinding.FragmentArHelmBinding
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

class ArHelmFragment : Fragment() {
    private var fragmentArHelmFragment: FragmentArHelmBinding? = null
    var binding: FragmentArHelmBinding? = null
    private val FitFragmentTAG = "ArHelmFragment"
    private var arHelmFragment: ArFragment? = null
    private var model: Uri? = null
    private var renderable: ModelRenderable? = null
    private var animator: ModelAnimator? = null
    private var saveStates = SaveStates
    private var profileViewModel: ProfileViewModel? = null
    private var data: LiveData<List<ProfileFullSuit>>? = null
    private var timer: CountDownTimer? = null
    private var presenter = MainPresenter
    private var flagPlaceModel = false
    private var job: Job = CoroutineScope(Dispatchers.IO).launch {
        delay(70)
        getScreenShot()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArHelmBinding.inflate(inflater, container, false)
        fragmentArHelmFragment = binding
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        arHelmFragment =
            (childFragmentManager.findFragmentById(R.id.arHelmFragment) as ArFragment?)!!
        model = Uri.parse("man.sfb")
        arHelmFragment?.setOnTapArPlaneListener { hitResult, plane, _ ->
            if(!flagPlaceModel) {
                if(plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                    return@setOnTapArPlaneListener
                }
                val anchor = hitResult.createAnchor()
                placeObject(arHelmFragment!!, anchor, model)
            }
        }

        when(saveStates.fitLevel) {
            1 -> data = profileViewModel?.getEasyProfiles()
            2 -> data = profileViewModel?.getMediumProfiles()
            3 -> data = profileViewModel?.getHardProfiles()
        }

        binding?.SeekBarHelm?.setOnTouchListener { view, motionEvent -> true }
        binding?.SeekBarHelm1?.setOnTouchListener { view, motionEvent -> true }

        binding?.TVNameFitHelm?.text = data?.value?.get(saveStates.selectedFit)?.name
        binding?.TVTimesHelm?.text = data?.value?.get(saveStates.selectedFit)?.times.toString()
        if(data?.value?.get(saveStates.selectedFit)?.typeTimes != true) binding?.TVTypeTimesHelm?.text =
            "секунд"
        else binding?.TVTypeTimesHelm?.text = "раз"
        binding?.TVNameFitHelm1?.text = data?.value?.get(saveStates.selectedFit)?.name
        binding?.TVTimesHelm1?.text = data?.value?.get(saveStates.selectedFit)?.times.toString()
        if(data?.value?.get(saveStates.selectedFit)?.typeTimes != true) binding?.TVTypeTimesHelm1?.text =
            "секунд"
        else binding?.TVTypeTimesHelm1?.text = "раз"

        (activity as FitProgrammsActivity).binding?.topCL?.visibility = ConstraintLayout.INVISIBLE
        (activity as FitProgrammsActivity).binding?.topCLblack?.visibility = ConstraintLayout.VISIBLE
        (activity as FitProgrammsActivity).binding?.view2?.visibility = View.INVISIBLE

        binding?.IVpause?.setOnClickListener { pauseAnimate() }

        return binding?.root
    }

    private fun performingFit() {
        if(!data?.value?.get(saveStates.selectedFit)?.typeTimes!!) {
            binding?.SeekBarHelm?.visibility = SeekBar.VISIBLE
            binding?.SeekBarHelm1?.visibility = SeekBar.VISIBLE
            binding?.TVRemainedTimeHelm?.visibility = TextView.VISIBLE
            binding?.TVRemainedTimeHelm1?.visibility = TextView.VISIBLE
            binding?.SeekBarHelm?.max = data?.value?.get(saveStates.selectedFit)?.times!!
            binding?.SeekBarHelm1?.max = data?.value?.get(saveStates.selectedFit)?.times!!
            timer = object : CountDownTimer(
                (data?.value?.get(saveStates.selectedFit)?.times!! * 1000).toLong(), 1000
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    binding?.TVRemainedTimeHelm?.text =
                        (millisUntilFinished / 1000).toInt().toString()
                    binding?.TVRemainedTimeHelm1?.text =
                        (millisUntilFinished / 1000).toInt().toString()
                    binding?.SeekBarHelm?.progress = (millisUntilFinished / 1000).toInt()
                    binding?.SeekBarHelm1?.progress = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    binding?.SeekBarHelm?.visibility = SeekBar.INVISIBLE
                    binding?.SeekBarHelm1?.visibility = SeekBar.INVISIBLE
                    binding?.TVRemainedTimeHelm?.visibility = TextView.INVISIBLE
                    binding?.TVRemainedTimeHelm1?.visibility = TextView.INVISIBLE
                    stopDevice()
                }
            }
            (timer as CountDownTimer).start()
        } else { // TODO
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
        } catch(e: Exception) {
            Log.e(FitFragmentTAG, "addOnItemTouchListener - Exception - presenter.startPower")
        }
    }

    private fun animateModel(number: Int) {
        animator?.let { it ->
            if(it.isRunning) it.end()
        }

        renderable?.let { modelRenderable ->
            val data = modelRenderable.getAnimationData(number)
            animator = ModelAnimator(data, modelRenderable)
            animator?.start()
            animator?.repeatCount = 999999
        }
    }

    private fun pauseAnimate() {
        if(!animator?.isPaused!!) animator?.pause()
        else animator?.resume()
    }

    private fun placeObject(arFragment: ArFragment, anchor: Anchor?, model: Uri?) {
        ModelRenderable.builder().setSource(arFragment.context, model).build().thenAccept {
            renderable = it
            addToScane(arFragment, anchor, it)
            flagPlaceModel = true
            animateModel(saveStates.selectedArAnimation)
            performingFit()
        }.exceptionally {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage(it.message).setTitle("Error")
            val dialog = builder.create()
            dialog.show()
            return@exceptionally null
        }
    }

    private fun stopDevice() {
        CoroutineScope(Dispatchers.IO).launch {
            presenter.allStopPowerCommandPresenterFullSuit()
            presenter.startModulationAndFrequencyFullSuit(0)
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

    private fun getScreenShot() {
        CoroutineScope(Dispatchers.IO).launch {
            while(true) {
                delay(10)
                launch(Dispatchers.Main) {
                    try {
                        val returnedBitmap = Bitmap.createBitmap(
                            arHelmFragment!!.arSceneView.width,
                            arHelmFragment!!.arSceneView.height,
                            Bitmap.Config.ARGB_8888
                        )
                        arHelmFragment?.arSceneView?.let {
                            PixelCopy.request(
                                it, returnedBitmap, {}, Handler(Looper.getMainLooper())
                            )
                        }
                        binding?.bottomIV?.setImageBitmap(returnedBitmap)
                    } catch(e: Exception) {
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        stopDevice()
        job.cancel()
        if(timer != null) (timer as CountDownTimer).cancel()
        fragmentArHelmFragment = null
        flagPlaceModel = false
        super.onDestroyView()
    }
}