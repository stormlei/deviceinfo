package com.qpsoft.deviceinfo.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.blankj.utilcode.util.LogUtils
import com.clj.fastble.data.BleDevice
import com.qpsoft.deviceinfo.R
import com.qpsoft.deviceinfo.data.model.DeviceInfo
import com.qpsoft.deviceinfo.ui.login.LoginFragment

class MainFragment : Fragment() {

    companion object {
        fun newInstance(bleDevice: BleDevice?): MainFragment {
            val args = Bundle()
            args.putString("btMac", bleDevice?.mac)
            args.putString("btName", bleDevice?.name)
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
            view?.findViewById<TextView>(R.id.tvSubmit)!!.isEnabled = true
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
        val edtBleMac = view?.findViewById<EditText>(R.id.edtBleMac)
        val edtBleName = view?.findViewById<EditText>(R.id.edtBleName)
        edtBleMac?.setText(btMac)
        edtBleName?.setText(btName)

        view?.findViewById<TextView>(R.id.tvSubmit)?.setOnClickListener {
            view?.findViewById<TextView>(R.id.tvSubmit)!!.isEnabled = false
            val category = view?.findViewById<EditText>(R.id.edtCategory)?.text.toString().trim()
            val brand = view?.findViewById<EditText>(R.id.edtBrand)?.text.toString().trim()
            val model = view?.findViewById<EditText>(R.id.edtModel)?.text.toString().trim()
            val bleMac = edtBleMac?.text.toString().trim()
            val bleName = edtBleName?.text.toString().trim()
            val sn = view?.findViewById<EditText>(R.id.edtSn)?.text.toString().trim()
            val networkMac = view?.findViewById<EditText>(R.id.edtNetworkMac)?.text.toString().trim()
            val deviceInfo = DeviceInfo(category, brand, model, bleMac, bleName, sn, networkMac)
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