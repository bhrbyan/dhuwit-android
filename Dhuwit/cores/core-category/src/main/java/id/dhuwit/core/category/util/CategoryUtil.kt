package id.dhuwit.core.category.util

import android.content.Context
import id.dhuwit.core.category.R
import id.dhuwit.core.category.model.Category
import id.dhuwit.core.category.model.CategoryType

object CategoryUtil {

    fun getCategories(context: Context): List<Category> {
        return listOf(
            Category(context.getString(R.string.category_income_salary), CategoryType.Income,1),
            Category(context.getString(R.string.category_income_invoices), CategoryType.Income,2),
            Category(context.getString(R.string.category_income_interest), CategoryType.Income, 3),
            Category(context.getString(R.string.category_income_rental_income), CategoryType.Income, 4),
            Category(context.getString(R.string.category_income_lottery), CategoryType.Income, 5),
            Category(context.getString(R.string.category_expense_food_drink), CategoryType.Expense, 6),
            Category(context.getString(R.string.category_expense_groceries), CategoryType.Expense, 7),
            Category(context.getString(R.string.category_expense_restaurant), CategoryType.Expense, 8),
            Category(context.getString(R.string.category_expense_bar_cafe), CategoryType.Expense, 9),
            Category(context.getString(R.string.category_expense_shopping), CategoryType.Expense, 10),
            Category(context.getString(R.string.category_expense_clothes_shoes), CategoryType.Expense, 11),
            Category(context.getString(R.string.category_expense_jewels_accessories), CategoryType.Expense, 12),
            Category(context.getString(R.string.category_expense_health_beauty), CategoryType.Expense, 13),
            Category(context.getString(R.string.category_expense_kids), CategoryType.Expense, 14),
            Category(context.getString(R.string.category_expense_pets), CategoryType.Expense, 15),
            Category(context.getString(R.string.category_expense_electronics_accessories), CategoryType.Expense, 16),
            Category(context.getString(R.string.category_expense_gifts), CategoryType.Expense, 17),
            Category(context.getString(R.string.category_expense_stationery_tools), CategoryType.Expense, 18),
            Category(context.getString(R.string.category_expense_medicine), CategoryType.Expense, 19),
            Category(context.getString(R.string.category_expense_housing), CategoryType.Expense, 20),
            Category(context.getString(R.string.category_expense_rent), CategoryType.Expense, 21),
            Category(context.getString(R.string.category_expense_service), CategoryType.Expense, 22),
            Category(context.getString(R.string.category_expense_energy_utilities), CategoryType.Expense, 23),
            Category(context.getString(R.string.category_expense_house_maintenance), CategoryType.Expense, 24),
            Category(context.getString(R.string.category_expense_transportation), CategoryType.Expense, 25),
            Category(context.getString(R.string.category_expense_vehicle), CategoryType.Expense, 26),
            Category(context.getString(R.string.category_expense_vehicle_maintenance), CategoryType.Expense, 27),
            Category(context.getString(R.string.category_expense_fuel), CategoryType.Expense, 28),
            Category(context.getString(R.string.category_expense_parking), CategoryType.Expense, 29),
            Category(context.getString(R.string.category_expense_lifestyle), CategoryType.Expense, 30),
            Category(context.getString(R.string.category_expense_sports), CategoryType.Expense, 31),
            Category(context.getString(R.string.category_expense_hobbies), CategoryType.Expense, 32),
            Category(context.getString(R.string.category_expense_education), CategoryType.Expense, 33),
            Category(context.getString(R.string.category_expense_books), CategoryType.Expense, 34),
            Category(context.getString(R.string.category_expense_charity), CategoryType.Expense, 35),
        )
    }

}