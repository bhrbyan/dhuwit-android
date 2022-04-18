package id.dhuwit.feature.overview.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.feature.overview.databinding.OverviewAccountFragmentBinding

class OverviewAccountFragment : BaseFragment() {

    private var binding: OverviewAccountFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OverviewAccountFragmentBinding.inflate(inflater, container, false)
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