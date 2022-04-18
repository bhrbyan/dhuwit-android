package id.dhuwit.feature.overview.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.feature.overview.databinding.OverviewCategoryFragmentBinding

class OverviewCategoryFragment : BaseFragment() {

    private var binding: OverviewCategoryFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OverviewCategoryFragmentBinding.inflate(inflater, container, false)
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