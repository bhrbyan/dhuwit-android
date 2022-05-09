package id.dhuwit.feature.budget.ui.form.plan.adapter

import id.dhuwit.core.budget.model.BudgetPlan

interface BudgetFormPlanListener {
    fun onClickItemPlan(plan: BudgetPlan?)
}