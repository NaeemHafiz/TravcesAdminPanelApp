package com.example.travcesadminpanelapp.view.fragments.login

import android.os.Bundle
import android.view.View
import com.example.travcesadminpanelapp.R
import com.example.travcesadminpanelapp.view.fragments.base.BaseFragment


class SplashFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavigation()
    }

    private fun initNavigation() {
//        bLogin.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_splashFragment_to_loginFragment))
//        bSignUp.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_splashFragment_to_signUpFragment))
    }
}