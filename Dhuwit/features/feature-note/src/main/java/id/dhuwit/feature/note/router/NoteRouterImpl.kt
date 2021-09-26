package id.dhuwit.feature.note.router

import android.content.Context
import android.content.Intent
import id.dhuwit.feature.note.NoteActivity
import id.dhuwit.feature.note.NoteConstants.KEY_NOTE

object NoteRouterImpl : NoteRouter {
    override fun openNotePage(context: Context, note: String?): Intent {
        return Intent(context, NoteActivity::class.java).apply {
            putExtra(KEY_NOTE, note)
        }
    }
}