package id.dhuwit.core.account.model

import android.graphics.drawable.Drawable

data class AccountOnBoarding(
    val name: String,
    val description: String,
    val icon: Drawable?,
    val backgroundColor: Int,
)
