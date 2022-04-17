package id.dhuwit.feature.budget.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.dhuwit.core.base.BaseFragment
import id.dhuwit.feature.budget.databinding.BudgetFragmentBinding

class BudgetFragment : BaseFragment() {

    private var binding: BudgetFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BudgetFragmentBinding.inflate(inflater, container, false)

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