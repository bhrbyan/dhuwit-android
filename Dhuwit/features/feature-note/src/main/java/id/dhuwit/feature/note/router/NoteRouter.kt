package id.dhuwit.feature.note.router

import android.content.Context
import android.content.Intent

interface NoteRouter {
    fun openNotePage(context: Context, note: String?): Intent
}