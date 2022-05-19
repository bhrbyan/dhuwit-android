package id.dhuwit.feature.account.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.feature.account.databinding.AccountMainFragmentBinding

class AccountMainFragment : BaseFragment() {

    private var binding: AccountMainFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AccountMainFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun init() {

    }

    override fun listener() {

    }

    override fun observer() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}