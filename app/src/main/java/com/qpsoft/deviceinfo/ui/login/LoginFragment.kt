package com.qpsoft.deviceinfo.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.qpsoft.deviceinfo.R
import com.qpsoft.deviceinfo.ui.main.MainFragment

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.loginOkLiveData.observe(this@LoginFragment) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(null))
                .commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUI()
    }

    private fun handleUI() {
        view?.findViewById<TextView>(R.id.tvLogin)?.setOnClickListener {
            view?.findViewById<TextView>(R.id.tvLogin)!!.isEnabled = false
            var account = view?.findViewById<EditText>(R.id.edtAccount)?.text.toString().trim()
            var password = view?.findViewById<EditText>(R.id.edtPassword)?.text.toString().trim()

            account = "user1"
            password = "Aa@123456"
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                ToastUtils.showShort("账号或密码不能为空")
                return@setOnClickListener
            }


            viewModel.login(account, password)
        }

    }

}