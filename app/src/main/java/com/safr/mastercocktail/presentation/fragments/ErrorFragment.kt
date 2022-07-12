package com.safr.mastercocktail.presentation.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.safr.mastercocktail.databinding.FragmentErrorBinding
import com.safr.mastercocktail.presentation.viewmodels.ConnectionLiveData
import com.safr.mastercocktail.presentation.viewmodels.ErrorViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_PARAM1 = "param"

@AndroidEntryPoint
class ErrorFragment : Fragment() {

    private var param1: String? = null

    private var binding: FragmentErrorBinding? = null
    private val mBinding get() = binding!!

    private val viewModel: ErrorViewModel by viewModels()

    @Inject
    lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentErrorBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        subscribeObservers()
        reload()
    }

    private fun reload() {
        connectionLiveData.observe(viewLifecycleOwner) { isNetworkAvailable ->
            isNetworkAvailable?.let {
//                updateUI(it)
//                viewModel.start()
//                subscribeObservers()
                when (param1) {
                    "detail" -> {
                        findNavController().safeNavigate(ErrorFragmentDirections.actionErrorFragmentToCocktailDetailFragment())
                    }
                    "tab" -> {
                        findNavController().safeNavigate(ErrorFragmentDirections.actionErrorFragmentToTabFragment())
                    }
                    "list" -> {
                        findNavController().safeNavigate(ErrorFragmentDirections.actionErrorFragmentToCocktailListFragment())
                    }
                }
            }
        }
    }


//    private fun subscribeObservers() {
//
//        viewModel.isDataLoading.observe(viewLifecycleOwner) {
//            mBinding.swipe.isRefreshing = it
//        }
//
//        viewModel.isSuccess.observe(viewLifecycleOwner) { success ->
//            Log.d("lol", "ErrorFragment viewModel.isSuccess $success bb $param1 ")
//            if (success) {
//                when (param1) {
//                    "detail" -> {
//                        findNavController().safeNavigate(ErrorFragmentDirections.actionErrorFragmentToCocktailDetailFragment())
//                    }
//                    "tab" -> {
//                        findNavController().safeNavigate(ErrorFragmentDirections.actionErrorFragmentToTabFragment())
//                    }
//                    "list" -> {
//                        findNavController().safeNavigate(ErrorFragmentDirections.actionErrorFragmentToCocktailListFragment())
//                    }
//                }
//            }
//        }
//
//    }

    /*
    * защита от двойного срабатывания хоть можно сделать просто через менеджер
    * */
    fun NavController.safeNavigate(direction: NavDirections) {
        Log.d("lol", "Click happened")
        currentDestination?.getAction(direction.actionId)?.run {
            Log.d("lol", "Click Propagated")
            navigate(direction)
        }
    }


}