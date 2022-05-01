package id.dhuwit.core.budget.model

sealed class BudgetType {

    object Weekly : BudgetType()
    object Monthly : BudgetType()
    object Yearly : BudgetType()

    companion object {
        private const val WEEKLY: String = "Weekly"
        private const val MONTHLY: String = "Monthly"
        private const val YEARLY: String = "Yearly"
    }

    override fun toString(): String {
        return when (this) {
            is Weekly -> WEEKLY
            is Monthly -> MONTHLY
            is Yearly -> YEARLY
            else -> throw Exception("Unknown Budget Type")
        }
    }

}
