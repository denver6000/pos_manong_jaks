package com.denproj.posmanongjaks.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.denproj.posmanongjaks.databinding.FragmentSettingsBinding
import com.denproj.posmanongjaks.util.NetworkStatusMonitor
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel
import com.denproj.posmanongjaks.viewModel.SettingsFragmentViewmodel
import com.denproj.posmanongjaks.viewModel.SettingsFragmentViewmodel.OnLogOutSuccessful
import com.denproj.posmanongjaks.viewModel.SettingsFragmentViewmodel.OnPasswordResetFinished
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    val homeActivityViewmodel: HomeActivityViewmodel by activityViewModels<HomeActivityViewmodel> ()
    val settingsFragmentViewmodel: SettingsFragmentViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSettingsBinding.inflate(
            layoutInflater
        )
        NetworkStatusMonitor().isConnected

        settingsFragmentViewmodel.mutableLiveData.observe(viewLifecycleOwner) {
            binding.changePassword.setOnClickListener { view: View? ->
                settingsFragmentViewmodel!!.resetPassword(
                    it,
                    object : OnPasswordResetFinished {
                        override fun onSuccess() {
                            Toast.makeText(
                                requireActivity(),
                                "Password reset email sent on email address used for this account.",
                                Toast.LENGTH_SHORT
                            ).show()
                            requireActivity().finish()
                        }

                        override fun onFail() {
                            Toast.makeText(
                                requireActivity(),
                                "Connection is not reachable or illegal user session.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
            binding.logOutBtn.setOnClickListener { view: View? ->
                settingsFragmentViewmodel!!.logout(object : OnLogOutSuccessful {
                    override fun onSuccess() {
                        requireActivity().finish()
                    }

                    override fun onFail(e: Exception?) {
                        requireActivity().finish()
                        Toast.makeText(requireContext(), e!!.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        return binding.root
    }
}