package id.dhuwit.core.budget.model

sealed class BudgetPeriodType {

    object Weekly : BudgetPeriodType()
    object Monthly : BudgetPeriodType()
    object Yearly : BudgetPeriodType()

    companion object {
        private const val WEEKLY: String = "Weekly"
        private const val MONTHLY: String = "Monthly"
        private const val YEARLY: String = "Yearly"

        fun getBudgetPeriodType(periodType: String?): BudgetPeriodType {
            return when (periodType) {
                WEEKLY -> Weekly
                MONTHLY -> Monthly
                YEARLY -> Yearly
                else -> throw Exception("Unknown Period Type")
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            is Weekly -> WEEKLY
            is Monthly -> MONTHLY
            is Yearly -> YEARLY
            else -> throw Exception("Unknown Period Type")
        }
    }

}
