package id.dhuwit.feature.note

import android.content.Intent
import id.dhuwit.core.base.BaseActivity
import id.dhuwit.feature.note.NoteConstants.KEY_INPUT_NOTE
import id.dhuwit.feature.note.NoteConstants.KEY_NOTE
import id.dhuwit.feature.note.databinding.NoteActivityBinding

class NoteActivity : BaseActivity() {

    private lateinit var binding: NoteActivityBinding

    override fun init() {
        binding = NoteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            imageClose.setOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
    }

    override fun observer() {
        // Do nothing
    }

    private fun setNote(note: String?) {
        binding.inputTextNote.setText(note)
    }
}