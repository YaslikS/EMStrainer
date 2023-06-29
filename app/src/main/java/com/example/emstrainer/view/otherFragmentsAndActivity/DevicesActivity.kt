package com.example.emstrainer.view.otherFragmentsAndActivity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emstrainer.R
import com.example.emstrainer.adapters.DeviceAdapter
import com.example.emstrainer.databinding.ActivityDevicesBinding
import com.example.emstrainer.presenter.MainPresenter
import com.example.emstrainer.presenter.SaveStates
import com.example.emstrainer.model.devices.Devices
import com.example.emstrainer.recyclerAdapter.RecyclerItemClickListener
import com.example.emstrainer.recyclerAdapter.RecyclerItemLeftSwipeListenener
import com.example.emstrainer.recyclerAdapter.SwipeButton
import com.example.emstrainer.recyclerAdapter.SwipeButtonClickListener
import com.example.emstrainer.viewModels.DeviceViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_name_device.*
import kotlinx.coroutines.*
import java.util.*

class DevicesActivity : AppCompatActivity() {
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var devicesList = ArrayList<BluetoothDevice>()
    private var saveStates = SaveStates
    private var presenter = MainPresenter
    private var dialogConnecting: BottomSheetDialog? = null
    private var dialogSaveDevice: BottomSheetDialog? = null
    private var devicesActivityTAG = "DevicesActivity"
    private var flagBeDevice = false
    private var deviceAdapter: DeviceAdapter? = null
    private var deviceViewModel: DeviceViewModel? = null
    private var data: LiveData<List<Devices>>? = null
    var binding: ActivityDevicesBinding? = null
    //private val device: BluetoothDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevicesBinding.inflate(LayoutInflater.from(this))
        val view = binding!!.root
        setContentView(binding!!.root)

        connectionStatusObserver(true)
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        dialogConnecting = BottomSheetDialog(this)
        dialogSaveDevice = BottomSheetDialog(this)
        devicesList.clear()

        deviceViewModel = ViewModelProvider(this)[DeviceViewModel::class.java]
        deviceAdapter = DeviceAdapter()
        data = deviceViewModel!!.getAllDevices()
        data!!.observe(this, Observer {
            deviceAdapter!!.setContentList(it)
            deviceAdapter!!.notifyDataSetChanged()
            binding!!.recyclerProfiles.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding!!.recyclerProfiles.adapter = deviceAdapter
        })

        binding!!.recyclerProfiles.addOnItemTouchListener(
            RecyclerItemClickListener(
                binding!!.recyclerProfiles,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        try {
                            connectingWithDevice(
                                data!!.value!![position].name, data!!.value!![position].numberDevice
                            )
                        } catch(e: Exception) {
                            Log.d(
                                devicesActivityTAG,
                                "-!-!-!-!-!- recyclerDevices.addOnItemTouchListener - Exception -!-!-!-!-!-"
                            )
                        }
                    }
                })
        )

        val swipe = object : RecyclerItemLeftSwipeListenener(
            this, binding!!.recyclerProfiles, 300
        ) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder, buffer: MutableList<SwipeButton>
            ) {
                SwipeButton(this@DevicesActivity,
                    "УДАЛИТЬ",
                    35,
                    0,
                    Color.parseColor("#C01818"),
                    object : SwipeButtonClickListener {
                        override fun onClick(pos: Int) {
                            swipeDeleteDevice(pos)
                        }
                    }).let { buffer.add(it) }
            }
        }
    }

    private fun swipeDeleteDevice(pos: Int) {
        Snackbar.make(
            binding!!.mainLayoutDevices, "Подтвердите уделение", Snackbar.LENGTH_LONG
        ).setAction("Удалить") {
            deleteDevice(pos)
        }.show()
        deviceAdapter?.notifyDataSetChanged()
    }

    private fun deleteDevice(pos: Int) {
        deviceViewModel!!.deleteDevice(data?.value!![pos])
        updateRecycler()
        //presenter.unpairDevice()
    }

    fun connectingWithDevice(name: String, address: String) {
        if(address != saveStates.addressSelectDevice) {
            CoroutineScope(Dispatchers.IO).launch {
                launch(Dispatchers.Main) {
                    val view = layoutInflater.inflate(R.layout.fragment_connecting____, null)
                    dialogConnecting?.apply {
                        setCancelable(false)
                        setContentView(view)
                        show()
                    }
                }
                closeConnect()
                val flagOnConnect = presenter.onConnectFullSuit(address)
                Log.d(
                    devicesActivityTAG, "true-----------------GlobalScope.async - $flagOnConnect"
                )
                if(flagOnConnect) {
                    launch(Dispatchers.Main) {
                        saveStates.connectStatus = true
                        presenter.startBroadcastCommandPresenterFullSuit()
                        dialogConnecting?.dismiss()
                        Snackbar.make(
                            binding!!.mainLayoutDevices,
                            "Костюм $name подключен",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding!!.closeConnectFAB.visibility = FloatingActionButton.VISIBLE
                        binding!!.connectStatusCircleIVInDevices.setImageResource(R.drawable.green_circle)
                        binding!!.nameDeviceMainInDeviceActivity.text = name
                        saveStates.nameSelectDevice = name
                        saveStates.addressSelectDevice = address
                    }
                } else {
                    launch(Dispatchers.Main) {
                        saveStates.connectStatus = false
                        dialogConnecting?.dismiss()
                        errorStartJob()
                        binding!!.connectStatusCircleIVInDevices.setImageResource(R.drawable.red_circle)
                        Log.d(
                            devicesActivityTAG,
                            "false-----------------launch(Dispatchers.Main) - ${saveStates.connectStatus}"
                        )
                    }
                }
            }
        } else {
            Snackbar.make(
                binding!!.mainLayoutDevices, "Костюм $name уже подключен!", Snackbar.LENGTH_LONG
            ).show()
        }
    }

    fun saveNewDevice(view: View) {
        val newDevice =
            Devices(devicesList[0].address, dialogSaveDevice!!.saveDeviceET.text?.trim().toString())
        deviceViewModel?.insertDevice(newDevice)
        updateRecycler()
        saveStates.nameSelectDevice = dialogSaveDevice!!.saveDeviceET.text?.trim().toString()
        saveStates.addressSelectDevice = devicesList[0].address
        devicesList.clear()
        dialogSaveDevice!!.dismiss()
    }

    private fun updateRecycler() {
        deviceViewModel?.getAllDevices()?.value?.let { deviceAdapter?.setContentList(it) }
        deviceAdapter?.notifyDataSetChanged()
    }

    fun searchDevice(view: View?) {
        devicesList.clear()
        CoroutineScope(Dispatchers.IO).launch {
                launch(Dispatchers.Main) {
                    startDiscovering()
                }
                delay(6000)
                launch(Dispatchers.Main) {
                    foundedSuit()
                }
            }
    }

    private fun startDiscovering() {
        val view = layoutInflater.inflate(R.layout.fragment_connecting____, null)
        dialogConnecting?.apply {
            setCancelable(false)
            setContentView(view)
            show()
        }
        if(mBluetoothAdapter!!.isDiscovering) {
            mBluetoothAdapter!!.cancelDiscovery()
            devicesList.clear()
            mBluetoothAdapter!!.startDiscovery()
            val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(mBroadcastReceiver, discoverDevicesIntent)
        } else {
            mBluetoothAdapter!!.startDiscovery()
            val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(mBroadcastReceiver, discoverDevicesIntent)
        }
    }

    private fun foundedSuit() {
        //Toast.makeText(this, devicesList.size.toString(), Toast.LENGTH_SHORT).show()
        if(mBluetoothAdapter?.isDiscovering!!) mBluetoothAdapter!!.cancelDiscovery()
        if(devicesList.size == 1) {
            if(presenter.onConnectFullSuit(devicesList[0].address)) {
                Snackbar.make(
                    binding!!.mainLayoutDevices,
                    "Костюм ${devicesList[0].name} подключен",
                    Snackbar.LENGTH_LONG
                ).show()
                saveStates.connectStatus = true
                binding!!.closeConnectFAB.visibility = FloatingActionButton.VISIBLE
                binding!!.connectStatusCircleIVInDevices.setImageResource(R.drawable.green_circle)
                presenter.startBroadcastCommandPresenterFullSuit()
                val view = layoutInflater.inflate(R.layout.fragment_name_device, null)
                val dataList = deviceViewModel?.getAllDevices()?.value
                for(item: Devices in dataList!!) if(item.numberDevice == devicesList[0].address) {
                    flagBeDevice = true
                    break
                }
                if(!flagBeDevice) {
                    dialogSaveDevice?.apply {
                        setCancelable(false)
                        setContentView(view)
                        show()
                    }
                }
                flagBeDevice = false
            } else {
                errorStartJob()
                binding!!.connectStatusCircleIVInDevices.setImageResource(R.drawable.red_circle)
            }
        } else {
            if(devicesList.isEmpty()) errorStartJob()
            else {
                Snackbar.make(
                    binding!!.mainLayoutDevices,
                    "Обнаружено несколько костюмов! Выключите все, кроме того, которым хотите воспользоваться",
                    Snackbar.LENGTH_LONG
                ).show()
                binding!!.connectStatusCircleIVInDevices.setImageResource(R.drawable.red_circle)
            }
        }
        dialogConnecting?.dismiss()
    }

    private fun errorStartJob() {
        Snackbar.make(binding!!.mainLayoutDevices, "Ошибка подключения!", Snackbar.LENGTH_LONG)
            .show()
        saveStates.connectStatus = false
    }

    fun closeConnect(view: View? = null) {
        if(saveStates.connectStatus) {
            presenter.apply {
                stopBroadcastCommandPresenterFullSuit()
                offConnect()
            }
            Snackbar.make(
                binding!!.mainLayoutDevices,
                "Соединение разорвано!",
                Snackbar.LENGTH_LONG
            ).show()
            saveStates.connectStatus = false
            saveStates.nameSelectDevice = ""
            saveStates.addressSelectDevice = ""
            binding!!.connectStatusCircleIVInDevices.setImageResource(R.drawable.red_circle)
            binding!!.closeConnectFAB.visibility = FloatingActionButton.INVISIBLE
            connectionStatusObserver(false)
        }
    }

    fun closeDevicesActivity(view: View) {
        finish()
    }

    private fun connectionStatusObserver(state: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
                while(state) {
                    delay(1000)
                    if(saveStates.connectStatus) launch(Dispatchers.Main) {
                        binding!!.closeConnectFAB.visibility = FloatingActionButton.VISIBLE
                        binding!!.connectStatusCircleIVInDevices.setImageResource(R.drawable.green_circle)
                    }
                    else launch(Dispatchers.Main) {
                        binding!!.closeConnectFAB.visibility = FloatingActionButton.INVISIBLE
                        binding!!.connectStatusCircleIVInDevices.setImageResource(R.drawable.red_circle)
                        binding!!.nameDeviceMainInDeviceActivity.text = ""
                        saveStates.nameSelectDevice = ""
                        saveStates.addressSelectDevice = ""
                    }
                }
        }
    }

    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            Log.d(devicesActivityTAG, "onReceive: ACTION FOUND.")
            if(action == BluetoothDevice.ACTION_FOUND) {
                if(device!!.name == "EMS OutFit") devicesList.add(device!!)
                Log.d(devicesActivityTAG, "onReceive: " + device!!.name + ": " + device!!.address)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateRecycler()
        if(saveStates.connectStatus) {
            binding!!.closeConnectFAB.visibility = FloatingActionButton.VISIBLE
            binding!!.connectStatusCircleIVInDevices.setImageResource(R.drawable.green_circle)
            binding!!.nameDeviceMainInDeviceActivity.text = saveStates.nameSelectDevice
        } else {
            binding!!.closeConnectFAB.visibility = FloatingActionButton.INVISIBLE
            binding!!.connectStatusCircleIVInDevices.setImageResource(R.drawable.red_circle)
            binding!!.nameDeviceMainInDeviceActivity.text = ""
            // Snackbar.make(binding!!.worningSnakeLayout, "Перед поиском нового костюма убедитесь что другие костюмы отключены", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy() //unregisterReceiver(mBroadcastReceiver1)
        mBluetoothAdapter?.cancelDiscovery()
    }
}