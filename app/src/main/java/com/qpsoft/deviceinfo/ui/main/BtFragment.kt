package com.qpsoft.deviceinfo.ui.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.scan.BleScanRuleConfig
import com.qpsoft.deviceinfo.R
import com.qpsoft.deviceinfo.util.BleDeviceOpUtil


class BtFragment : Fragment() {

    companion object {
        fun newInstance() = BtFragment()
    }

    private lateinit var mAdapter: BaseQuickAdapter<BleDevice, BaseViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_bt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUI()
    }

    private fun handleUI() {
        view?.findViewById<TextView>(R.id.tvBack)?.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance(null)).commit()
        }

        view?.findViewById<TextView>(R.id.tvRefresh)?.setOnClickListener {
            mAdapter.data.clear()
            mAdapter.notifyDataSetChanged()
            getDeviceList()
        }

        val rvBleDevice = view?.findViewById<RecyclerView>(R.id.rv)!!
        rvBleDevice.layoutManager = LinearLayoutManager(requireActivity())
        rvBleDevice.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        mAdapter = object: BaseQuickAdapter<BleDevice, BaseViewHolder>(R.layout.item_device_list) {
            override fun convert(holder: BaseViewHolder, item: BleDevice) {
                holder.setText(R.id.tvName, item.name)
                holder.setText(R.id.tvMac, item.mac)
                holder.setText(R.id.tvRssi, "信号：${item.rssi}")
            }

        }
        rvBleDevice.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, view, position ->
            BleManager.getInstance().cancelScan()
            val bleDevice = mAdapter.getItem(position)
            parentFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance(bleDevice)).commit()
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PermissionUtils.permission(
                Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        BleDeviceOpUtil.openBle()
                        getDeviceList()
                    }

                    override fun onDenied() {
                        ToastUtils.make().setNotUseSystemToast().setMode(ToastUtils.MODE.DARK)
                            .setGravity(Gravity.CENTER, 0, 0)
                            .setTopIcon(R.mipmap.ic_launcher_round).setTextSize(32)
                            .show("蓝牙权限被禁用，请手动开启")
                    }
                })
                .request()
        } else {
            PermissionUtils.permission(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        BleDeviceOpUtil.openBle()
                        getDeviceList()
                    }

                    override fun onDenied() {
                        ToastUtils.make().setNotUseSystemToast().setMode(ToastUtils.MODE.DARK)
                            .setGravity(Gravity.CENTER, 0, 0)
                            .setTopIcon(R.mipmap.ic_launcher_round).setTextSize(32)
                            .show("蓝牙权限被禁用，请手动开启")
                    }
                })
                .request()
        }


    }

    private fun getDeviceList() {
        BleManager.getInstance().initScanRule(BleScanRuleConfig())
        BleManager.getInstance().scan(object : BleScanCallback() {
            override fun onScanStarted(success: Boolean) {}
            override fun onScanning(bleDevice: BleDevice) {
                if(bleDevice.name != null) {
                    mAdapter.addData(bleDevice)
                }
            }
            override fun onScanFinished(scanResultList: List<BleDevice>) {

            }
        })
    }

}