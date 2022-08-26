package id.dhuwit.feature.note

import android.content.Intent
import androidx.core.content.ContextCompat
import id.dhuwit.core.base.extension.visible
import id.dhuwit.feature.note.NoteConstants.KEY_INPUT_NOTE
import id.dhuwit.feature.note.NoteConstants.KEY_NOTE
import id.dhuwit.feature.note.databinding.NoteActivityBinding
import id.dhuwit.uikit.databinding.ToolbarBinding

class NoteActivity : id.dhuwit.core.base.base.BaseActivity() {

    private lateinit var binding: NoteActivityBinding
    private lateinit var bindingToolbar: ToolbarBinding

    override fun init() {
        binding = NoteActivityBinding.inflate(layoutInflater)
        bindingToolbar = binding.layoutToolbar
        setContentView(binding.root)

        setUpToolbar()

        val note = intent.getStringExtra(KEY_NOTE)
        setNote(note)
    }

    override fun listener() {
        with(binding) {
            buttonSave.setOnClickListener {
                val dateNote: String = binding.inputTextNote.text.toString()
                val data = Intent().apply { putExtra(KEY_INPUT_NOTE, dateNote) }
                setResult(RESULT_OK, data)
                finish()
            }
        }
    }

    override fun observer() {
        // Do nothing
    }

    private fun setUpToolbar() {
        bindingToolbar.apply {
            textTitle.text = getString(R.string.note_title)
            imageActionLeft.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close))
                setOnClickListener {
                    setResult(RESULT_CANCELED)
                    finish()
                }
                visible()
            }
        }
    }

    private fun setNote(note: String?) {
        binding.inputTextNote.setText(note)
    }
}