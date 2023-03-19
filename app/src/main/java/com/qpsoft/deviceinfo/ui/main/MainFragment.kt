package com.qpsoft.deviceinfo.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.clj.fastble.data.BleDevice
import com.qpsoft.deviceinfo.R
import com.qpsoft.deviceinfo.data.model.DeviceInfo
import com.qpsoft.deviceinfo.ui.login.LoginFragment

class MainFragment : Fragment() {

    companion object {
        fun newInstance(bleDevice: BleDevice?): MainFragment {
            val args = Bundle()
            if (bleDevice != null) {
                args.putString("btMac", bleDevice.mac)
                args.putString("btName", bleDevice.name)
                args.putString("deviceSn", bleDevice.name)
                args.putString("networkMac", String(bleDevice.scanRecord))
            }
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.loginOkLiveData.observe(this@MainFragment) {
            LogUtils.e("-------$it")
            if (!it) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance())
                    .commit()
            } else {

            }
        }
        viewModel.deviceInfoLiveData.observe(this@MainFragment) {
            //提交成功,清除填写框
            ToastUtils.showShort("录入成功")
            view?.findViewById<EditText>(R.id.edtBleMac)?.setText("")
            view?.findViewById<EditText>(R.id.edtBleName)?.setText("")
            view?.findViewById<EditText>(R.id.edtDeviceSn)?.setText("")
            view?.findViewById<EditText>(R.id.edtNetworkMac)?.setText("")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onResume() {
        super.onResume()
        //check login
        viewModel.checkLogin()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUI()
    }

    private fun handleUI() {
        val btMac = arguments?.getString("btMac")
        val btName = arguments?.getString("btName")
        val deviceSn = arguments?.getString("deviceSn")
        val networkMac = arguments?.getString("networkMac")
        val edtBleMac = view?.findViewById<EditText>(R.id.edtBleMac)
        val edtBleName = view?.findViewById<EditText>(R.id.edtBleName)
        val edtDeviceSn = view?.findViewById<EditText>(R.id.edtDeviceSn)
        val edtNetworkMac = view?.findViewById<EditText>(R.id.edtNetworkMac)
        edtBleMac?.setText(btMac)
        edtBleName?.setText(btName)
        edtDeviceSn?.setText(deviceSn)
        if (networkMac?.contains("QP") == true){
            val pos = networkMac.indexOf("QP")+2
            if (networkMac.length > pos + 18) edtNetworkMac?.setText(networkMac.substring(pos, pos+18))
        }


        var category = ""
        var brand = ""
        var model = ""
        val spinnerCategory = view?.findViewById<Spinner>(R.id.spinnerCategory)
        val spinnerBrand = view?.findViewById<Spinner>(R.id.spinnerBrand)
        val spinnerModel = view?.findViewById<Spinner>(R.id.spinnerModel)
        spinnerCategory?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                category = resources.getStringArray(R.array.category)[pos]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        spinnerBrand?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                brand = resources.getStringArray(R.array.brand)[pos]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        spinnerModel?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                model = resources.getStringArray(R.array.model)[pos]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        view?.findViewById<TextView>(R.id.tvSubmit)?.setOnClickListener {
            val bleMac = edtBleMac?.text.toString().trim()
            val bleName = edtBleName?.text.toString().trim()
            val deviceSn = edtDeviceSn?.text.toString().trim()
            val networkMac = edtNetworkMac?.text.toString().trim()
            val deviceInfo = DeviceInfo(category, brand, model, bleMac, bleName, deviceSn, networkMac)
            viewModel.submitDeviceInfo(deviceInfo)
        }

        view?.findViewById<TextView>(R.id.tvSearchBT)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, BtFragment.newInstance())
                .commit()
        }

        view?.findViewById<TextView>(R.id.tvLogout)?.setOnClickListener {
            CacheDiskStaticUtils.clear()
            //check login
            viewModel.checkLogin()
        }
    }

}