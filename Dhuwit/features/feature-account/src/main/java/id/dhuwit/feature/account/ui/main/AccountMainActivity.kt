package id.dhuwit.feature.account.ui.main

import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountMainActivityBinding
import id.dhuwit.feature.account.ui.main.adapter.AccountMainAdapter
import id.dhuwit.uikit.databinding.ToolbarBinding

class AccountMainActivity : BaseActivity() {

    private lateinit var binding: AccountMainActivityBinding
    private lateinit var bindingToolbar: ToolbarBinding
    private lateinit var viewPagerCallback: ViewPager2.OnPageChangeCallback

    override fun init() {
        binding = AccountMainActivityBinding.inflate(layoutInflater)
        bindingToolbar = binding.layoutToolbar
        setContentView(binding.root)

        setUpToolbar()
        setUpViewPagerAdapter()
    }

    override fun listener() {
        viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // get data
            }
        }
        binding.viewPager.registerOnPageChangeCallback(viewPagerCallback)
    }

    override fun observer() {

    }

    private fun setUpToolbar() {
        bindingToolbar.apply {
            textTitle.apply {
                text = getString(R.string.account_main_toolbar_title)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            imageActionRight.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_add_account_white
                    )
                )
                visible()
            }
            imageActionLeft.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_setting_white))
                visible()
            }
        }
    }

    private fun setUpViewPagerAdapter() {
        val viewPagerAdapter = AccountMainAdapter(this, 1)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "$position"
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(viewPagerCallback)
    }
}