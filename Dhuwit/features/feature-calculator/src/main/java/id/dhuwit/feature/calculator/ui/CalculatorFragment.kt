import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.dhuwit.feature.calculator.R
import id.dhuwit.feature.calculator.databinding.CalculatorFragmentBinding
import id.dhuwit.feature.calculator.ui.CalculatorListener

class CalculatorFragment : id.dhuwit.core.base.base.BaseFragment() {

    private var binding: CalculatorFragmentBinding? = null

    private var listener: CalculatorListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            context is CalculatorListener -> context
            parentFragment is CalculatorListener -> parentFragment as CalculatorListener
            else -> throw Exception("$activity or $parentFragment must implement CalculatorListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalculatorFragmentBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        listener()
        observer()
    }

    override fun init() {
        binding?.textTitle?.text = getString(R.string.calculator_bottom_sheet_head_title)
    }

    override fun listener() {
        binding?.let {
            it.buttonZero.setOnClickListener {
                listener?.onInputNumber(getString(R.string.calculator_zero))
            }

            it.buttonOne.setOnClickListener {
                listener?.onInputNumber(getString(R.string.calculator_one))
            }

            it.buttonTwo.setOnClickListener {
                listener?.onInputNumber(getString(R.string.calculator_two))
            }

            it.buttonThree.setOnClickListener {
                listener?.onInputNumber(getString(R.string.calculator_three))
            }

            it.buttonFour.setOnClickListener {
                listener?.onInputNumber(getString(R.string.calculator_four))
            }

            it.buttonFive.setOnClickListener {
                listener?.onInputNumber(getString(R.string.calculator_five))
            }

            it.buttonSix.setOnClickListener {
                listener?.onInputNumber(getString(R.string.calculator_six))
            }

            it.buttonSeven.setOnClickListener {
                listener?.onInputNumber(getString(R.string.calculator_seven))
            }

            it.buttonEight.setOnClickListener {
                listener?.onInputNumber(getString(R.string.calculator_eight))
            }

            it.buttonNine.setOnClickListener {
                listener?.onInputNumber(getString(R.string.calculator_nine))
            }

            it.buttonClear.setOnClickListener {
                listener?.onClear()
            }

            it.imageDown.setOnClickListener {
                listener?.onClose()
            }

            it.viewBlank.setOnClickListener {
                listener?.onClose()
            }
        }
    }

    override fun observer() {

    }
}