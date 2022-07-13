package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.safr.mastercocktail.databinding.FragmentErrorBinding
import com.safr.mastercocktail.presentation.viewmodels.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param"

@AndroidEntryPoint
class ErrorFragment : Fragment() {

    private var param: String? = null
    private var nameCat: String? = null
    private var drinkId: Int? = null

    private var binding: FragmentErrorBinding? = null
    private val mBinding get() = binding!!

    private val connectionLiveData: ConnectionLiveData by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nameCat = arguments?.let { ErrorFragmentArgs.fromBundle(it).nameCat }
        param = arguments?.let { ErrorFragmentArgs.fromBundle(it).param }
        drinkId = arguments?.let { ErrorFragmentArgs.fromBundle(it).drinkId }
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
        reload()
        Log.d("lol", "ErrorFragment nameCat $nameCat  param $param")
    }

    private fun reload() {

        connectionLiveData.connect.observe(viewLifecycleOwner) { isNetworkAvailable ->

            if (isNetworkAvailable) lifecycleScope.launch {
                when (param) {
                    "detail" -> {
                            delay(5000)
                            findNavController().safeNavigate(
                                ErrorFragmentDirections.actionErrorFragmentToCocktailDetailFragment(
                                    drinkId = drinkId ?: 15346
                                )
                            )
                    }
                    "category" -> {
                        delay(5000)
                        findNavController().safeNavigate(ErrorFragmentDirections.actionErrorFragmentToTabFragment())
                    }
                    "list" -> {
                        delay(5000)
                        findNavController().safeNavigate(
                            ErrorFragmentDirections.actionErrorFragmentToCocktailListFragment(
                                nameCat
                            )
                        )
                    }
                }
            }
        }
    }

    /*
    * защита от двойного срабатывания хоть можно сделать просто через менеджер
    * */
    fun NavController.safeNavigate(direction: NavDirections) {
        Log.d("lol", "ErrorFragment Click happened")
        currentDestination?.getAction(direction.actionId)?.run {
            Log.d("lol", "ErrorFragment Click Propagated")
            navigate(direction)
        }
    }


}